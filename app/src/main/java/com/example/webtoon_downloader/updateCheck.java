package com.example.webtoon_downloader;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class updateCheck extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notimanager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        Notification noti=new NotificationCompat.Builder(context,"1004")
                .setContentTitle("background")
                .setContentText("check")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
        notimanager.notify(3,noti);
        ConnectivityManager manager=(ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo network=manager.getActiveNetworkInfo();
        if(network==null|| !network.isConnected() ||network.getType()!=ConnectivityManager.TYPE_WIFI)
        {
            Log.d("hello","no network");
            makeAlarm(context);
            return;
        }
        new Thread(() -> {
            Room_Database db = Room_Database.getInstance(context);
            List<Room_Data> data = db.Room_DAO().selectBookmark();
            for (Room_Data a : data) {
                checkEpisode(a.title, a.EpisodeLink, context);
            }
        }).start();
        makeAlarm(context);
    }

    public void checkEpisode(String webtoon, String link, Context context) {

        try {
            Document doc = Jsoup.connect(link).get();
            Elements element = doc.select("tbody");
            String[] html = element.toString().split("<tr>");
            for (int i = html.length - 1; i >= 0; i--) {
                String s = html[i];
                if (s.contains("toonup")) {
                    String href = "https://comic.naver.com" + s.substring(s.indexOf("href") + 6, s.indexOf("onclick") - 2);
                    String title = s.substring(s.indexOf("title=") + 7, s.indexOf("alt=") - 2);

                    Room_Database db = Room_Database.getInstance(context);
                    Room_Data data = db.Room_DAO().selectTitle(webtoon);
                    if (!data.update.equals(title)) {
                        Log.d("hello","download check");
                        new Download(href, title, webtoon, context);
                        data.update = title;
                        db.Room_DAO().update(data);
                    }
                    else{
                        Log.d("hello",data.update);
                    }
                }
            }
        } catch (Exception e) {
            Log.d("hello", e.toString());
        }
    }

    public void makeAlarm(Context context) {
        Intent intent = new Intent(context, updateCheck.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        //API 23 이상
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 *10, sender);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, sender);
    }
}
