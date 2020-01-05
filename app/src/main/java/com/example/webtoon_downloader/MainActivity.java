package com.example.webtoon_downloader;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String mainURL="https://comic.naver.com/webtoon/weekday.nhn";
    public linkControl linkControl=new linkControl();
    public Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext=this;
        setContentView(R.layout.activity_main);
        ////tabhost 세팅
        TabHost host= findViewById(R.id.host);
        host.setup();
        TabHost.TabSpec monspec=host.newTabSpec("tab1");
        monspec.setContent(R.id.MontabContent);
        monspec.setIndicator("월");
        host.addTab(monspec);

        TabHost.TabSpec tuespec=host.newTabSpec("tab2");
        tuespec.setContent(R.id.TuetabContent);
        tuespec.setIndicator("화");
        host.addTab(tuespec);

        TabHost.TabSpec wedspec=host.newTabSpec("tab3");
        wedspec.setContent(R.id.WedtabContent);
        wedspec.setIndicator("수");
        host.addTab(wedspec);

        TabHost.TabSpec thuspec=host.newTabSpec("tab4");
        thuspec.setContent(R.id.ThutabContent);
        thuspec.setIndicator("목");
        host.addTab(thuspec);

        TabHost.TabSpec frispec=host.newTabSpec("tab5");
        frispec.setContent(R.id.FritabContent);
        frispec.setIndicator("금");
        host.addTab(frispec);

        TabHost.TabSpec satspec=host.newTabSpec("tab6");
        satspec.setContent(R.id.SattabContent);
        satspec.setIndicator("토");
        host.addTab(satspec);

        TabHost.TabSpec sonspec=host.newTabSpec("tab7");
        sonspec.setContent(R.id.SuntabContent);
        sonspec.setIndicator("일");

        host.addTab(sonspec);

        ArrayList<itemList> a= new ArrayList<>();
        new  Thread() {
            public void run()  {
                try {
                    Document doc = Jsoup.connect(mainURL).get();
                    Elements elements=doc.select("div.col_inner");
                    linkControl.sethtml(elements.toString());
                }

                catch (Exception ex) {
                    Log.d("mydebug",ex.toString());
                }
                handler.sendMessage(handler.obtainMessage());

            }
        }.start();
        Log.d("mydebug","func end");
    }
   Handler handler=new Handler(msg -> {
       linkControl.getElementList().size();
       return true;
   });
}
