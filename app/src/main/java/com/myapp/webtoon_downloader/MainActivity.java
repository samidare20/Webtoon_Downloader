package com.myapp.webtoon_downloader;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.myapp.webtoon_viewer.ViewerActivity;

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
        setContentView(R.layout.downloader_activity_main);
        Toolbar tb=findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setTitle("웹툰 다운로더");

        new updateCheck().makeAlarm(this);

        Thread a = new Thread(() -> {
            try {
                Document doc = Jsoup.connect(mainURL).get();
                Elements elements = doc.select("div.col_inner");
                linkControl.sethtml(mcontext, elements.toString());
            } catch (Exception ignored) {
            }
        });
        a.start();
        ////tabhost 세팅
        TabHost host = findViewById(R.id.host);
        host.setup();
        TabHost.TabSpec monspec = host.newTabSpec("montabScroll");
        monspec.setContent(R.id.montabScroll);
        monspec.setIndicator("월");
        host.addTab(monspec);

        TabHost.TabSpec tuespec = host.newTabSpec("tuetabScroll");
        tuespec.setContent(R.id.tuetabScroll);
        tuespec.setIndicator("화");
        host.addTab(tuespec);

        TabHost.TabSpec wedspec = host.newTabSpec("wedtabScroll");
        wedspec.setContent(R.id.wedtabScroll);
        wedspec.setIndicator("수");
        host.addTab(wedspec);

        TabHost.TabSpec thuspec = host.newTabSpec("thutabScroll");
        thuspec.setContent(R.id.thutabScroll);
        thuspec.setIndicator("목");
        host.addTab(thuspec);

        TabHost.TabSpec frispec = host.newTabSpec("fritabScroll");
        frispec.setContent(R.id.fritabScroll);
        frispec.setIndicator("금");
        host.addTab(frispec);

        TabHost.TabSpec satspec = host.newTabSpec("sattabScroll");
        satspec.setContent(R.id.sattabScroll);
        satspec.setIndicator("토");
        host.addTab(satspec);

        TabHost.TabSpec sonspec = host.newTabSpec("suntabScroll");
        sonspec.setContent(R.id.suntabScroll);
        sonspec.setIndicator("일");

        host.addTab(sonspec);
        host.setOnTabChangedListener(tabId -> {
            int id = MainActivity.this.getResources().getIdentifier(tabId, "id", MainActivity.this.getPackageName());
            ScrollView view = findViewById(id);
            view.fullScroll(View.FOCUS_UP);
        });
        try {
            a.join();
        } catch (Exception ignored) {
        }
        makePermission();
        createNotificationChannel();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(displaySize);
        setDrawer();
        setTab();


    }
    void setTab() {//타일 설정
        Handler mhandler = new Handler();

        new Thread(() -> {
            String[] names = new String[]{"mon", "tue", "wed", "thu", "fri", "sat", "sun"};
            int nameindex = 0;
            Room_Database db = Room_Database.getInstance(mcontext);
            List<Room_Data> datalist;

            while (nameindex < 7) {
                int id = MainActivity.this.getResources().getIdentifier(names[nameindex] + "tabContent", "id", MainActivity.this.getPackageName());
                LinearLayout layout = MainActivity.this.findViewById(id);

                datalist = db.Room_DAO().selectDay(names[nameindex]);
                for (int i = 0; i < datalist.size(); i++) {
                    Room_Data data = datalist.get(i);
                    mhandler.post(() -> {
                        WebtoonTiles tile = new WebtoonTiles(mcontext, null, 0);
                        tile.setData(data.title, data.ThumbnailLink, data.EpisodeLink, data.bookmark);
                        layout.addView(tile);
                        tile.getLayoutParams().width = displaySize.x;
                    });

                }
                nameindex++;
            }
        }).start();


    }
    void setDrawer(){
        NavigationView navi=findViewById(R.id.drawer);
        navi.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.viewer) {
                Intent intent=new Intent(this, ViewerActivity.class);
                startActivity(intent);
            }
            return true;
        });
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}