package com.example.webtoon_downloader

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.episode_main.*
import kotlinx.android.synthetic.main.webtoon_tiles.view.*

class Episode : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.episode_main)

        var intent=getIntent().extras!!
        var comicLink=intent.getString("link")
        var title=intent.getString("title")
        var thumbnail=intent.getString("thumbnail")
        Log.d("mydebug",comicLink)
        Log.d("mydebug",title)
        Log.d("mydebug",thumbnail)
        Glide.with(this).load(thumbnail).into(episode_thumbnail)
        episode_title.setText(title)

    }
}