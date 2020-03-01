package com.myapp.bookmark

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.myapp.webtoon_downloader.Episode
import com.myapp.webtoon_downloader.R
import com.myapp.webtoon_downloader.Room_Database
import com.myapp.webtoon_downloader.contextManager
import kotlinx.android.synthetic.main.bookmark_tiles.view.*

class BookmarkTiles constructor(
        context: Context
) : LinearLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.bookmark_tiles, this, true)
    }
    var title=""
    fun setData(title: String, thumblink: String, comic: String, mbookmark: Boolean) {
        titlename.text = title
        val db = Room_Database.getInstance(contextManager().getContext())
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
        this.setOnLongClickListener {

            true
        }
    }
}