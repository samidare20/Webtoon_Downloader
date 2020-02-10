package com.example.webtoon_downloader

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.webtoon_tiles.view.*

class WebtoonTiles @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.webtoon_tiles, this, true)
    }

    fun setData(title: String, link: String, comic: String,mbookmark:Boolean) {
        titlename.text = title
        bookmark.isSelected=mbookmark
        try {
            Glide.with(context).load(link).into(thumbnail)
        } catch (e: Exception) {
            Log.d("mydebug", e.toString())
            Log.d("mydebug", thumbnail.toString())
        }
        this.setOnClickListener {
            val intent = Intent(context, Episode::class.java)
            intent.putExtra("link", comic)
            intent.putExtra("title", title)
            intent.putExtra("thumbnail", link)
            startActivity(context, intent, null)
        }
        bookmark.setOnClickListener {
            Thread(Runnable {
                if(bookmark.isSelected)
                bookmark.isSelected = !bookmark.isSelected
                val db = Room_Database.getInstance(context)
                val data = db.Room_DAO().selectTitle("")
                data.bookmark = bookmark.isSelected
            }).start()
        }
    }
}