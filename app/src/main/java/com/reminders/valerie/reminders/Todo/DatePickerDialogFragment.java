package com.reminders.valerie.reminders.Todo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;


public class DatePickerDialogFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener date_set_listener;
    private int year, month, day;

    public DatePickerDialogFragment() {
    }

    public void setCallBack(DatePickerDialog.OnDateSetListener date_set_listener) {
        this.date_set_listener = date_set_listener;
    }


    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), date_set_listener, year, month, day);
    }

}
