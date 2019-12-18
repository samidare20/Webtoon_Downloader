package com.example.webtoon_downloader;

import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity {
    String mainURL="https://comic.naver.com/webtoon/weekday.nhn";
    linkControl linkControl=new linkControl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ////tabhost 세팅
        TabHost host= findViewById(R.id.host);
        host.setup();
        TabHost.TabSpec monspec=host.newTabSpec("tab1");
        monspec.setContent(R.id.tabContent);
        monspec.setIndicator("월요웹툰");
        host.addTab(monspec);

        TabHost.TabSpec tuespec=host.newTabSpec("tab2");
        tuespec.setContent(R.id.tabContent);
        tuespec.setIndicator("화요웹툰");
        host.addTab(tuespec);

        TabHost.TabSpec wedspec=host.newTabSpec("tab3");
        wedspec.setContent(R.id.tabContent);
        wedspec.setIndicator("수요웹툰");
        host.addTab(wedspec);

        TabHost.TabSpec thuspec=host.newTabSpec("tab4");
        thuspec.setContent(R.id.tabContent);
        thuspec.setIndicator("목요웹툰");
        host.addTab(thuspec);

        TabHost.TabSpec frispec=host.newTabSpec("tab5");
        frispec.setContent(R.id.tabContent);
        frispec.setIndicator("금요웹툰");
        host.addTab(frispec);

        TabHost.TabSpec satspec=host.newTabSpec("tab6");
        satspec.setContent(R.id.tabContent);
        satspec.setIndicator("토요웹툰");
        host.addTab(satspec);

        TabHost.TabSpec sonspec=host.newTabSpec("tab7");
        sonspec.setContent(R.id.tabContent);
        sonspec.setIndicator("일요웹툰");
        host.addTab(sonspec);
        ////


        new Thread() {
            public void run() {
                try {
                    Document doc = Jsoup.connect(mainURL).get();
                    Elements elements=doc.select("div.col_inner");
                    linkControl.sethtml(elements.toString());
                }

                catch (Exception ex) {
                    Log.d("mydebug",ex.toString());
                }
            }
        }.start();
    }
}
