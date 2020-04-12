package com.myapp.webtoon_downloader

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import java.io.File

class notiCancelEvent : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        Log.d("mdg", "received~~~")
        val mintent = intent.extras!!
        val id = mintent.getInt("id")
        val title = mintent.getString("title")
        val path = mintent.getString("path")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notiBuilder = NotificationCompat.Builder(context, "1004")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("$title 의 다운로드가 취소되었습니다")
        notificationManager.notify(id, notiBuilder.build())
        val file = File(path)
        val filelist = file.listFiles()
        for (i in filelist) {
            i.delete()
        }

        file.delete()
    }
}
