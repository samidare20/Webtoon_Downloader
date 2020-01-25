package com.example.webtoon_downloader

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.episode_tiles.view.*

class episodeTiles @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet?=null,
        defStyleAttr:Int=0
):LinearLayout(context,attrs,defStyleAttr){
    init{
        LayoutInflater.from(context).inflate(R.layout.episode_tiles,this,true)
    }
    fun setTile(title : String, href:String,series:String){
        episode_title.setText(title)

        this.setOnClickListener {
            Thread(Runnable {
                Download(href,title,series)
            }).start()

        }
    }
}

