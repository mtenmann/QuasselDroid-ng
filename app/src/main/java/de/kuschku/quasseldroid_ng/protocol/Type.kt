package de.kuschku.quasseldroid_ng.protocol

import java.util.*


enum class Type(val id: kotlin.Int) {
  Void(0),
  Bool(1),
  Int(2),
  UInt(3),
  LongLong(4),
  ULongLong(5),

  Double(6),
  QChar(7),
  QVariantMap(8),
  QVariantList(9),

  QString(10),
  QStringList(11),
  QByteArray(12),

  QBitArray(13),
  QDate(14),
  QTime(15),
  QDateTime(16),
  QUrl(17),

  QLocale(18),
  QRect(19),
  QRectF(20),
  QSize(21),
  QSizeF(22),

  QLine(23),
  QLineF(24),
  QPoint(25),
  QPointF(26),
  QRegExp(27),

  QVariantHash(28),
  QEasingCurve(29),

  FirstGuiType(63),

  QFont(64),
  QPixmap(65),
  QBrush(66),
  QColor(67),
  QPalette(68),

  QIcon(69),
  QImage(70),
  QPolygon(71),
  QRegion(72),
  QBitmap(73),

  QCursor(74),
  QSizePolicy(75),
  QKeySequence(76),
  QPen(77),

  QTextLength(78),
  QTextFormat(79),
  QMatrix(80),
  QTransform(81),

  QMatrix4x4(82),
  QVector2D(83),
  QVector3D(84),
  QVector4D(85),

  QQuaternion(86),

  VoidStar(128),
  Long(129),
  Short(130),
  Char(131),
  ULong(132),

  UShort(133),
  UChar(134),
  Float(135),
  QObjectStar(136),
  QWidgetStar(137),

  QVariant(138),

  User(256),
  UserType(127),
  LastType(-1);

  val serializableName
    get() = if (name.startsWith("Q")) {
      name
    } else {
      name.toLowerCase(Locale.ENGLISH)
    }

  companion object {
    private val byId = Type.values().associateBy(Type::id)
    fun of(type: kotlin.Int) = byId[type]
  }
}
