package com.myapp.webtoon_downloader

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class Download(val link: String, val title: String, val series: String, val context: Context) {
    private var path = context.filesDir.toString()

    private val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    lateinit var html: List<String>
    var id = 0

    init {
        id = (System.currentTimeMillis() / 1000).toInt()
        Log.d("mdg", "class check")
        Thread(Runnable {
            makefolder()
            val doc = Jsoup.connect(link).get()
            val element = doc.select("div.wt_viewer")
            html = element.toString().split(">")
            download()
        }).start()
    }


    private fun makefolder() {
        var folder = File(path)

        path += "/" + series
        folder = File(path)
        if (!folder.exists())
            folder.mkdir()

        path += "/" + title

        path += "$$" + link.substring(link.indexOf("&no=") + 3)
        folder = File(path)
        if (!folder.exists())
            folder.mkdir()
    }

    fun download() {

        val nowstring = ArrayList<String>()
        var index = 1
        for (i in html) {
            if (i.indexOf("title") != -1)
                nowstring.add(i.substring(i.indexOf("src") + 5, i.indexOf("title") - 2))
        }
        val max = nowstring.size + 1
        val notiBuilder = NotificationCompat.Builder(context, "1004")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("$series - $title ")
                .setProgress(max, 0, false)
                .setOngoing(true)
                .setStyle(NotificationCompat.BigTextStyle())

        notificationManager.notify(id, notiBuilder.build())



        for (i in nowstring) {

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
                notificationManager.cancel(id)
                notiBuilder.setContentText("다운로드가 일시중지됨")
                notiBuilder.setOngoing(false)
                notiBuilder.setProgress(0, 1, true)
                notificationManager.notify(id, notiBuilder.build())


                var pendingIntent: PendingIntent
                val mintent = Intent(context, notiRestartEvent::class.java)
                mintent.putExtra("index", index - 1)
                mintent.putExtra("id", id)
                mintent.putExtra("title", title)
                mintent.putExtra("path", path)
                mintent.putExtra("link", link)
                mintent.putExtra("series", series)

                pendingIntent = PendingIntent.getBroadcast(context, 1, mintent, PendingIntent.FLAG_UPDATE_CURRENT)
                notiBuilder.addAction(R.drawable.ic_launcher_background, "재시작", pendingIntent)

                pendingIntent = PendingIntent.getBroadcast(context, 1, mintent, PendingIntent.FLAG_UPDATE_CURRENT)
                notiBuilder.addAction(R.drawable.ic_launcher_background, "취소", pendingIntent)
                notificationManager.notify(id, notiBuilder.build())
                return
            }
            updateProgress(index, notiBuilder, max)
        }
        notificationManager.cancel(id)
        notiBuilder.setContentTitle("$series - $title")
        notiBuilder.setContentText(" 다운로드 완료")
        notiBuilder.setOngoing(false)
        notiBuilder.setProgress(0, 0, false)

        notificationManager.notify(id, notiBuilder.build())

    }


    fun updateProgress(progress: Int, notification: NotificationCompat.Builder, size: Int) {
        notification.setProgress(size, progress, false)
        notification.setContentText("$progress/$size")
        notificationManager.notify(id, notification.build())


    }
}

/*
todo  다운로드 실패시 재시도
1)다운로드를 일시정지함
2)크롬과 같이 <재시작> 과 <취소> 선택지를 제시 O
3)재시작을 누르면 실패한 번호의 이미지 제거 후 그곳부터 다운로드 시작
4)취소를 누르면 폴더째로 삭제 O
*/
