package com.example.webtoon_downloader

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.episode_main.*
import org.jsoup.Jsoup
import java.lang.Exception

class Episode : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.episode_main)

        val mcontext=this
        val intent=getIntent().extras!!

        Glide.with(this).load(intent.getString("thumbnail")).into(episode_thumbnail)
        comic_title.setText(intent.getString("title"))

        var handler= @SuppressLint("HandlerLeak")
        object:Handler(){
            override fun handleMessage(msg: Message) {
                var episode=episodeTiles(mcontext,null,0)
                var message=msg.obj.toString()
                val title=message.substring(message.indexOf("-")+1)
                message=message.substring(0,message.indexOf("-"))

                episode.setTile(title,message)
                episode_area.addView(episode)
            }
        }
        Thread(Runnable {
            var doc=Jsoup.connect(intent.getString("link")).get()
            var elements=doc.select("tbody").toString()
            var html=elements.split("<tr>")

            var href:String
            var title:String
            for (i in html)
            {

                href="https://comic.naver.com"
                try {
                    href += i.substring(i.indexOf("href") + 6, i.indexOf("weekday") - 5)
                    title=i.substring(i.indexOf("title=")+7)
                    title=title.substring(0,title.indexOf("\""))
                }
                catch (e:Exception){
                    continue
                }

                val message=Message.obtain()
                message.obj=href+"-"+title
                handler.sendMessage(message)
            }
        }).start()


    }
}