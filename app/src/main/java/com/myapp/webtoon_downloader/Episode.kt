package com.myapp.webtoon_downloader

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.downloader_episode_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup

class Episode : AppCompatActivity() {
    val mcontext = this
    lateinit var mintent :Bundle

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.downloader_episode_main)
        mintent = intent.extras!!
        comic_title.text = mintent.getString("title")
        Glide.with(this).load(mintent.getString("thumbnail")).into(thumbnail)

        init()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun init() {
        CoroutineScope(Dispatchers.Default).launch {
            val link = mintent.getString("link") + "&page="
            var prev = ""
            var page = 1
            var destroy=false
            while (true) {
                lateinit var doc: org.jsoup.nodes.Document
                runBlocking {
                    val job = CoroutineScope(Dispatchers.IO).launch {
                        try {
                            doc = Jsoup.connect(link + page.toString()).get()
                        } catch (e: java.lang.Exception) {
                            destroy=true
                            return@launch
                        }
                    }
                    if (destroy)
                        return@runBlocking
                    job.join()
                }
                if(destroy) {
                    runOnUiThread {
                        val mToast = Toast.makeText(applicationContext, "인터넷이 연결되지 않았습니다.", Toast.LENGTH_LONG)
                        mToast.show()
                        val restartButton=Button(mcontext)
                        restartButton.text="재시도"
                        restartButton.setOnClickListener {
                            episode_area.removeAllViews()
                            init()
                        }
                        episode_area.addView(restartButton)
                    }
                    return@launch
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
                            episode.setTile(title, href, mintent.getString("title")!!)
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
}
