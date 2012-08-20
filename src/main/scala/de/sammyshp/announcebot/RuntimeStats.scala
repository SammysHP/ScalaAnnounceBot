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
    val rt = runtime / 1000;
    "%dd %dh %dm %ds".format(rt / 86400, rt / 3600, rt / 60, rt)
  }

  def announces = {
    announceCount
  }

  def incAnnounces() {
    announceCount += 1
  }
}
