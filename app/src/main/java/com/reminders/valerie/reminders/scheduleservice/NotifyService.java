package com.reminders.valerie.reminders.scheduleservice;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.reminders.valerie.reminders.EditTaskActivity;
import com.reminders.valerie.reminders.NewTaskActivity;
import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.CursorToBundle;


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
            showNotification(intent.getExtras());
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void showNotification(Bundle extras){
        CharSequence title = extras.getString("task_title");
        int icon = R.drawable.ic_dialog_alert;
        //time to show on the notification
        long time = System.currentTimeMillis();

        //build notifcation
        NotificationCompat.Builder notification_builder = new NotificationCompat.Builder(this)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationCompat.InboxStyle inbox_style = new NotificationCompat.InboxStyle();
        String[] events = {extras.getString("task_date"), extras.getString("task_time")};
        inbox_style.setBigContentTitle(title);
        for(int i = 0; i<events.length;i++){
            inbox_style.addLine(events[i]);
        }
        notification_builder.setStyle(inbox_style);

        TaskDBHandler dbhandler = new TaskDBHandler(getApplicationContext());
        Bundle args = dbhandler.getTaskById(extras.getLong("task_id"));
        Intent edit_task_intent = new Intent(this, EditTaskActivity.class);
        edit_task_intent.putExtra("arguments", args);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(EditTaskActivity.class);
        stackBuilder.addNextIntent(edit_task_intent);
        PendingIntent result_pending_intent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notification_builder.setContentIntent(result_pending_intent);
        notification_mgr.notify(NOTIFICATION, notification_builder.build());

        dbhandler.markReminderAsFired(extras.getLong("reminder_id"));
        dbhandler.close();
        stopSelf();
    }
}
