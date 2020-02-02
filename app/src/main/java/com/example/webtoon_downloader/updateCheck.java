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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static android.content.Context.ALARM_SERVICE;

public class updateCheck extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        linkControl link=new linkControl();
        //Log.d("mydebug","background check");
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect("https://comic.naver.com/webtoon/weekday.nhn").get();
                Elements elements = doc.select("div.col_inner");
                link.findUpdate(elements.toString());
            }
            catch (Exception e){
                Log.d("mydebug",e.toString());
            }
        }).start();
        //makeAlarm(context);
    }

    public void makeAlarm(Context context) {

        Intent intent = new Intent(context, updateCheck.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        long firstTime = System.currentTimeMillis() / 1000;
        firstTime += 10; //10초 후 알람 이벤트 발생
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        //API 23 이상
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000*60*5, sender);
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, sender);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            am.setExact(AlarmManager.RTC_WAKEUP, firstTime, sender);
    }
}
