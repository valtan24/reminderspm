package com.reminders.valerie.reminders.scheduleservice;

import java.util.Calendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.reminders.valerie.reminders.model.Reminder;
import com.reminders.valerie.reminders.model.ReminderNotification;

public class AlarmTask implements Runnable {

    private final Calendar date;
    private final AlarmManager alarm_mgr;
    private final Context context;
    private final ReminderNotification reminder;

    public AlarmTask(Context context, Calendar date, ReminderNotification reminder) {
        this.context = context;
        this.alarm_mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;
        this.reminder = reminder;
    }

    @Override
    public void run() {
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        intent.putExtra("reminder_id", reminder.getReminder_id());
        intent.putExtra("task_id", reminder.getTask_id());
        intent.putExtra("task_title", reminder.getTask_title());
        intent.putExtra("task_date", reminder.getDateString());
        intent.putExtra("task_time", reminder.getTimeString());

        PendingIntent pending_intent = PendingIntent.getService(context, 0, intent, 0);
        //todo fix lost alarm when phone turns off and on again
        alarm_mgr.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pending_intent);
    }
}
