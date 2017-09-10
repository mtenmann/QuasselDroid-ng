package de.kuschku.quasseldroid_ng.quassel.syncables

import de.kuschku.quasseldroid_ng.protocol.*
import de.kuschku.quasseldroid_ng.quassel.syncables.interfaces.IIrcUser
import de.kuschku.quasseldroid_ng.session.SignalProxy
import org.threeten.bp.Instant
import java.nio.charset.Charset

class IrcUser(
  hostmask: String,
  network: Network,
  proxy: SignalProxy
) : SyncableObject(proxy, "IrcUser"), IIrcUser {
  override fun init() {
    updateObjectName()
  }

  override fun toVariantMap() = initProperties()

  override fun fromVariantMap(properties: QVariantMap) {
    initSetProperties(properties)
  }

  override fun initProperties(): QVariantMap = mapOf(
    "user" to QVariant_(user(), Type.QString),
    "host" to QVariant_(host(), Type.QString),
    "nick" to QVariant_(nick(), Type.QString),
    "realName" to QVariant_(realName(), Type.QString),
    "account" to QVariant_(account(), Type.QString),
    "away" to QVariant_(isAway(), Type.Bool),
    "awayMessage" to QVariant_(awayMessage(), Type.QString),
    "idleTime" to QVariant_(idleTime(), Type.QDateTime),
    "loginTime" to QVariant_(loginTime(), Type.QDateTime),
    "server" to QVariant_(server(), Type.QString),
    "ircOperator" to QVariant_(ircOperator(), Type.QString),
    "lastAwayMessage" to QVariant_(lastAwayMessage(), Type.Int),
    "whoisServiceReply" to QVariant_(whoisServiceReply(), Type.QString),
    "suserHost" to QVariant_(suserHost(), Type.QString),
    "encrypted" to QVariant_(encrypted(), Type.Bool),

    "channels" to QVariant_(channels(), Type.QStringList),
    "userModes" to QVariant_(userModes(), Type.QString)
  )

  override fun initSetProperties(properties: QVariantMap) {
    setUser(properties["user"].value(user()))
    setHost(properties["host"].value(host()))
    setNick(properties["nick"].value(nick()))
    setRealName(properties["realName"].value(realName()))
    setAccount(properties["account"].value(account()))
    setAway(properties["away"].value(isAway()))
    setAwayMessage(properties["awayMessage"].value(awayMessage()))
    setIdleTime(properties["idleTime"].value(idleTime()))
    setLoginTime(properties["loginTime"].value(loginTime()))
    setServer(properties["server"].value(server()))
    setIrcOperator(properties["ircOperator"].value(ircOperator()))
    setLastAwayMessage(properties["lastAwayMessage"].value(lastAwayMessage()))
    setWhoisServiceReply(properties["whoisServiceReply"].value(whoisServiceReply()))
    setSuserHost(properties["suserHost"].value(suserHost()))
    setEncrypted(properties["encrypted"].value(encrypted()))

    setUserModes(properties["userModes"].value(userModes()))
  }

  fun user() = _user
  fun host() = _host
  fun nick() = _nick
  fun realName() = _realName
  fun account() = _account
  fun hostMask() = "${nick()}!${user()}@${host()}"
  fun isAway() = _away
  fun awayMessage() = _awayMessage
  fun idleTime(): Instant {
    if (Instant.now().epochSecond - _idleTimeSet.epochSecond > 1200)
      _idleTime = Instant.EPOCH
    return _idleTime
  }

  fun loginTime() = _loginTime
  fun server() = _server
  fun ircOperator() = _ircOperator
  fun lastAwayMessage() = _lastAwayMessage
  fun whoisServiceReply() = _whoisServiceReply
  fun suserHost() = _suserHost
  fun encrypted() = _encrypted
  fun network() = _network
  fun userModes() = _userModes
  fun channels() = _channels.map(IrcChannel::name)
  fun codecForEncoding() = _codecForEncoding
  fun codecForDecoding() = _codecForDecoding
  fun setCodecForEncoding(codecName: String) = setCodecForEncoding(Charset.forName(codecName))
  fun setCodecForEncoding(codec: Charset) {
    _codecForEncoding = codec
  }

  fun setCodecForDecoding(codecName: String) = setCodecForDecoding(Charset.forName(codecName))
  fun setCodecForDecoding(codec: Charset) {
    _codecForDecoding = codec
  }

  override fun setUser(user: String) {
    if (_user != user) {
      _user = user
      super.setUser(user)
    }
  }

  override fun setHost(host: String) {
    if (_host != host) {
      _host = host
      super.setHost(host)
    }
  }

  override fun setNick(nick: String) {
    if (nick.isNotBlank() && _nick != nick) {
      _nick = nick
      updateObjectName()
      super.setNick(nick)
    }
  }

  override fun setRealName(realName: String) {
    if (_realName != realName) {
      _realName = realName
      super.setRealName(realName)
    }
  }

  override fun setAccount(account: String) {
    if (_account != account) {
      _account = account
      super.setAccount(account)
    }
  }

  override fun setAway(away: Boolean) {
    if (_away != away) {
      _away = away
      super.setAway(away)
    }
  }

  override fun setAwayMessage(awayMessage: String) {
    if (_awayMessage != awayMessage) {
      _awayMessage = awayMessage
      super.setAwayMessage(awayMessage)
    }
  }

  override fun setIdleTime(idleTime: Instant) {
    if (_idleTime != idleTime) {
      _idleTime = idleTime
      _idleTimeSet = Instant.now()
      super.setIdleTime(idleTime)
    }
  }

  override fun setLoginTime(loginTime: Instant) {
    if (_loginTime != loginTime) {
      _loginTime = loginTime
      super.setLoginTime(loginTime)
    }
  }

  override fun setIrcOperator(ircOperator: String) {
    if (_ircOperator != ircOperator) {
      _ircOperator = ircOperator
      super.setIrcOperator(ircOperator)
    }
  }

  override fun setLastAwayMessage(lastAwayMessage: Int) {
    if (lastAwayMessage > _lastAwayMessage) {
      _lastAwayMessage = lastAwayMessage
      super.setLastAwayMessage(lastAwayMessage)
    }
  }

  override fun setWhoisServiceReply(whoisServiceReply: String) {
    if (_whoisServiceReply != whoisServiceReply) {
      _whoisServiceReply = whoisServiceReply
      super.setWhoisServiceReply(whoisServiceReply)
    }
  }

  override fun setSuserHost(suserHost: String) {
    if (_suserHost != suserHost) {
      _suserHost = suserHost
      super.setSuserHost(suserHost)
    }
  }

  override fun setEncrypted(encrypted: Boolean) {
    if (_encrypted != encrypted) {
      _encrypted = encrypted
      super.setEncrypted(encrypted)
    }
  }

  override fun updateHostmask(mask: String) {
    if (hostMask() != mask) {
      val (user, host, _) = splitHostMask(mask)
      setUser(user)
      setHost(host)
    }
  }

  override fun setUserModes(modes: String) {
    if (_userModes != modes) {
      _userModes = modes
      super.setUserModes(modes)
    }
  }

  override fun joinChannel(channel: IrcChannel, skip_channel_join: Boolean) {
    if (!_channels.contains(channel)) {
      _channels.add(channel)
      if (!skip_channel_join)
        channel.joinIrcUser(this)
    }
  }

  override fun joinChannel(channelname: String) {
    joinChannel(network().newIrcChannel(channelname))
  }

  override fun partChannel(channel: IrcChannel) {
    if (_channels.contains(channel)) {
      _channels.remove(channel)
      channel.part(this)
      super.partChannel(channel.name())
      if (_channels.isEmpty() && !network().isMe(this))
        quit()
    }
  }

  override fun partChannel(channelname: String) {
    val channel = network().ircChannel(channelname)
      ?: throw IllegalArgumentException("Received part for unknown channel : $channelname")
    partChannel(channel)
  }

  override fun quit() {
    for (channel in _channels) {
      channel.part(this)
    }
    _channels.clear()
    network().removeIrcUser(this)
    super.quit()
    proxy.stopSynchronize(this)
  }

  override fun addUserModes(modes: String) {
    super.addUserModes(modes)
  }

  override fun removeUserModes(modes: String) {
    super.removeUserModes(modes)
  }

  fun updateObjectName() {
    renameObject("${network().networkId()}/$_nick")
  }

  private var _nick: String = nickFromMask(hostmask)
  private var _user: String = userFromMask(hostmask)
  private var _host: String = hostFromMask(hostmask)
  private var _realName: String = ""
  private var _account: String = ""
  private var _awayMessage: String = ""
  private var _away: Boolean = false
  private var _server: String = ""
  private var _idleTime: Instant = Instant.EPOCH
  private var _idleTimeSet: Instant = Instant.EPOCH
  private var _loginTime: Instant = Instant.EPOCH
  private var _ircOperator: String = ""
  private var _lastAwayMessage: Int = 0
  private var _whoisServiceReply: String = ""
  private var _suserHost: String = ""
  private var _encrypted: Boolean = false
  private var _channels: MutableSet<IrcChannel> = mutableSetOf()
  private var _userModes: String = ""
  private var _network: Network = network
  private var _codecForEncoding: Charset? = null
  private var _codecForDecoding: Charset? = null
}
