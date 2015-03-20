package com.reminders.valerie.reminders.myroutine;


import android.database.Cursor;
import android.util.Log;
import android.view.View;

import com.reminders.valerie.reminders.TaskDBHandler;

import java.util.Calendar;

public class NewEventFragment extends EventInputFragment{
    @Override
    public void setContents() {
        getAction_header().setText("New Activity");

        //set hours and minutes
        final Calendar cal = Calendar.getInstance();
        setStart_hour(cal.get(Calendar.HOUR_OF_DAY));
        setStart_minute(cal.get(Calendar.MINUTE));
        setEnd_hour(cal.get(Calendar.HOUR_OF_DAY));
        setEnd_minute(cal.get(Calendar.MINUTE));

        getDelete_button().setVisibility(View.GONE);
        getButton_space().setVisibility(View.GONE);
        getDelete_button().setClickable(false);
    }
}
