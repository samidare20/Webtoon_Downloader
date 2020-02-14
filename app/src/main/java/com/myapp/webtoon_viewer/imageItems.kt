package com.myapp.webtoon_viewer

import android.util.Log

class imageItems(path:String,isImage:Boolean) :Comparable<imageItems>{
    var path=""
    var number=-1
    init {
        this.path=path
        if(isImage) {
            number = path.substring(path.lastIndexOf("/")+1, path.lastIndexOf(".")).toInt()
            Log.d("hello", number.toString())
        }
    }

    override fun compareTo(other: imageItems): Int {
        if (number==-1&&other.number==-1)
            return path.toLowerCase().compareTo(other.path.toLowerCase())
        else
            return number.compareTo(other.number)
    }

}