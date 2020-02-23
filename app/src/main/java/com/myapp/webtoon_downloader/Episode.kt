package com.myapp.webtoon_downloader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.downloader_episode_main_content.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup

class Episode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.downloader_episode_main)
        val mcontext = this
        val intent = intent.extras!!
        Glide.with(this).load(intent.getString("thumbnail")).into(episode_thumbnail)
        comic_title.text = intent.getString("title")

        CoroutineScope(Dispatchers.Main).launch {
            val link = intent.getString("link") + "&page="
            var prev = ""
            var page = 1
            while (true) {
                lateinit var doc:org.jsoup.nodes.Document
                runBlocking {
                    val job=CoroutineScope(Dispatchers.IO).launch {
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
                    val episode = episodeTiles(mcontext)
                    episode.setTile(title, href, intent.getString("title")!!)
                    episode_area.addView(episode)
                }
                if (end)
                    break
                page++
            }
        }
    }


}