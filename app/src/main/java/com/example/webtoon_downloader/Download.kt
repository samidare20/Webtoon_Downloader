package com.example.webtoon_downloader

import android.os.Environment
import android.util.Log
import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class Download(link:String,title:String,series:String) {
    var path = Environment.getExternalStorageDirectory().absolutePath.toString() + "/download/"


    init {
        Log.d("mydebug", "check")
        makefolder(title,series)
        try {
            val doc = Jsoup.connect(link).get()
            val element = doc.select("div.wt_viewer")
            val html = element.toString().split(">")
            download(html)
        }
        catch (ex: Exception) {
            Log.d("mydebug", ex.toString())
        }
    }
    fun makefolder(title:String,series: String)
    {
        path+="Webtoon"
        var folder=File(path)
        if(folder.exists()==false)
            folder.mkdir()

        path+="/"+series
        folder=File(path)
        if(folder.exists()==false)
            folder.mkdir()

        path+="/"+title
        folder=File(path)
        if(folder.exists()==false)
            folder.mkdir()
    }
    fun download(html: List<String>) {
        var nowstring:String
        var index=1
        for (i in html) {
            if (i.indexOf("title") != -1) {
                nowstring = i.substring(i.indexOf("src") + 5, i.indexOf("title")-2)
                Log.d("mydebug",nowstring)

                val filename = index.toString()
                index++

                val filepath = path + "/" + filename + ".jpg"
                val file=File(filepath)
                if (file.exists()==true) {
                    Log.d("mydebug","exist file")
                    continue
                }
                Log.d("mydebug",filepath)

                try {
                    val imageurl = URL(nowstring)
                    val conn=imageurl.openConnection() as HttpURLConnection
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0")
                    var input:InputStream
                    if(conn.responseCode==HttpURLConnection.HTTP_OK)
                        input=conn.inputStream
                    else
                        input=conn.errorStream  //웹페이지 연결 설정

                    val file = File(filepath)
                    val fos = FileOutputStream(file)
                    val tmpbyte = ByteArray(conn.contentLength)
                    var read : Int
                    while (true) {
                        read = input.read(tmpbyte)
                        if(read<0)
                            break
                        fos.write(tmpbyte, 0, read)
                    }//파일 다운로드
                    input.close()
                    fos.close()
                } catch (e: java.lang.Exception) {
                    Log.d("mydebug",e.toString())
                }
            }
        }
    }
}