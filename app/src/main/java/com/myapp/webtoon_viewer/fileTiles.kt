package com.myapp.webtoon_viewer

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.myapp.webtoon_downloader.R
import kotlinx.android.synthetic.main.viewer_file_tiles.view.*

class fileTiles constructor(
        context: Context
):LinearLayout(context){
    var path=""
    init {
        LayoutInflater.from(context).inflate(R.layout.viewer_file_tiles,this,true)
    }
    fun setData(root:String){
        path=root
        filename.text=root.substring(root.lastIndexOf("/")+1,root.length)
    }
}