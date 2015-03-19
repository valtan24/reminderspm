package com.reminders.valerie.reminders.scheduleservice;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.reminders.valerie.reminders.NewTaskActivity;


public class NotifyService extends Service{

    public class ServiceBinder extends Binder{
        NotifyService getService(){
            return NotifyService.this;
        }
    }

    //unique id to identify the notifcation
    private static final int NOTIFICATION = 2412;
    public static final String INTENT_NOTIFY = "com.reminders.valerie.reminders.scheduleservice.INTENT_NOTIFY";
    private NotificationManager notification_mgr;

    private final IBinder binder = new ServiceBinder();

    @Override
    public void onCreate(){
        notification_mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if(intent.getBooleanExtra(INTENT_NOTIFY, false)){
            showNotification();
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void showNotification(){
        CharSequence title = "You have a task to do!";
        CharSequence text = "Task title and date and time here!";
        int icon = R.drawable.ic_dialog_alert;
        //time to show on the notification
        long time = System.currentTimeMillis();

        //build notifcation
        NotificationCompat.Builder notification_builder = new NotificationCompat.Builder(this)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(text);

        NotificationCompat.InboxStyle inbox_style = new NotificationCompat.InboxStyle();
        String[] events = {"task title", "task date", "task time"};
        inbox_style.setBigContentTitle(title);
        for(int i = 0; i<events.length;i++){
            inbox_style.addLine(events[i]);
        }
        notification_builder.setStyle(inbox_style);

        Intent result_intent = new Intent(this, NewTaskActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NewTaskActivity.class);
        stackBuilder.addNextIntent(result_intent);
        PendingIntent result_pending_intent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notification_builder.setContentIntent(result_pending_intent);
        notification_mgr.notify(NOTIFICATION, notification_builder.build());

        stopSelf();
    }
}
