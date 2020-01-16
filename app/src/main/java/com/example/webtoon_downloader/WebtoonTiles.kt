package com.example.webtoon_downloader

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.webtoon_tiles.view.*

class WebtoonTiles @JvmOverloads constructor(
        context:Context,
        attrs:AttributeSet?=null,
        defStyleAttr:Int=0
) : LinearLayout(context,attrs,defStyleAttr){
    var URL=""
    init{
        LayoutInflater.from(context).inflate(R.layout.webtoon_tiles,this,true)
        this.setOnClickListener({
            Log.d("mydebug","check")
        })
    }
    fun setThumbnail(title:String,link:String){
        Glide.with(context).load(link).into(thumbnail)
        titlename.setText(title)
    }

}