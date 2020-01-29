package com.example.webtoon_downloader;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class restart extends Service {
    public restart() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"1004");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        startForeground(9,builder.build());

        Intent in=new Intent(this,updateCheck.class);
        startService(in);
        stopForeground(true);
        stopSelf();
        return START_NOT_STICKY;
    }
}
