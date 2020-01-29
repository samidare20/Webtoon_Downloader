package com.example.webtoon_downloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent in = new Intent(context, restart.class);
            context.startForegroundService(in);
            Log.d("mydebug", "alarm check");
        } else {
            Intent in = new Intent(context, updateCheck.class);
            context.startService(in);
        }
    }
}
