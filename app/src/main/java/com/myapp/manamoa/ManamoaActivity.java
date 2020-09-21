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
import org.jsoup.select.Elements;

import java.io.IOException;

public class ManamoaActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manamoa_activity_main);

        Toolbar tb = findViewById(R.id.toolbar);
        tb.setTitle("마나모아 다운로더");
        setSupportActionBar(tb);
        init();
    }

    private void init() {
        Button searchButton = findViewById(R.id.manamoaSearchButton);
        EditText searchInput = findViewById(R.id.manamoaSearchLink);
        searchButton.setOnClickListener(v -> {
            String link = searchInput.getText().toString();
            search(link);
        });
    }

    private void search(String link) {
        new Thread(() -> {
            String html = "";
            String[] episode = {"",};
            try {
                Document doc = Jsoup.connect(link).get();
                Elements element = doc.select("div.toon-nav");
                html = element.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            html = html.substring(html.lastIndexOf("href") + 6);
            html = html.substring(0, html.indexOf("\""));
            html = html.replace("amp;", "");
            html = "https://manamoa.net" + html;
            Log.d("mydebug", html);

            try {
                Document doc = Jsoup.connect(html).get();
                Elements element = doc.select("div.chapter-list");
                episode = element.toString().split("<div class=\"title\">");
            } catch (Exception ignored) {

            }
            for (int i = 0; i < episode.length; i++)
                Log.d("mydebug", episode[i]);

        }).start();
    }
}
