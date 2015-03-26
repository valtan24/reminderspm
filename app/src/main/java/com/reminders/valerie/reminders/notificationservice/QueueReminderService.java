package com.reminders.valerie.reminders.notificationservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.Reminder;
import com.reminders.valerie.reminders.model.Task;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class QueueReminderService extends Service {


    public class QueueReminderBinder extends Binder {
        QueueReminderService getService(){ return QueueReminderService.this; }
    }

    private final QueueReminderBinder binder = new QueueReminderBinder();
    private TaskDBHandler dbhandler;
    private long task_id;
    private long reminder_id;
    private AlarmManager alarm_mgr;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate(){
        dbhandler = new TaskDBHandler(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i("starting queue reminder service", "true");
        Bundle extras = intent.getExtras();
        task_id = extras.getLong("task_id");
        reminder_id = extras.getLong("reminder_id");
        Reminder next_reminder = dbhandler.getNextReminder(task_id);
        Bundle task_bundle = dbhandler.getTaskById(task_id);
        //use alarm manager and broadcast receiver here
        Calendar cal = Calendar.getInstance();
        cal.set(next_reminder.getYear(), next_reminder.getMonth(), next_reminder.getDay());
        cal.set(Calendar.HOUR, next_reminder.getHour());
        cal.set(Calendar.MINUTE, next_reminder.getMinute());
        cal.set(Calendar.SECOND, 0);

        Intent receiver_intent = new Intent(QueueReminderService.this, NotificationReceiver.class);
        receiver_intent.putExtra("task_title", task_bundle.getString("title"));
        int hour = task_bundle.getInt("task_hour");
        int minute = task_bundle.getInt("task_minute");
        int year = task_bundle.getInt("task_year");
        int month = task_bundle.getInt("task_month");
        int day = task_bundle.getInt("task_day");


        String month_name = new DateFormatSymbols().getMonths()[month];
        String date_text = day + " " + month_name + " " + year;
        String time_text = "";
        if(hour < 10) time_text = time_text + "0" + hour;
        else time_text = time_text + hour;
        if(minute< 10) time_text = time_text + ":0" + minute;
        else time_text = time_text + ":" + minute;
        receiver_intent.putExtra("task_date", date_text);
        receiver_intent.putExtra("task_time", time_text);
        receiver_intent.putExtra("task_id", task_id);
        receiver_intent.putExtra("reminder_id", reminder_id);

        PendingIntent pending_intent = PendingIntent.getBroadcast(QueueReminderService.this, 2412, receiver_intent, 0);
        alarm_mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm_mgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pending_intent);

        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        dbhandler.close();
        super.onDestroy();
    }

}
