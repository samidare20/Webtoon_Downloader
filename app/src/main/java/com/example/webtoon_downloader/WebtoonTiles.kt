package com.example.webtoon_downloader

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.webtoon_tiles.view.*
import org.w3c.dom.Text

class WebtoonTiles @JvmOverloads constructor(
        context:Context,
        attrs:AttributeSet?=null,
        defStyleAttr:Int=0
) : LinearLayout(context,attrs,defStyleAttr){
    init{
        LayoutInflater.from(context).inflate(R.layout.webtoon_tiles,this,true)

    }

}