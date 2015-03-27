package com.reminders.valerie.reminders.notificationservice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class DismissNotification extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i("dismiss notification", "started");
        int notification_id = intent.getExtras().getInt("notification_id");
        if(notification_id != -1){
            Log.i("dimissing notification", ""+notification_id);
            NotificationManager notification_mgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notification_mgr.cancel(notification_id);
        }
        stopSelf();
        return START_NOT_STICKY;
    }

}
