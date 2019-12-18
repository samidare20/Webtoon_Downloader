package com.example.webtoon_downloader


class itemList{//표지 이미지, 웹툰 이름 저장하는 클래스
    var title=""//이름
    var imagesrc=""//표지 이미지 링크
    var day=""

    fun getElement(html:String){
        title=html.substring(html.indexOf("title=")+7)//제목 알아냄
        title=title.substring(0,title.indexOf("\""))

        var a=html.substring(html.indexOf("src=\"")+5)//표지 찾아냄
        if(a in "jpg")
            imagesrc = a.substring(0,html.indexOf(".jpg\"") + 6)
        else if(a in "png")
            imagesrc = a.substring(0,html.indexOf(".png\"") + 6)
        else if(a in "gif")
            imagesrc = a.substring(0,html.indexOf(".gif\"") + 6)

        var index=html.indexOf("weekday")+7//요일 알아냄
        day=html.substring(index+1,index+4)
        println(day)
    }
}

class linkControl {
    var ElementList=ArrayList<itemList>()

    fun sethtml(text:String){
        var itemlist=itemList()
        var a= text.split("<li>")
        for(i in a){
            if(!("src" in a)) {
                itemlist.getElement(i)
                ElementList.add(itemlist)
            }
        }
    }
}