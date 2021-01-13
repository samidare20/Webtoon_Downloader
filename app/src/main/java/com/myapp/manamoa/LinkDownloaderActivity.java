package com.myapp.manamoa;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.myapp.webtoon_downloader.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class LinkDownloaderActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linkdownloader_activity_main);

        Toolbar tb = findViewById(R.id.toolbar);
        tb.setTitle("만화 다운로더");
        setSupportActionBar(tb);
        init();
    }

    private void init() {
        Button searchButton = findViewById(R.id.SearchButton);
        EditText searchInput = findViewById(R.id.LinkInput);
        searchButton.setOnClickListener(v -> {
            String link = searchInput.getText().toString();
            search(link);
        });
    }

    private void search(String link) {

        try{
            Integer.parseInt(link);
            link="https://hitomi.la/reader/"+link+".html#";
        }
        catch (Exception ignored){}

        String finalLink = link;
        new Thread(() -> {

            String page=finalLink;
            for(int i=1;i<10;i++){
                Document doc= null;
                try {
                    doc=Jsoup.connect(finalLink).get();
                    Elements elements=doc.select("img");
                    String url=elements.toString();
                    Log.d("mydebug",url);
                    /*doc = Jsoup.connect(page+i).get();String html=doc.toString();
                    //Elements elements=doc.select("");
                    Log.d("mydebug",html);
                    Log.d("mydebug",doc.toString());*/
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }).start();


    }
}