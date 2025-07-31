package com.myapp.webtoon_downloader

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.myapp.webtoon_downloader.databinding.DownloaderEpisodeTilesBinding

class episodeTiles constructor(
        context: Context
) : LinearLayout(context) {
    private val binding: DownloaderEpisodeTilesBinding
    init {
        binding = DownloaderEpisodeTilesBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setTile(title: String, href: String, series: String) {
        binding.episodeTitle.text = title

        this.setOnClickListener {
            Download(href, title, series, context)
        }
    }

}

