package com.example.webtoon_downloader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import static android.content.Context.ALARM_SERVICE;

public class updateCheck extends BroadcastReceiver {
    boolean first = true;
    String mainlink = "https://comic.naver.com/webtoon/weekday.nhn";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("hello", "alarm check");
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect(mainlink).get();
                Elements elements = doc.select("div.col_inner");
                String[] html = elements.toString().split("<li>");
                String href;
                String title;
                int index;
                for (String s : html) {
                    if (s.contains("updt")) {
                        href = "https://comic.naver.com" + s.substring(s.indexOf("href") + 6, s.indexOf("onclick") - 2);
                        index = s.indexOf("title=");
                        s = s.substring(index + 7);
                        title = s.substring(0, s.indexOf("\""));
                        checkEpisode(title, href, context);
                    }

                }
            } catch (Exception ignored) {
            }
        }).start();
        //makeAlarm(context);
    }

    public void checkEpisode(String webtoon, String link, Context context) {
        try {
            Document doc = Jsoup.connect(link).get();
            Elements element = doc.select("tbody");
            String[] html = element.toString().split("<tr>");
            for (int i=html.length-1;i>=0;i--) {
                String s=html[i];
                if (s.contains("toonup")) {
                    String href = "https://comic.naver.com" + s.substring(s.indexOf("href") + 6, s.indexOf("onclick") - 2);
                    String title = s.substring(s.indexOf("title=") + 7, s.indexOf("alt=") - 2);

                    Room_Database db=Room_Database.getInstance(context);
                    Room_Data data=db.Room_DAO().selectTitle(webtoon);
                    Log.d("mdg",data.title);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 10, sender);
            //am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000*60*5, sender);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, sender);
    }
}
