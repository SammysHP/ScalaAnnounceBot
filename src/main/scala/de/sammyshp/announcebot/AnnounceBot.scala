package de.sammyshp.announcebot

import org.jibble.pircbot._

import scala.util.control.Breaks._

class AnnounceBot(config: Configuration) extends PircBot {
  var wasConnected = false
  val stats = new RuntimeStats()

  def start() {
    setName(config.nick)
    setEncoding("UTF-8")
    setAutoNickChange(true)

    onDisconnect()

    val urlFilter = (config.bitlyUser, config.bitlyKey) match {
      case (Some(user), Some(key)) => {
        val urlShortener = new BitlyUrlShortener(user, key)
        (s: String) => { urlShortener.shorten(s) }
      }
      case _ => (s: String) => s
    }

    new PeriodicRssFetcher(config.url, config.pollInterval, 
      (s: String) => { stats.incAnnounces(); config.channels.map(sendMessage(_, s)) }, urlFilter) start
  }

  override def onConnect() {
    wasConnected = true

    config.identify match {
      case None => ()
      case Some(s) => identify(s)
    }

    Log.d("Join channels " + config.channels.mkString(", "))
    config.channels.map(joinChannel)
  }

  override def onDisconnect() {
    if (wasConnected)
      Log.w("Lost connection to server")

    Log.d("Connect to server")

    try {
      (config.host, config.port) match {
        case (server: String, None) => connect(server)
        case (server: String, Some(port)) => connect(server, port)
        case _ => throw new ConfigurationException("Malformed configuration: server")
      }
    } catch {
      case e => {
        Log.e("Cannot connect to server:\n  " + e)
        Log.d("Try reconnect in 1 minute.")
        Thread sleep 60 * 1000
        wasConnected = false
        onDisconnect()
      }
    }
  }

  override def onPrivateMessage(sender: String, login: String, hostname: String, message: String) {
    Log.d("Receive private message:    " + sender + ": " + message)

    val command = message.split(' ')(0)

    command toLowerCase match {
      case "!help" => {
        sendMessage(sender, "!help      Zeige diese Hilfe an")
        sendMessage(sender, "!source    Wo gibt es den Source?")
        sendMessage(sender, "!author    Kontakt zum Entwickler des Bots")
        sendMessage(sender, "!stats     Statistiken der aktuellen AnnounceBot-Session")
        sendMessage(sender, "!about     Informationen zum Bot")
      }
      case "!source" => sendMessage(sender, "http://github.com/SammysHP/ScalaAnnounceBot")
      case "!author" => sendMessage(sender, "Sven Karsten Greiner (SammysHP), sven@sammyshp.de")
      case "!stats" => {
        sendMessage(sender, "Gestartet: " + stats.startedString)
        sendMessage(sender, "Laufzeit: " + stats.runtimeString)
        sendMessage(sender, "Angekündigte Beiträge: " + stats.announces)
      }
      case "!about" => {
        sendMessage(sender, "Lizenz: GPL")
        sendMessage(sender, "Unter Verwendung von PircBot (http://jibble.org/pircbot.php, GPL) und geschrieben in Scala.")
        sendMessage(sender, "Siehe auch !author und !source")
      }
      case _ => sendMessage(sender, "Befehl nicht erkannt, probiere \"!help\"")
    }
  }
}

object AnnounceBot {
  def main(args: Array[String]) {
    Log.d("Start bot")
    Log.d("Interrupt (Ctrl+C) to stop")

    try {
      val bot = new AnnounceBot(Configuration("config.xml"))
      bot start
    } catch {
      case e: ConfigurationException => {
        Log.e("Error in configuration, exit.")
        Log.e(e.toString)
        System.exit(1)
      }
    }
  }
}
