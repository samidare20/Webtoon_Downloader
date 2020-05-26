package com.myapp.webtoon_downloader

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import java.io.File

class notiRestartEvent : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val mintent = intent.extras!!
        val title = mintent.getString("title")!!
        val link = mintent.getString("link")!!
        val series = mintent.getString("series")!!
        val path = mintent.getString("path")!!
        val index = "${mintent.getInt("index")}.jpg"
        val id = mintent.getInt("id")
        val mfile = File(path + title + index)

        mfile.delete()

        val notificationmanager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationmanager.cancel(id)

        Download(link, title, series, context)
    }
}
