package com.reminders.valerie.reminders.myroutine;


import android.view.View;

import java.util.Calendar;

public class NewEventFragment extends EventInputFragment{
    @Override
    public void setContents() {
        getAction_header().setText("New Event");

        //set hours and minutes
        final Calendar cal = Calendar.getInstance();
        setStart_hour(cal.get(Calendar.HOUR_OF_DAY));
        setStart_minute(cal.get(Calendar.MINUTE));
        setEnd_hour(cal.get(Calendar.HOUR_OF_DAY));
        setEnd_minute(cal.get(Calendar.MINUTE));

        getDelete_button().setVisibility(View.INVISIBLE);
        getDelete_button().setClickable(false);
    }
}
