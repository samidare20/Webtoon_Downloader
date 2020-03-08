package com.myapp.bookmark

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.myapp.webtoon_downloader.*
import kotlinx.android.synthetic.main.bookmark_tiles.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkTiles constructor(
        context: Context
) : LinearLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.bookmark_tiles, this, true)
    }
    var title=""
    fun setData(title: String, thumblink: String, comic: String) {
        val maincontext=contextManager().getContext()
        titlename.text = title
        val db = Room_Database.getInstance(maincontext)
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
            val builder=AlertDialog.Builder(context)
            builder.setTitle("북마크에서 제거하시겠습니까?")
            builder.setPositiveButton("예") { DialogInterface, i: Int ->
                var id=-1
                CoroutineScope(Dispatchers.Default).launch {
                    val db = Room_Database.getInstance(maincontext)
                    val data = db.Room_DAO().selectTitle(title)
                    id=data.id
                    Log.d("mdg","bookmark : ${0x8000+id}")
                }
                //val inflater:LayoutInflater= LayoutInflater.from(main)
                //val view = inflater.inflate(R.layout.downloader_activity_main_contents, null);
                val tile=(maincontext as Activity).findViewById<WebtoonTiles>(0x8000+id)
                tile.Offbookmark()
            }
            builder.setNeutralButton("아니오",null)
            builder.create().show()
            true
        }
    }
}