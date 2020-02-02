package com.example.webtoon_downloader

import android.util.Log
import kotlin.TODO as TODO1

class itemList {
    //표지 이미지, 웹툰 이름 저장하는 클래스
    var title = ""//이름
    var imagesrc = ""//표지 이미지 링크
    var day = ""//요일
    var comiclist = ""//만화 회차 링크

    fun getElement(args: String) {
        var html = args
        title = html.substring(html.indexOf("title=") + 7)//제목 알아냄
        title = title.substring(0, title.indexOf("\""))

        var a = html.substring(html.indexOf("src=\"") + 5) //표지 찾아냄
        imagesrc = a.substring(0, a.indexOf(".jpg") + 4)

        html = html.substring(html.indexOf("href") + 7)
        comiclist = html
        comiclist = "https://comic.naver.com/" + comiclist.substring(0, comiclist.indexOf("\""))
        comiclist = comiclist.substring(0, comiclist.indexOf("amp") - 1)


        var index = html.indexOf("weekday") + 7//요일 알아냄
        day = html.substring(index + 1, index + 4)
    }
}
class updateCheck{
    var title=""
    var link=""
    fun updateCheck(args: String){

    }

}
class linkControl {

    var ElementList = ArrayList<itemList>()
    var Updatelist=ArrayList<updateCheck>()

    //TODO update 여부를 판단하고 그것에 필요한  결과물
    //
    //1. 각 업데이트된 것들의 제목
    //
    //2. 각 업데이트된 것들의 링크

    fun findUpdate(text: String){
        Log.d("mydebug","func check")
        val a=text.split("<li>")
        var firstcheck=true;
        for(i in a){
            if(firstcheck){
                firstcheck=false
                continue
            }
            Log.d("mydebug",i)
            var update=updateCheck()
            update.updateCheck(i)
            Updatelist.add(update)
        }
    }

    fun sethtml(text: String): ArrayList<itemList> {
        var a = text.split("<li>") //html을 <li>구분해서 분할(각 만화로 나누어짐)
        var firstCheck = true
        for (i in a) {
            if (firstCheck) {
                firstCheck=false
                continue
            }
            var itemlist = itemList()//나눈 만화를 각각 itemlist에 넣어 저장
            itemlist.getElement(i)
            ElementList.add(itemlist)
        }
        return ElementList
    }
}
