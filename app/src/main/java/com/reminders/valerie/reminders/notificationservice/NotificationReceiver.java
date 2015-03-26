package com.reminders.valerie.reminders.notificationservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.reminders.valerie.reminders.TaskDBHandler;


public class NotificationReceiver extends BroadcastReceiver{

    private TaskDBHandler dbhandler;
    private long reminder_id;
    private long task_id;

    public NotificationReceiver(){
        Log.i("receiver constructor", "reached");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("receiver", "notification received");
        dbhandler = new TaskDBHandler(context);
        Bundle extras = intent.getExtras();
        reminder_id = extras.getLong("reminder_id");
        task_id = extras.getLong("task_id");
        Intent notify_intent = new Intent(context, NotifyService.class);
        notify_intent.putExtra("task_title", extras.getString("task_title"));
        notify_intent.putExtra("task_date", extras.getString("task_date"));
        notify_intent.putExtra("task_time", extras.getString("task_time"));
        notify_intent.putExtra("task_id", task_id);

        context.startService(notify_intent);

        //mark reminder as fired and schedule next one
        scheduleNextReminder();
    }

    private void scheduleNextReminder(){

    }
}
