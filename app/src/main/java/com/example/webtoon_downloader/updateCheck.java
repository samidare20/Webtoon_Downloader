package com.example.webtoon_downloader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import static android.content.Context.ALARM_SERVICE;

public class updateCheck extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("mydebug", "background check");
        makeAlarm(context);
    }

    public void makeAlarm(Context context) {

        Intent intent = new Intent(context, updateCheck.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        long firstTime = System.currentTimeMillis() / 1000;
        firstTime += 10; //10초 후 알람 이벤트 발생
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        //API 23 이상
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, sender);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            am.setExact(AlarmManager.RTC_WAKEUP, firstTime, sender);
    }
}
