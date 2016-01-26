package de.kuschku.libquassel;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.kuschku.libquassel.events.ConnectionChangeEvent;
import de.kuschku.libquassel.events.GeneralErrorEvent;
import de.kuschku.libquassel.events.HandshakeFailedEvent;
import de.kuschku.libquassel.functions.types.HandshakeFunction;
import de.kuschku.libquassel.objects.types.ClientInit;
import de.kuschku.libquassel.primitives.QMetaTypeRegistry;
import de.kuschku.libquassel.primitives.serializers.ProtocolSerializer;
import de.kuschku.libquassel.primitives.types.Protocol;
import de.kuschku.libquassel.protocols.DatastreamPeer;
import de.kuschku.libquassel.protocols.LegacyPeer;
import de.kuschku.libquassel.protocols.RemotePeer;
import de.kuschku.util.ServerAddress;
import de.kuschku.util.niohelpers.WrappedChannel;

import static de.kuschku.libquassel.primitives.QMetaType.Type.UInt;
import static de.kuschku.util.AndroidAssert.*;

/**
 * Starts a connection to a core and handles the data in the backend.
 * Provides a Client object for interacting with a friendly tree structure of the data.
 *
 * @author Janne Koschinski
 */
public class CoreConnection {

    @NonNull
    private final ServerAddress address;
    @Nullable
    private ExecutorService outputExecutor;
    @Nullable
    private ExecutorService inputExecutor;
    @Nullable
    private RemotePeer remotePeer;
    @NonNull
    private final ClientData clientData;
    @NonNull
    private final BusProvider busProvider;
    @Nullable
    private WrappedChannel channel;
    @Nullable
    private Socket socket;
    @NonNull
    private ConnectionChangeEvent.Status status = ConnectionChangeEvent.Status.DISCONNECTED;
    @Nullable
    private Client client;

    public CoreConnection(@NonNull final ServerAddress address, @NonNull final ClientData clientData, @NonNull final BusProvider busProvider) {
        this.address = address;
        this.clientData = clientData;
        this.busProvider = busProvider;
    }

    @NonNull
    public ConnectionChangeEvent.Status getStatus() {
        return status;
    }

    /**
     * This method opens a socket to the specified address and starts the connection process.
     *
     * @throws IOException
     * @param supportsKeepAlive If the connection may use keepAlive
     */
    public void open(boolean supportsKeepAlive) throws IOException {
        assertNotNull(client);

        // Intialize socket
        socket = new Socket();
        if (supportsKeepAlive) socket.setKeepAlive(true);
        socket.connect(new InetSocketAddress(address.host, address.port), 10000);

        // Wrap socket in channel for nio functions
        channel = WrappedChannel.ofSocket(socket);

        busProvider.event.register(this);
        client.setConnectionStatus(ConnectionChangeEvent.Status.HANDSHAKE);

        // Create executor for write events
        outputExecutor = Executors.newSingleThreadExecutor();
        inputExecutor = Executors.newSingleThreadExecutor();

        // Execute handshake
        handshake();
    }

    /**
     * Closes the connection and interrupts all threads this connection has spawned.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        assertNotNull(client);

        client.setConnectionStatus(ConnectionChangeEvent.Status.DISCONNECTED);

        // We can do this because we clean up the file handles ourselves
        if (inputExecutor != null) inputExecutor.shutdownNow();
        if (outputExecutor != null) outputExecutor.shutdownNow();

        // Which we do exactly here
        if (channel != null) channel.close();
        if (socket != null) socket.close();
    }

    @Nullable
    public ExecutorService getOutputExecutor() {
        return outputExecutor;
    }

    @NonNull
    public ClientData getClientData() {
        return clientData;
    }

    @NonNull
    public WrappedChannel getChannel() {
        assertNotNull(channel);

        return channel;
    }

    @Nullable
    public RemotePeer getRemotePeer() {
        return remotePeer;
    }

    @Nullable
    public Socket getSocket() {
        return socket;
    }

    /**
     * Starts the first handshake phase with negotiating the protocol and if SSL or compression are to be used.
     *
     * @throws IOException
     */
    private void handshake() throws IOException {
        assertNotNull(channel);
        assertNotNull(inputExecutor);

        // Start protocol handshake with magic version and feature flags
        QMetaTypeRegistry.serialize(UInt, channel, 0x42b33f00 | clientData.flags.flags);

        // Send list of supported protocols
        for (int supportedProtocol : clientData.supportedProtocols) {
            QMetaTypeRegistry.serialize(UInt, channel, supportedProtocol);
        }
        QMetaTypeRegistry.serialize(UInt, channel, 0x01 << 31);

        // Spawn and start a new read thread
        inputExecutor.submit(new ReadRunnable());
    }

    public void onEventAsync(HandshakeFailedEvent event) {
        try {
            this.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEventAsync(@NonNull ConnectionChangeEvent event) {
        this.status = event.status;
    }

    public void setCompression(boolean supportsCompression) {
        if (supportsCompression) channel = WrappedChannel.withCompression(getChannel());
    }

    public void setClient(@NonNull Client client) {
        this.client = client;
    }

    /**
     * A runnable that reads from the channel and calls the functions responsible for processing the read data.
     */
    private class ReadRunnable implements Runnable {
        @Override
        public void run() {
            assertNotNull(client);

            try {
                boolean hasReadPreHandshake = false;
                while (true) {
                    if (!hasReadPreHandshake) {
                        final ByteBuffer buffer = ByteBuffer.allocate(4);
                        assertNotNull(buffer);
                        getChannel().read(buffer);

                        final Protocol protocol = ProtocolSerializer.get().deserialize(buffer);

                        // Wrap socket in deflater if compression is enabled
                        setCompression(protocol.protocolFlags.supportsCompression);

                        // Initialize remote peer
                        switch (protocol.protocolVersion) {
                            case 0x01:
                                remotePeer = new LegacyPeer(CoreConnection.this, busProvider);
                                break;
                            case 0x02:
                                remotePeer = new DatastreamPeer(CoreConnection.this, busProvider);
                                break;
                            default:
                                busProvider.sendEvent(new HandshakeFailedEvent("Core too new: Protocol Unsupported"));
                                close();
                                return;
                        }

                        // Mark prehandshake as read
                        hasReadPreHandshake = true;

                        // Send client data to core
                        String clientDate = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.US).format(new Date());
                        assertNotNull(clientDate);
                        busProvider.dispatch(new HandshakeFunction(new ClientInit(
                                clientDate,
                                protocol.protocolFlags.supportsSSL,
                                getClientData().identifier,
                                false,
                                getClientData().protocolVersion
                        )));
                    } else {
                        remotePeer.processMessage();
                    }
                }
            } catch (SocketException e) {
                Log.e("libquassel", "Socket closed while reading");
                client.setConnectionStatus(ConnectionChangeEvent.Status.DISCONNECTED);
            } catch (Exception e) {
                busProvider.sendEvent(new GeneralErrorEvent(e));
            }
        }
    }
}
