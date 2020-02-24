package com.myapp.webtoon_downloader

import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Environment
import android.util.Log
import androidx.core.app.NotificationCompat
import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class Download(link: String, title: String, series: String, context: Context) {
    private var path = Environment.getExternalStorageDirectory().absolutePath.toString() + "/download/"
    private val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    val id=(System.currentTimeMillis()/1000).toInt()

    init {
        object : Thread() {
            override fun run() {
                super.run()
                makefolder(title, series)
                val doc = Jsoup.connect(link).get()
                val element = doc.select("div.wt_viewer")
                val html = element.toString().split(">")
                download(html, context, title)
            }
        }.start()
    }


    private fun makefolder(title: String, series: String) {
        path += "Webtoon"
        var folder = File(path)
        if (!folder.exists())
            folder.mkdir()

        path += "/" + series
        folder = File(path)
        if (!folder.exists())
            folder.mkdir()

        path += "/" + title
        folder = File(path)
        if (!folder.exists())
            folder.mkdir()
    }

    private fun download(html: List<String>, context: Context, title: String) {

        var nowstring = ArrayList<String>()
        var index = 1
        for (i in html) {
            if (i.indexOf("title") != -1)
                nowstring.add(i.substring(i.indexOf("src") + 5, i.indexOf("title") - 2))
        }
        val max = nowstring.size + 1
        val notiBuilder = NotificationCompat.Builder(context, "1004")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("$title 다운로드 중...")
                .setProgress(max, 0, false)
                .setOngoing(true)

        notificationManager.notify(id, notiBuilder.build())

        for (i in nowstring) {
            Log.d("mydebug", i)

            val filename = index.toString()
            index++

            val filepath = "$path/$filename.jpg"
            val file = File(filepath)
            if (file.exists()) {
                continue
            }

            try {
                val imageurl = URL(i)
                val conn = imageurl.openConnection() as HttpURLConnection
                conn.setRequestProperty("User-Agent", "Mozilla/4.0")
                var input: InputStream
                if (conn.responseCode == HttpURLConnection.HTTP_OK)
                    input = conn.inputStream
                else
                    input = conn.errorStream  //웹페이지 연결 설정

                val file = File(filepath)
                val fos = FileOutputStream(file)
                val tmpbyte = ByteArray(conn.contentLength)
                var read: Int
                while (true) {
                    read = input.read(tmpbyte)
                    if (read < 0)
                        break
                    fos.write(tmpbyte, 0, read)
                }//파일 다운로드
                input.close()
                fos.close()
            } catch (e: java.lang.Exception) {
                Log.d("mydebug", e.toString())
            }
            updateProgress(index, notiBuilder, max)
        }
        notificationManager.cancel(id)
        notiBuilder.setContentTitle(title)
        notiBuilder.setContentText(" 다운로드 완료")
        notiBuilder.setOngoing(false)
        notiBuilder.setGroup("1")
        notiBuilder.setProgress(0, 0, false)
        notificationManager.notify(id, notiBuilder.build())
        /*notificationManager.notify(2, notiBuilder.build())
        val summaryNotification = NotificationCompat.Builder(context, "1004")
                .setContentTitle("webtoon_downloader"k)
                //set content text to support devices running API level < 24
                .setContentText("Two new messages")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                //specify which group this notification belongs to
                .setGroup("1")
                //set this notification as the summary for the group
                .setGroupSummary(true)
                .build()
        notificationManager.notify(0, summaryNotification)
        */

    }


    fun updateProgress(progress: Int, notification: NotificationCompat.Builder, size: Int) {
        notification.setProgress(size, progress, false)
        notification.setContentText("$progress/$size")
        notificationManager.notify(id, notification.build())


    }
}