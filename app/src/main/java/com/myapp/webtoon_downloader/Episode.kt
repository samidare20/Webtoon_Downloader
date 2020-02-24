package com.myapp.webtoon_downloader

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.myapp.bookmark.Bookmark
import com.myapp.webtoon_viewer.ViewerActivity
import kotlinx.android.synthetic.main.downloader_episode_main_content.*
import kotlinx.coroutines.*
import org.jsoup.Jsoup

class Episode : AppCompatActivity() {
    val mcontext = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.downloader_episode_main)
        val intent = intent.extras!!
        Glide.with(this).load(intent.getString("thumbnail")).into(episode_thumbnail)
        setDrawer()
        init(intent)
    }

    private fun init(intent: Bundle) {
        CoroutineScope(Dispatchers.Default).launch {
            val link = intent.getString("link") + "&page="
            var prev = ""
            var page = 1
            while (true) {
                lateinit var doc: org.jsoup.nodes.Document
                runBlocking {
                    val job = CoroutineScope(Dispatchers.IO).launch {
                        doc = Jsoup.connect(link + page.toString()).get()
                    }
                    job.join()
                }

                val elements = doc.select("tbody").toString()
                val html = elements.split("<tr>")
                var end = false
                var first = false
                var href: String
                var title: String
                for (i in html) {

                    href = "https://comic.naver.com"
                    try {
                        val amp = i.indexOf("&amp")
                        href += i.substring(i.indexOf("href") + 6, amp) + "&" + i.substring(amp + 5, i.indexOf("weekday") - 5)
                        title = i.substring(i.indexOf("title=") + 7)
                        title = title.substring(0, title.indexOf("\""))
                        if (!first) {
                            if (prev == title) {
                                end = true
                                break
                            }
                            prev = title
                            first = true
                        }
                    } catch (e: Exception) {
                        continue
                    }
                    runBlocking {
                        val job = CoroutineScope(Dispatchers.Main).launch {
                            val episode = episodeTiles(mcontext)
                            episode.setTile(title, href, intent.getString("title")!!)
                            episode_area.addView(episode)
                        }
                        job.join()
                    }

                }
                if (end)
                    break
                page++
            }
        }
    }

    fun setDrawer() {
        Thread(Runnable {
            val navi = findViewById<NavigationView>(R.id.navi)
            navi.setNavigationItemSelectedListener { item: MenuItem ->
                val drawer = findViewById<DrawerLayout>(R.id.drawer)
                drawer.closeDrawer(GravityCompat.START)
                lateinit var intent: Intent
                if (item.itemId == R.id.viewer)
                    intent = Intent(this, ViewerActivity::class.java)
                else if (item.itemId == R.id.bookmarklist)
                    intent = Intent(this, Bookmark::class.java)
                startActivity(intent)
                true
            }
        }).start()
    }
}
