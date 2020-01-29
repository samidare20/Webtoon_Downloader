package com.example.webtoon_downloader

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
    private val intent = Intent(context, MainActivity::class.java)
    private val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

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
        if (folder.exists() == false)
            folder.mkdir()

        path += "/" + series
        folder = File(path)
        if (folder.exists() == false)
            folder.mkdir()

        path += "/" + title
        folder = File(path)
        if (folder.exists() == false)
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
        var notiBuilder = NotificationCompat.Builder(context, "1004")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("$title 다운로드 중...")
                .setProgress(max, 0, false)
                .setOngoing(true)
        notificationManager.notify(1, notiBuilder.build())

        for (i in nowstring) {
            Log.d("mydebug", i)

            val filename = index.toString()
            index++

            val filepath = path + "/" + filename + ".jpg"
            val file = File(filepath)
            if (file.exists() == true) {
                Log.d("mydebug", "exist file")
                continue
            }
            Log.d("mydebug", filepath)

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
        notificationManager.cancel(1)
        notiBuilder.setContentTitle(title)
        notiBuilder.setContentText(" 다운로드 완료")
        notiBuilder.setOngoing(false)
        notiBuilder.setProgress(0, 0, false)
        notificationManager.notify(1, notiBuilder.build())
    }


    fun updateProgress(progress: Int, notification: NotificationCompat.Builder, size: Int) {
        notification.setProgress(size, progress, false)
        notification.setContentText("$progress/$size")
        notificationManager.notify(1, notification.build())


    }
}