package com.reminders.valerie.reminders.notificationservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.Reminder;


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
        scheduleNextReminder(context);
    }

    private void scheduleNextReminder(Context context){
        if(dbhandler!=null){
            dbhandler.markReminderAsFired(reminder_id);
            Log.i("reminder id to mark as fired", ""+reminder_id);
            Reminder next_reminder = dbhandler.getNextReminder(task_id);
            if(next_reminder != null){
                new AlarmTask(context, task_id, next_reminder.getId()).run();
            }
            dbhandler.close();
        }
    }
}
