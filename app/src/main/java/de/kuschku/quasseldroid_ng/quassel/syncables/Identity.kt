package de.kuschku.quasseldroid_ng.quassel.syncables

import de.kuschku.quasseldroid_ng.protocol.*
import de.kuschku.quasseldroid_ng.quassel.syncables.interfaces.IIdentity
import de.kuschku.quasseldroid_ng.session.SignalProxy

class Identity constructor(
  proxy: SignalProxy
) : SyncableObject(proxy, "Identity"), IIdentity {
  override fun toVariantMap() = initProperties()
  override fun fromVariantMap(properties: QVariantMap) {
    initSetProperties(properties)
  }

  override fun init() {
    renameObject("${id()}")
  }

  override fun initProperties(): QVariantMap = mapOf(
    "identityId" to QVariant_(id(), QType.IdentityId),
    "identityName" to QVariant_(identityName(), Type.QString),
    "realName" to QVariant_(realName(), Type.QString),
    "nicks" to QVariant_(nicks(), Type.QStringList),
    "awayNick" to QVariant_(awayNick(), Type.QString),
    "awayNickEnabled" to QVariant_(awayNickEnabled(), Type.Bool),
    "awayReason" to QVariant_(awayReason(), Type.QString),
    "awayReasonEnabled" to QVariant_(awayReasonEnabled(), Type.Bool),
    "autoAwayEnabled" to QVariant_(autoAwayEnabled(), Type.Bool),
    "autoAwayTime" to QVariant_(autoAwayTime(), Type.Int),
    "autoAwayReason" to QVariant_(autoAwayReason(), Type.QString),
    "autoAwayReasonEnabled" to QVariant_(autoAwayReasonEnabled(), Type.Bool),
    "detachAwayEnabled" to QVariant_(detachAwayEnabled(), Type.Bool),
    "detachAwayReason" to QVariant_(detachAwayReason(), Type.QString),
    "detachAwayReasonEnabled" to QVariant_(detachAwayReasonEnabled(), Type.Bool),
    "ident" to QVariant_(ident(), Type.QString),
    "kickReason" to QVariant_(kickReason(), Type.QString),
    "partReason" to QVariant_(partReason(), Type.QString),
    "quitReason" to QVariant_(quitReason(), Type.QString)
  )

  override fun initSetProperties(properties: QVariantMap) {
    setId(properties["identityId"].value(id()))
    setIdentityName(properties["identityName"].value(identityName()))
    setRealName(properties["realName"].value(realName()))
    setNicks(properties["nicks"].value(nicks()))
    setAwayNick(properties["awayNick"].value(awayNick()))
    setAwayNickEnabled(properties["awayNickEnabled"].value(awayNickEnabled()))
    setAwayReason(properties["awayReason"].value(awayReason()))
    setAwayReasonEnabled(properties["awayReasonEnabled"].value(awayReasonEnabled()))
    setAutoAwayEnabled(properties["autoAwayEnabled"].value(autoAwayEnabled()))
    setAutoAwayTime(properties["autoAwayTime"].value(autoAwayTime()))
    setAutoAwayReason(properties["autoAwayReason"].value(autoAwayReason()))
    setAutoAwayReasonEnabled(properties["autoAwayReasonEnabled"].value(autoAwayReasonEnabled()))
    setDetachAwayEnabled(properties["detachAwayEnabled"].value(detachAwayEnabled()))
    setDetachAwayReason(properties["detachAwayReason"].value(detachAwayReason()))
    setDetachAwayReasonEnabled(
      properties["detachAwayReasonEnabled"].value(detachAwayReasonEnabled()))
    setIdent(properties["ident"].value(ident()))
    setKickReason(properties["kickReason"].value(kickReason()))
    setPartReason(properties["partReason"].value(partReason()))
    setQuitReason(properties["quitReason"].value(quitReason()))
  }

  fun id() = _identityId
  fun identityName() = _identityName
  fun realName() = _realName
  fun nicks() = _nicks
  fun awayNick() = _awayNick
  fun awayNickEnabled() = _awayNickEnabled
  fun awayReason() = _awayReason
  fun awayReasonEnabled() = _awayReasonEnabled
  fun autoAwayEnabled() = _autoAwayEnabled
  fun autoAwayTime() = _autoAwayTime
  fun autoAwayReason() = _autoAwayReason
  fun autoAwayReasonEnabled() = _autoAwayReasonEnabled
  fun detachAwayEnabled() = _detachAwayEnabled
  fun detachAwayReason() = _detachAwayReason
  fun detachAwayReasonEnabled() = _detachAwayReasonEnabled
  fun ident() = _ident
  fun kickReason() = _kickReason
  fun partReason() = _partReason
  fun quitReason() = _quitReason

  override fun setAutoAwayEnabled(enabled: Boolean) {
    _autoAwayEnabled = enabled
    super.setAutoAwayEnabled(enabled)
  }

  override fun setAutoAwayReason(reason: String) {
    _autoAwayReason = reason
    super.setAutoAwayReason(reason)
  }

  override fun setAutoAwayReasonEnabled(enabled: Boolean) {
    _autoAwayReasonEnabled = enabled
    super.setAutoAwayReasonEnabled(enabled)
  }

  override fun setAutoAwayTime(time: Int) {
    _autoAwayTime = time
    super.setAutoAwayTime(time)
  }

  override fun setAwayNick(awayNick: String) {
    _awayNick = awayNick
    super.setAwayNick(awayNick)
  }

  override fun setAwayNickEnabled(enabled: Boolean) {
    _awayNickEnabled = enabled
    super.setAwayNickEnabled(enabled)
  }

  override fun setAwayReason(awayReason: String) {
    _awayReason = awayReason
    super.setAwayReason(awayReason)
  }

  override fun setAwayReasonEnabled(enabled: Boolean) {
    _awayReasonEnabled = enabled
    super.setAwayReasonEnabled(enabled)
  }

  override fun setDetachAwayEnabled(enabled: Boolean) {
    _detachAwayEnabled = enabled
    super.setDetachAwayEnabled(enabled)
  }

  override fun setDetachAwayReason(reason: String) {
    _detachAwayReason = reason
    super.setDetachAwayReason(reason)
  }

  override fun setDetachAwayReasonEnabled(enabled: Boolean) {
    _detachAwayReasonEnabled = enabled
    super.setDetachAwayReasonEnabled(enabled)
  }

  override fun setId(id: IdentityId) {
    _identityId = id
    super.setId(id)
  }

  override fun setIdent(ident: String) {
    _ident = ident
    super.setIdent(ident)
  }

  override fun setIdentityName(name: String) {
    _identityName = name
    super.setIdentityName(name)
  }

  override fun setKickReason(reason: String) {
    _kickReason = reason
    super.setKickReason(reason)
  }

  override fun setNicks(nicks: QStringList) {
    _nicks = nicks.filterNotNull().toMutableList()
    super.setNicks(nicks)
  }

  override fun setPartReason(reason: String) {
    _partReason = reason
    super.setPartReason(reason)
  }

  override fun setQuitReason(reason: String) {
    _quitReason = reason
    super.setQuitReason(reason)
  }

  override fun setRealName(realName: String) {
    _realName = realName
    super.setRealName(realName)
  }

  private var _identityId: IdentityId = -1
  private var _identityName: String = "<empty>"
  private var _realName: String = ""
  private var _nicks: MutableList<String> = mutableListOf("quassel")
  private var _awayNick: String = ""
  private var _awayNickEnabled: Boolean = false
  private var _awayReason: String = "Gone fishing."
  private var _awayReasonEnabled: Boolean = true
  private var _autoAwayEnabled: Boolean = false
  private var _autoAwayTime: Int = 10
  private var _autoAwayReason: String = "Not here. No, really. not here!"
  private var _autoAwayReasonEnabled: Boolean = false
  private var _detachAwayEnabled: Boolean = false
  private var _detachAwayReason: String = "All Quassel clients vanished from the face of the earth..."
  private var _detachAwayReasonEnabled: Boolean = false
  private var _ident: String = "quassel"
  private var _kickReason: String = "Kindergarten is elsewhere!"
  private var _partReason: String = "http://quassel-irc.org - Chat comfortably. Anywhere."
  private var _quitReason: String = "http://quassel-irc.org - Chat comfortably. Anywhere."
}
