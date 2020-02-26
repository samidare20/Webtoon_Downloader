package com.myapp.webtoon_downloader

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.downloader_webtoon_tiles.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WebtoonTiles constructor(
        context: Context
) : LinearLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.downloader_webtoon_tiles, this, true)
    }
    var title=""
    fun setData(title: String, thumblink: String, comic: String, mbookmark: Boolean) {
        titlename.text = title
        bookmark.isSelected = mbookmark
        val db = Room_Database.getInstance(context)
        this.title=title
        try {
            Glide.with(context).load(thumblink).into(thumbnail)
        } catch (e: Exception) {
            print("Glide error : $e")
        }
        this.setOnClickListener {
            val intent = Intent(context, Episode::class.java)
            intent.putExtra("link", comic)
            intent.putExtra("title", title)
            intent.putExtra("thumbnail",thumblink)
            startActivity(context, intent, null)
        }

        bookmark.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                bookmark.isSelected = !bookmark.isSelected
                val data = db.Room_DAO().selectTitle(title)
                data.bookmark = bookmark.isSelected
                db.Room_DAO().update(data)
            }
        }
    }
}