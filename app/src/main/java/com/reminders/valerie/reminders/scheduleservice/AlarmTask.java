package com.reminders.valerie.reminders.scheduleservice;

import java.util.Calendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmTask implements Runnable {

    private final Calendar date;
    private final AlarmManager alarm_mgr;
    private final Context context;

    public AlarmTask(Context context, Calendar date) {
        this.context = context;
        this.alarm_mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;
    }

    @Override
    public void run() {
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        PendingIntent pending_intent = PendingIntent.getService(context, 0, intent, 0);
        //todo fix lost alarm when phone turns off and on again
        alarm_mgr.set(AlarmManager.RTC, date.getTimeInMillis(), pending_intent);
    }
}
