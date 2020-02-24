package com.myapp.webtoon_viewer

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
        return if (number == -1 && other.number == -1)
            path.toLowerCase().compareTo(other.path.toLowerCase())
        else
            number.compareTo(other.number)
    }

}