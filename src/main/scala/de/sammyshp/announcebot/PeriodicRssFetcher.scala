package de.sammyshp.announcebot

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PeriodicRssFetcher(url: String, pollInterval: Long, callback: String => Unit, urlFilter: String => String) extends Thread {
  val dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
  var lastMessageTime: Long = 0;

  override def run() {
    while (true) {
      Log.d("Fetch RSS: " + url)

      try {
        val xml = scala.xml.XML.load(new java.net.URL(url))

        val posts = (xml \\ "item")
          .map(x => (x \ "title" text, x \ "link" text, dateFormat.parse(x \ "pubDate" text).getTime))
          .filter(x => x._3 > lastMessageTime)
          .groupBy(x => x._1)
          .map(x => { val y = x._2(0); (y._1, y._2, y._3, x._2.size) })
          .toList
          .sortBy(x => x._3)

        Log.d(
          posts.size match {
            case 0 => "No new posts"
            case 1 => "1 new post"
            case i => "" + i + " new posts"
          })

        if (lastMessageTime > 0) {
          posts.foreach(x => callback(
              (if (x._4 == 1)
                "Neuer Beitrag"
              else
                ("" + x._4 + " neue Beiträge"))
              + " im Thema \""
              + x._1
              + "\": "
              + urlFilter(x._2)))
        } else {
          Log.d("First fetch, no announce")
        }

        if (posts.size > 0)
          lastMessageTime = posts.last._3
      } catch {
        case e => Log.e("Cannot fetch or post RSS: " + e)
      }

      Thread sleep pollInterval
    }
  }
}
