package com.myapp.webtoon_viewer

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.myapp.webtoon_downloader.R
import com.myapp.webtoon_viewer.databinding.ViewerFileTilesBinding

class fileTiles constructor(
        context: Context
) : LinearLayout(context) {
    var path = ""
    private val binding: ViewerFileTilesBinding

    init {
        binding = ViewerFileTilesBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setData(root: String) {
        path = root
        binding.filename.text = root.substring(root.lastIndexOf("/") + 1, root.length)
    }
}