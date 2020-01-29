package com.example.webtoon_downloader

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.episode_tiles.view.*

class episodeTiles @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.episode_tiles, this, true)
    }

    fun setTile(title: String, href: String, series: String) {
        episode_title.text = title

        this.setOnClickListener {
            Download(href, title, series, context)
        }
    }

}

