package com.example.webtoon_downloader

import android.util.Log

class itemList{//표지 이미지, 웹툰 이름 저장하는 클래스
    var title=""//이름
    var imagesrc=""//표지 이미지 링크
    var day=""//요일
    var comiclist=""//만화 회차 링크

    fun getElement(args:String){
        var html=args
        title=html.substring(html.indexOf("title=")+7)//제목 알아냄
        title=title.substring(0,title.indexOf("\""))

        var a=html.substring(html.indexOf("src=\"")+5) //표지 찾아냄
        imagesrc = a.substring(0,a.indexOf(".jpg") + 4)

        html=html.substring(html.indexOf("href")+7)
        comiclist=html
        comiclist="https://comic.naver.com/"+comiclist.substring(0,comiclist.indexOf("\""))

        var index=html.indexOf("weekday")+7//요일 알아냄
        day=html.substring(index+1,index+4)
    }
}

class linkControl{

    var ElementList=ArrayList<itemList>()

    fun sethtml(text:String):ArrayList<itemList>{
        var a= text.split("<li>") //html을 <li>구분해서 분할(각 만화로 나누어짐)
        for(i in a){
            if(!("src" in a)) {
                var itemlist=itemList()//나눈 만화를 각각 itemlist에 넣어 저장
                itemlist.getElement(i)
                ElementList.add(itemlist)
            }
        }
        return ElementList
    }
}
