package com.myapp.webtoon_downloader

import android.content.Context
import android.util.Log

class itemList(args:String){
    //표지 이미지, 웹툰 이름 저장하는 클래스
    var title = ""//이름
    var imagesrc = ""//표지 이미지 링크
    var day = ""//요일
    var comiclist = ""//만화 회차 링크
    init {
        var html = args

        html = html.substring(html.indexOf("href") + 7)
        comiclist = "https://comic.naver.com/" + html.substring(0, html.indexOf("\""))
        comiclist = comiclist.substring(0, comiclist.indexOf("amp") - 1)

        html = html.substring(html.indexOf("src=\"") + 5) //표지 찾아냄
        Log.d("mdg",html)
            imagesrc = html.substring(0, html.indexOf("\" width"))

        html = html.substring(html.indexOf("title=") + 7)//제목 알아냄
        title = html.substring(0, html.indexOf("\""))

        val index = html.indexOf("weekday") + 7//요일 알아냄
        day = html.substring(index + 1, index + 4)
        if(title=="이것도 친구라고")
            Log.d("hello",imagesrc)

    }
}

class linkControl {
    fun sethtml(context: Context, text: String) {
        val a = text.split("<li>") //html을 <li>구분해서 분할(각 만화로 나누어짐)
        var firstCheck = true

        for (i in a) {
            if (firstCheck) {
                firstCheck = false
                continue
            }
            val itemlist = itemList(i)//나눈 만화를 각각 itemlist에 넣어 저장
            val thread = Thread(Runnable {
                val db = Room_Database.getInstance(context)
                if (db.Room_DAO().selectTitle(itemlist.title) == null) {
                    val data = Room_Data()
                    data.title = itemlist.title
                    data.ThumbnailLink = itemlist.imagesrc
                    data.EpisodeLink = itemlist.comiclist
                    data.day = itemlist.day
                    data.update = " "
                    data.bookmark = false
                    db.Room_DAO().insert(data)
                }
            })
            thread.start()
            thread.join()

        }
    }
}
