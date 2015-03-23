package com.reminders.valerie.reminders.scheduleservice;

import android.content.Context;
import java.util.Calendar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.reminders.valerie.reminders.model.Reminder;
import com.reminders.valerie.reminders.model.ReminderNotification;

public class ScheduleClient {

    private ScheduleService bound_service;
    private Context app_context;
    private boolean is_bound;

    private ServiceConnection connection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName class_name, IBinder svc){
            bound_service = ((ScheduleService.ServiceBinder) svc).getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName class_name){
            bound_service = null;
        }
    };

    public ScheduleClient(Context applicationContext) {
        app_context = applicationContext;
    }


    public void doBindService(){
        app_context.bindService(new Intent(app_context, ScheduleService.class), connection, Context.BIND_AUTO_CREATE);
        is_bound = true;
    }

    public void setAlarmForNotification(Calendar c, ReminderNotification reminder){
        bound_service.setAlarm(c, reminder);
    }

    public void doUnbindService(){
        if(is_bound){
            app_context.unbindService(connection);
            is_bound = false;
        }
    }
}
