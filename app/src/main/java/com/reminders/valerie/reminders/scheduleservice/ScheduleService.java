package com.reminders.valerie.reminders.scheduleservice;

import java.util.Calendar;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.reminders.valerie.reminders.model.Reminder;
import com.reminders.valerie.reminders.model.ReminderNotification;

public class ScheduleService extends Service {
    public class ServiceBinder extends Binder{
        ScheduleService getService(){
            return ScheduleService.this;
        }
    }

    private final IBinder app_binder = new ServiceBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent){
        return app_binder;
    }


    public void setAlarm(Calendar c, ReminderNotification reminder){
        new AlarmTask(this, c, reminder).run();
    }
}
