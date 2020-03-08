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
import kotlinx.coroutines.runBlocking

class WebtoonTiles constructor(
        context: Context
) : LinearLayout(context) {
    var title=""
    val db = Room_Database.getInstance(context)
    lateinit var data:Room_Data

    init {
        LayoutInflater.from(context).inflate(R.layout.downloader_webtoon_tiles, this, true)
    }
    fun setData(title: String, thumblink: String, comic: String, mbookmark: Boolean) {
        titlename.text = title
        bookmark.isSelected = mbookmark

        runBlocking {
            val job=CoroutineScope(Dispatchers.Default).launch {
                data=db.Room_DAO().selectTitle(title)
            }
            job.join()
        }

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
                data.bookmark = bookmark.isSelected
                db.Room_DAO().update(data)
            }
        }
    }
    fun Offbookmark(){
        bookmark.isSelected=false
        CoroutineScope(Dispatchers.Default).launch {
            data.bookmark=false
            db.Room_DAO().update(data)
        }
    }
}