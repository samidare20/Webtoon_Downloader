package com.myapp.webtoon_downloader;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class test {

    test() {
        Log.d("mydebug","hello");
        new Thread(() -> {
            {
                Document doc;
                try {
                    Jsoup.connect("https://map.naver.com/v5/search/%EA%B0%95%EB%82%A8%EA%B5%AC%20%ED%95%99%EC%9B%90?c=14143466.2922833,4508175.8844563,14,0,0,0,dh")
                    doc=Jsoup.parse()
                    String text = doc.select("div.container").toString();
                    Log.d("mydebug", text);
                    String[] a=text.split("");
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();


    }


}
