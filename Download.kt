package com.example.webtoon_downloader

import android.os.Environment
import android.util.Log
import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class Download(link:String) {
    init {
        Thread()
        Log.d("mydebug", "check")
        try {
            var doc = Jsoup.connect(link).get();
            var element = doc.select("div.wt_viewer")
            var html = element.toString().split(">")
            download(html)
        } catch (ex: Exception) {
            Log.d("mydebug", ex.toString());
        }
    }

    fun download(html: List<String>) {
        var nowstring = ""
        for (i in html) {
            if (i.indexOf("title") != -1) {
                nowstring = i.substring(i.indexOf("src") + 5, i.indexOf("title")-2)
                Log.d("mydebug", nowstring)

                val filename = "test";
                var path = Environment.getExternalStorageDirectory().absolutePath.toString() + "/download"
                val filepath = path + "/" + filename + ".jpg"
                Log.d("mydebug",filepath)
                try {
                    val imageurl = URL(nowstring)
                    var conn=imageurl.openConnection() as HttpURLConnection
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0");

                    var input:InputStream
                    if(conn.responseCode==HttpURLConnection.HTTP_OK)
                        input=conn.inputStream
                    else
                        input=conn.errorStream

                    val file = File(filepath)
                    val fos = FileOutputStream(file)
                    var tmpbyte = ByteArray(conn.contentLength)
                    var read = 1
                    while (true) {
                        read = input.read(tmpbyte)
                        if(read<0)
                            break;
                        fos.write(tmpbyte, 0, read)
                    }
                    input.close()
                    fos.close()
                } catch (e: java.lang.Exception) {
                    Log.d("mydebug",e.toString())
                }
                break
            }
        }
    }
}