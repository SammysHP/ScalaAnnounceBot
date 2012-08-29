package de.sammyshp.announcebot

class Configuration(
    val host: String,
    val port: Option[Int],
    val channels: List[String],
    val nick: String,
    val identify: Option[String],
    val url: String,
    val pollInterval: Long,
    val bitlyUser: Option[String],
    val bitlyKey: Option[String]) {
}

object Configuration {
  def apply(configFile: String) = {
    try {
      val xml = scala.xml.XML.loadFile(configFile)

      val host = xml \ "server" text

      val port = xml \ "server" \ "@port" text match {
        case "" => None
        case i => Some(i.toInt)
      }

      val channels: List[String] = xml \\ "channel" map(_ text) toList

      val nick = xml \ "user" text

      val identify = xml \ "user" \ "@identify" text match {
        case "" => None
        case s => Some(s)
      }

      val url = xml \ "feed" text

      val pollInterval = xml \ "feed" \ "@interval" text match {
        case "" => 60000
        case i => i.toLong
      }

      val bitlyUser = xml \ "bitly" \ "@user" text match {
        case "" => None
        case s => Some(s)
      }
      val bitlyKey = xml \ "bitly" \ "@key" text match {
        case "" => None
        case s => Some(s)
      }

      new Configuration(host, port, channels, nick, identify, url, pollInterval, bitlyUser, bitlyKey)
    } catch {
      case e => throw new ConfigurationException(e.toString)
    }
  }
}

case class ConfigurationException(s: String) extends Exception(s) {}
