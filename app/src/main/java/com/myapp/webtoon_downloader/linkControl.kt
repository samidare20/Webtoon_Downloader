package com.myapp.webtoon_downloader

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.LinearLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class itemList(args: String) {
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
        imagesrc = html.substring(0, html.indexOf("\" width"))

        html = html.substring(html.indexOf("title=") + 7)//제목 알아냄
        title = html.substring(0, html.indexOf("\""))

        val index = html.indexOf("weekday") + 7//요일 알아냄
        day = html.substring(index + 1, index + 4)

        //Log.d("yee",title)
    }
}

class linkControl {
    fun sethtml(context: Context) {

        // val a = text.split("<li>") //html을 <li>구분해서 분할(각 만화로 나누어짐)
        var a = emptyList<String>()
        var thread = Thread(Runnable {
            val doc = Jsoup.connect("https://comic.naver.com/webtoon/weekday.nhn").get()
            val text = doc.select("div.col_inner").toString()
            a = text.split("<li>")
        })
        thread.start()
        thread.join()

        var firstCheck = true
        for (i in a) {
            if (firstCheck) {
                firstCheck = false
                continue
            }
            val itemlist = itemList(i)//나눈 만화를 각각 itemlist에 넣어 저장
            Log.d("mydebug", "${itemlist.title} ${itemlist.day}")
            thread = Thread(Runnable {
                val db = Room_Database.getInstance(context)
                if (db.Room_DAO().selectTitle(itemlist.title) == null) {
                    Log.d("yee", itemlist.title)
                    val data = Room_Data()
                    data.title = itemlist.title
                    data.ThumbnailLink = itemlist.imagesrc
                    data.EpisodeLink = itemlist.comiclist
                    data.day = itemlist.day
                    data.update = ""
                    data.bookmark = false
                    db.Room_DAO().insert(data)
                    CoroutineScope(Dispatchers.Main).launch {
                        settab(context, itemlist)
                    }
                }
            })
            thread.start()
            thread.join()
        }
    }
}

fun settab(context: Context, itemlist: itemList) {
    val id = context.resources.getIdentifier(itemlist.day + "tabContent", "id", context.packageName)
    Log.d("mdg", "${itemlist.title} ${itemlist.day} ")
    val layout = (context as Activity).findViewById<LinearLayout>(id)
    val tab = WebtoonTiles(context)

    tab.setData(itemlist.title, itemlist.imagesrc, itemlist.comiclist, false)
    layout.addView(tab, layout.childCount)
}
