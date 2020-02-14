package com.myapp.webtoon_downloader

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.downloader_episode_tiles.view.*

class episodeTiles constructor(
        context: Context
) : LinearLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.downloader_episode_tiles, this, true)
    }

    fun setTile(title: String, href: String, series: String) {
        episode_title.text = title

        this.setOnClickListener {
            Download(href, title, series, context)
        }
    }

}

