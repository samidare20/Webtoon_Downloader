package com.myapp.webtoon_viewer

import androidx.room.Ignore

class imageItems(path: String, isImage: Boolean) : Comparable<imageItems> {
    var path = ""
    var number = -1

    init {
        this.path = path
        if (isImage) {
            try {
                number = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf(".")).toInt()
            } catch (e: Exception) {
            }
        }
    }

    override fun compareTo(other: imageItems): Int {
        if (number != -1 && other.number != -1)
            return number.compareTo(other.number)
        else {
            try {
                return this.path.substring(this.path.indexOf("$$=") + 3).toInt().compareTo(other.path.substring(other.path.indexOf("$$=") + 3).toInt())
            } catch (e:Exception) {
                return path.toLowerCase().compareTo(other.path.toLowerCase())
            }
        }
    }

}