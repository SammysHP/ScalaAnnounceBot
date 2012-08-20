package de.sammyshp.announcebot

import java.text.SimpleDateFormat
import java.util.Date

object Log {
  val LEVEL_ERROR = 1
  val LEVEL_WARNING = 2
  val LEVEL_DEBUG = 4

  val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  def e(msg: String) {
    log(LEVEL_ERROR, msg)
  }

  def w(msg: String) {
    log(LEVEL_WARNING, msg)
  }

  def d(msg: String) {
    log(LEVEL_DEBUG, msg)
  }

  private def log(level: Int, msg: String) {
    println("%-10s%-22s%s".format(
      level match {
        case LEVEL_ERROR =>   "ERROR"
        case LEVEL_WARNING => "WARNING"
        case LEVEL_DEBUG =>   "DEBUG"
      },
      dateFormat.format(new Date()),
      msg))
  }
}
