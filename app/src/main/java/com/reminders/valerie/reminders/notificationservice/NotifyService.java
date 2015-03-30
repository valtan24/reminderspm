package com.reminders.valerie.reminders.notificationservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.reminders.valerie.reminders.EditTaskActivity;
import com.reminders.valerie.reminders.TaskDBHandler;


public class NotifyService extends Service {

    private NotificationManager n_mgr;

    private static int notification_id=1;

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

        if(extras.getString("audio_uri")!=null){
            Log.i("have audio", "true");
            String uri = extras.getString("audio_uri");
            notification_builder.setSound(Uri.parse(uri), AudioAttributes.USAGE_NOTIFICATION_EVENT);
        }

        NotificationCompat.InboxStyle inbox_style = new NotificationCompat.InboxStyle();
        inbox_style.addLine(date);
        inbox_style.addLine(time);
        notification_builder.setStyle(inbox_style);

        Intent dismiss_intent = new Intent(this, DismissNotification.class);
        dismiss_intent.putExtra("notification_id", notification_id);
        PendingIntent pending_dismiss = PendingIntent.getService(this.getApplicationContext(), 0, dismiss_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification_builder.addAction(android.R.drawable.ic_lock_idle_alarm, "Close", pending_dismiss);

        Intent open_task_intent = new Intent(this, EditTaskActivity.class);
        TaskDBHandler dbhandler = new TaskDBHandler(this);
        Log.i("task id in notification", ""+extras.getLong("task_id"));
        Bundle args = dbhandler.getTaskById(extras.getLong("task_id"));
        open_task_intent.putExtra("arguments", args);
        open_task_intent.putExtra("notification_id", notification_id);

        PendingIntent pending_intent = PendingIntent.getActivity(this.getApplicationContext(),0, open_task_intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification_builder.addAction(android.R.drawable.ic_menu_edit, "Open Task", pending_intent);
        notification_builder.setContentIntent(pending_intent);
        n_mgr.notify(notification_id, notification_builder.build()); //notification id should be unique as well
        notification_id = notification_id % Integer.MAX_VALUE + 1;
        dbhandler.close();
        stopSelf();


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
