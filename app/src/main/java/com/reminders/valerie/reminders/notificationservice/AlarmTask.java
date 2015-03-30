package com.reminders.valerie.reminders.notificationservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.Reminder;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

public class AlarmTask implements Runnable {

    private TaskDBHandler dbhandler;
    private long task_id;
    private long reminder_id; //to mark as fired
    private AlarmManager alarm_mgr;
    private Context context;

    private static int requestCode = 1;

    public AlarmTask(Context context, long task_id, long reminder_id){
        alarm_mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        this.context = context;
        dbhandler = new TaskDBHandler(this.context);
        this.task_id = task_id;
        this.reminder_id = reminder_id;
    }


    @Override
    public void run() {
        Log.i("starting alarm task", "true");
        Log.i("reminder id", ""+reminder_id);
        Reminder next_reminder = dbhandler.getNextReminder(task_id);
        if(next_reminder != null){
            Log.i("next reminder id", ""+next_reminder.getId());
            Bundle task_bundle = dbhandler.getTaskById(task_id);
            //use alarm manager and broadcast receiver here
            Calendar cal = Calendar.getInstance();
            Log.i("reminder day", ""+next_reminder.getDay());
            cal.set(next_reminder.getYear(), next_reminder.getMonth(), next_reminder.getDay());
            cal.set(Calendar.HOUR_OF_DAY, next_reminder.getHour());
            cal.set(Calendar.MINUTE, next_reminder.getMinute());
            cal.set(Calendar.SECOND, 0);

            Intent receiver_intent = new Intent(context, NotificationReceiver.class);
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

            //check for audio
            if(next_reminder.getWith_audio() == 1){
                //with audio
                String uri = dbhandler.getUriFromCategory(task_bundle.getString("category"));
                receiver_intent.putExtra("audio_uri", uri);
            }

            PendingIntent pending_intent = PendingIntent.getBroadcast(context, requestCode, receiver_intent, PendingIntent.FLAG_ONE_SHOT);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                //see documentation: beginning from kitkat, setexact needs to be used if alarm is to be delivered at exact time
                alarm_mgr.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pending_intent);
            }
            else{
                alarm_mgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pending_intent);
            }
            Log.i("time", cal.getTime().toString());
            Log.i("current", Calendar.getInstance().getTime().toString());
            requestCode = requestCode % Integer.MAX_VALUE + 1;
        }
        dbhandler.close();
    }
}
