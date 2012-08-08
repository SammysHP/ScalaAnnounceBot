package de.sammyshp.announcebot

object Log {
  val LEVEL_ERROR = 1
  val LEVEL_WARNING = 2
  val LEVEL_DEBUG = 4

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
    level match {
      case LEVEL_ERROR =>   println("ERROR:   " + msg)
      case LEVEL_WARNING => println("WARNING: " + msg)
      case LEVEL_DEBUG =>   println("DEBUG:   " + msg)
    }
  }
}
