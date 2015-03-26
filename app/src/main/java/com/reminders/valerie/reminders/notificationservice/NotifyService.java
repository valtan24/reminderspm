package com.reminders.valerie.reminders.notificationservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.reminders.valerie.reminders.EditTaskActivity;
import com.reminders.valerie.reminders.TaskDBHandler;


public class NotifyService extends Service {

    private NotificationManager n_mgr;

    @Override
    public void onCreate(){
        n_mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i("starting notify service", "true");
        showNotification(intent.getExtras());
        return START_NOT_STICKY;
    }

    public void showNotification(Bundle extras){
        Log.i("showing notification", "started");
        String title = extras.getString("task_title");
        String date = extras.getString("task_date");
        String time = extras.getString("task_time");


        NotificationCompat.Builder notification_builder = new NotificationCompat.Builder(this)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(android.R.drawable.ic_dialog_alert);

        NotificationCompat.InboxStyle inbox_style = new NotificationCompat.InboxStyle();
        inbox_style.addLine(date);
        inbox_style.addLine(time);
        notification_builder.setStyle(inbox_style);

        Intent open_task_intent = new Intent(this, EditTaskActivity.class);
        TaskDBHandler dbhandler = new TaskDBHandler(this);
        Log.i("task id in notification", ""+extras.getLong("task_id"));
        Bundle args = dbhandler.getTaskById(extras.getLong("task_id"));
        open_task_intent.putExtra("arguments", args);

        PendingIntent pending_intent = PendingIntent.getActivity(this.getApplicationContext(),0, open_task_intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification_builder.addAction(android.R.drawable.ic_lock_idle_alarm, "Open Task", pending_intent);
        notification_builder.setContentIntent(pending_intent);
        n_mgr.notify(2412, notification_builder.build());
        dbhandler.close();
        stopSelf();


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
