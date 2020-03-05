package com.myapp.bookmark

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.myapp.webtoon_downloader.*
import kotlinx.android.synthetic.main.bookmark_tiles.view.thumbnail
import kotlinx.android.synthetic.main.bookmark_tiles.view.titlename
import kotlinx.android.synthetic.main.downloader_webtoon_tiles.view.*
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
            val builder=AlertDialog.Builder(context)
            builder.setTitle("북마크에서 제거하시겠습니까?")
            builder.setPositiveButton("예") { DialogInterface, i: Int ->
                lateinit var db:Room_Database
                lateinit var data:Room_Data
                var id=-1
                CoroutineScope(Dispatchers.Default).launch {
                    db = Room_Database.getInstance(maincontext)
                    data = db.Room_DAO().selectTitle(title)
                    data.bookmark = false
                    id=data.id
                    db.Room_DAO().update(data)
                }
                val tile=findViewById<WebtoonTiles>(id)
                try {
                    tile.bookmark.isSelected = false
                }
                catch (E:java.lang.Exception)
                {
                    print(E)
                }
            }
            builder.setNeutralButton("아니오",null)
            builder.create().show()
            true
        }
    }
}