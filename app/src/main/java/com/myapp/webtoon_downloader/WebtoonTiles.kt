package com.myapp.webtoon_downloader

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.downloader_webtoon_tiles.view.*

class WebtoonTiles constructor(
        context: Context
) : LinearLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.downloader_webtoon_tiles, this, true)
    }

    fun setData(title: String, thumblink: String, comic: String, mbookmark: Boolean) {
        titlename.text = title
        bookmark.isSelected = mbookmark
        val db = Room_Database.getInstance(context)

        try {
            Glide.with(context).load(thumblink).into(thumbnail)
        } catch (e: Exception) {
            //Log.d("mydebug", e.toString())
            Log.d("mydebug", "glide error")
            Log.d("mydebug", thumbnail.toString())
        }
        this.setOnClickListener {
            val intent = Intent(context, Episode::class.java)
            intent.putExtra("link", comic)
            intent.putExtra("title", title)
            startActivity(context, intent, null)
        }

        bookmark.setOnClickListener {
            Thread(Runnable {
                bookmark.isSelected = !bookmark.isSelected
                val data = db.Room_DAO().selectTitle(title)
                data.bookmark = bookmark.isSelected
                db.Room_DAO().update(data)
            }).start()
        }
    }
}