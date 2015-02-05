package com.reminders.valerie.reminders.myroutine;


import android.os.Bundle;
import android.view.View;

import com.reminders.valerie.reminders.R;

import java.util.Calendar;

public class EditEventFragment extends EventInputFragment {

    private Bundle args;

    public void setArgs(Bundle args){
        this.args = args;
    }

    @Override
    public void setContents() {
        getAction_header().setText(getActivity().getResources().getText(R.string.edit_event));

        //set title
        getEvent_title().setText(args.getString("title"));

        //set hours and minutes
        setStart_hour(args.getInt("start_hour"));
        setStart_minute(args.getInt("start_minute"));
        setEnd_hour(args.getInt("end_hour"));
        setEnd_minute(args.getInt("end_minute"));
        getStart_time().setText(getTime_et_mgr().buildText(getStart_hour(), getStart_minute(), 0));
        getEnd_time().setText(getTime_et_mgr().buildText(getEnd_hour(), getEnd_minute(), 0));

        getDelete_button().setVisibility(View.VISIBLE);
        getButton_space().setVisibility(View.VISIBLE);
        getDelete_button().setClickable(true);

        //set complexity and environment
    }
}
