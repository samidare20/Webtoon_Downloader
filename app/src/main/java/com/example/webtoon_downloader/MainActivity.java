package com.example.webtoon_downloader;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public linkControl linkControl = new linkControl();
    public Context mcontext = this;
    String mainURL = "https://comic.naver.com/webtoon/weekday.nhn";

    Point displaySize = new Point();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        Thread a = new Thread(() -> {
            try {
                Document doc = Jsoup.connect(mainURL).get();
                Elements elements = doc.select("div.col_inner");
                linkControl.sethtml(mcontext, elements.toString());
            } catch (Exception ex) {
                Log.d("mydebug", ex.toString());
            }
        });
        a.start();
        ////tabhost 세팅
        TabHost host = findViewById(R.id.host);
        host.setup();
        TabHost.TabSpec monspec = host.newTabSpec("tab1");
        monspec.setContent(R.id.montabContent);
        monspec.setIndicator("월");
        host.addTab(monspec);

        TabHost.TabSpec tuespec = host.newTabSpec("tab2");
        tuespec.setContent(R.id.tuetabContent);
        tuespec.setIndicator("화");
        host.addTab(tuespec);

        TabHost.TabSpec wedspec = host.newTabSpec("tab3");
        wedspec.setContent(R.id.wedtabContent);
        wedspec.setIndicator("수");
        host.addTab(wedspec);

        TabHost.TabSpec thuspec = host.newTabSpec("tab4");
        thuspec.setContent(R.id.thutabContent);
        thuspec.setIndicator("목");
        host.addTab(thuspec);

        TabHost.TabSpec frispec = host.newTabSpec("tab5");
        frispec.setContent(R.id.fritabContent);
        frispec.setIndicator("금");
        host.addTab(frispec);

        TabHost.TabSpec satspec = host.newTabSpec("tab6");
        satspec.setContent(R.id.sattabContent);
        satspec.setIndicator("토");
        host.addTab(satspec);

        TabHost.TabSpec sonspec = host.newTabSpec("tab7");
        sonspec.setContent(R.id.suntabContent);
        sonspec.setIndicator("일");

        host.addTab(sonspec);
        try {
            a.join();
        } catch (Exception ignored) {  }
        setTab();
        new Thread(() -> {
            makePermission();
            createNotificationChannel();
            Display display = getWindowManager().getDefaultDisplay();
            display.getSize(displaySize);
        }).start();
    }

    void setTab() {//타일 설정
        Handler mhandler = new Handler();

        new Thread(() -> {
            String[] names = new String[]{"mon", "tue", "wed", "thu", "fri", "sat", "sun"};
            int nameindex = 0;
            Room_Database db = Room_Database.getInstance(mcontext);
            List<Room_Todo> datalist;

            while (nameindex < 7) {
                int id = MainActivity.this.getResources().getIdentifier(names[nameindex] + "tabContent", "id", MainActivity.this.getPackageName());
                LinearLayout layout = MainActivity.this.findViewById(id);

                datalist = db.Room_DAO().selectDay(names[nameindex]);
                for (int i = 0; i < datalist.size(); i++) {
                    Room_Todo data = datalist.get(i);
                    mhandler.post(() -> {
                        WebtoonTiles tile = new WebtoonTiles(mcontext, null, 0);
                        tile.setData(data.title, data.ThumbnailLink, data.EpisodeLink);
                        layout.addView(tile);
                        tile.getLayoutParams().width = displaySize.x;
                    });

                }
                nameindex++;
            }
        }).start();

        updateCheck a = new updateCheck();
        a.makeAlarm(this);
    }

    private void makePermission() {
        if (Build.VERSION.SDK_INT > 22) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1004", "yee", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            channel.setVibrationPattern(new long[0]);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
    }
}