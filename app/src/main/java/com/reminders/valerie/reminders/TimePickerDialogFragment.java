package com.reminders.valerie.reminders;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class TimePickerDialogFragment extends DialogFragment {

    TimePickerDialog.OnTimeSetListener time_set_listener;
    private int hour, minute;

    public TimePickerDialogFragment(){}

    public void setCallBack(TimePickerDialog.OnTimeSetListener time_set_listener){
        this.time_set_listener = time_set_listener;
    }

    @Override
    public void setArguments(Bundle args){
        super.setArguments(args);
        hour = args.getInt("hour");
        minute = args.getInt("minute");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return new TimePickerDialog(getActivity(), time_set_listener, hour, minute, true);
    }
}
