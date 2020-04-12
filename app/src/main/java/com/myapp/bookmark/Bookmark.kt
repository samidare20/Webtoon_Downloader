package com.myapp.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.myapp.webtoon_downloader.R
import com.myapp.webtoon_downloader.Room_Database
import com.myapp.webtoon_downloader.contextManager
import com.myapp.webtoon_viewer.ViewerActivity
import kotlinx.android.synthetic.main.bookmark_activity_main.*
import kotlinx.android.synthetic.main.bookmark_main_contents.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Bookmark : AppCompatActivity() {
    val mcontext=contextManager().getContext()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bookmark_activity_main)
        val tb = toolbar
        tb.title = "북마크"
        setSupportActionBar(tb)
        setField()
        setDrawer()
    }
    fun setField(){

        CoroutineScope(Dispatchers.Default).launch {
            val db=Room_Database.getInstance(mcontext)
            val list=db.Room_DAO().selectBookmark()
            val layout=bookmarkField
            for(i in list){
                val tile= BookmarkTiles(this@Bookmark)
                tile.setData(i.title,i.ThumbnailLink,i.EpisodeLink,mcontext)
                layout.addView(tile)
                tile.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
    }
    fun setDrawer() {
        val navi = navi
        navi.setNavigationItemSelectedListener { item: MenuItem ->
            val drawer = drawer
            drawer.closeDrawer(GravityCompat.START)
            if (item.itemId == R.id.downloader) {
                finish()
            } else if (item.itemId == R.id.viewer) {
                val intent = Intent(this, ViewerActivity::class.java)
                startActivity(intent)
                finish()
            }
            true
        }
    }
}