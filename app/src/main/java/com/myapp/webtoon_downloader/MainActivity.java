package com.myapp.webtoon_downloader;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.Toast;

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

    Boolean gettingData = true;
    private Context mcontext = this;
    private double backKeyPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downloader_activity_main);
        new updateCheck().makeAlarm(this);


        ///tabhost 세팅
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
        setTab();
        Toolbar tb = findViewById(R.id.toolbar);
        tb.setTitle("웹툰 다운로더");
        setSupportActionBar(tb);
        setDrawer();

        makePermission();
        createNotificationChannel();


    }

    void setTab() {//타일 설정
        Handler mhandler = new Handler();

        while (gettingData) {
            try {
                wait();
            } catch (Exception ignored) {

            }
        }
        try {
            notifyAll();
        } catch (Exception ignored) {

        }

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
                        WebtoonTiles tile = new WebtoonTiles(mcontext);
                        tile.setData(data.title, data.ThumbnailLink, data.EpisodeLink, data.bookmark);
                        layout.addView(tile);
                        tile.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    });
                }
                nameindex++;
            }
        }).start();
    }

    void setDrawer() {
        NavigationView navi = findViewById(R.id.navi);

        navi.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.viewer) {
                DrawerLayout drawer = findViewById(R.id.drawer);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(this, ViewerActivity.class);
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
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                Toast.makeText(this, "뒤로가기를 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                finish();
            }
        }
    }

    @Override
    public void onResume() {
        gettingData = false;
        new Thread(() -> {
            Log.d("hello", "onresume");
            try {
                Document doc = Jsoup.connect("https://comic.naver.com/webtoon/weekday.nhn").get();
                Elements elements = doc.select("div.col_inner");
                new linkControl().sethtml(mcontext, elements.toString());
            } catch (Exception ignored) {
            }
        }).start();
        super.onResume();
    }
}