package de.sammyshp.announcebot

import java.net.{URLEncoder, URL}
import java.io.{BufferedReader, InputStreamReader}

class BitlyUrlShortener(user: String, key: String) {
    def shorten(url: String) = {
        val apiCall = "http://api.bitly.com/v3/shorten?login=%s&apiKey=%s&longUrl=%s&format=txt&domain=j.mp".format(user, key, URLEncoder.encode(url, "UTF-8"))
        
        httpGetLine(apiCall) match {
            case Some(s) => {
              Log.w("Shortened " + url + " to " + s)
              s
            }
            case None => {
              Log.w("Cannot shorten url")
              url
            }
        }
    }
    
    private def httpGetLine(url: String) = {
        try {
            val in = new BufferedReader(new InputStreamReader(new URL(url).openStream()))
            
            val result = in.readLine() match {
                case null => None
                case s => Some(s)
            }
            
            in.close()
            
            result
        } catch {
            case e => None
        }
    }
}
