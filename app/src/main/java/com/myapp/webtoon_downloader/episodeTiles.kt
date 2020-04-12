package com.myapp.webtoon_downloader

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.downloader_episode_tiles.view.*

class episodeTiles constructor(
        context: Context
) : LinearLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.downloader_episode_tiles, this, true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setTile(title: String, href: String, series: String) {
        episode_title.text = title

        this.setOnClickListener {
            Download(href, title, series, context)
        }
    }

}

