package de.sammyshp.announcebot

import java.text.SimpleDateFormat
import java.util.Date

class RuntimeStats {
  val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  val started = System.currentTimeMillis()
  private var announceCount = 0

  def startedString = {
    dateFormat.format(started)
  }

  def runtime = {
    System.currentTimeMillis() - started
  }

  def runtimeString = {
    val rt = runtime / 1000
    val s = rt % 60
    val m = (rt % 3600) / 60
    val h = (rt % 86400) / 3600
    val d = rt / 86400
    "%dd %dh %dm %ds".format(d, h, m, s)
  }

  def announces = {
    announceCount
  }

  def incAnnounces() {
    announceCount += 1
  }
}
