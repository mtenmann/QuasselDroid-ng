package de.kuschku.quasseldroid_ng.protocol.primitive.serializer

import de.kuschku.quasseldroid_ng.protocol.Protocol_Features
import de.kuschku.quasseldroid_ng.protocol.Quassel_Features
import de.kuschku.quasseldroid_ng.quassel.Protocol
import de.kuschku.quasseldroid_ng.util.nio.ChainedByteBuffer
import java.nio.ByteBuffer

object ProtocolSerializer : Serializer<Protocol> {
  override fun serialize(buffer: ChainedByteBuffer, data: Protocol, features: Quassel_Features) {
    ByteSerializer.serialize(buffer, data.flags.toByte(), features)
    ShortSerializer.serialize(buffer, data.data, features)
    ByteSerializer.serialize(buffer, data.version, features)
  }

  override fun deserialize(buffer: ByteBuffer, features: Quassel_Features): Protocol {
    return Protocol(
      Protocol_Features.of(ByteSerializer.deserialize(buffer, features).toInt()),
      ShortSerializer.deserialize(buffer, features),
      ByteSerializer.deserialize(buffer, features)
    )
  }
}
