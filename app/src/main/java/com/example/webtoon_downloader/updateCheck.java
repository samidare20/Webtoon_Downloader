package com.example.webtoon_downloader;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class updateCheck extends Service {
    public updateCheck() {
    }

    public static Intent serviceIntent = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder mbuild = new NotificationCompat.Builder(this, "1004")
                .setContentTitle("check")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(false);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(2, mbuild.build());

        final int[] index = {1};
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                if (index[0]++ >= 10)
                    this.cancel();
                Log.d("mydebug", "check" + index[0]);
            }
        };
        Timer timer = new Timer();
        timer.schedule(tt, 0, 3000);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.SECOND, 1);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
        Log.d("mydebug", "service end");

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.SECOND, 3);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
        Log.d("mydebug", "service end");
    }


}
