package com.reminders.valerie.reminders.model;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.EditText;

import com.reminders.valerie.reminders.taskinputview.DatePickerDialogFragment;

import java.text.DateFormatSymbols;

public class DateEditTextManager implements DateTimeEditTextMgr {
    @Override
    public String buildText(int arg1, int arg2, int arg3) {
        //arg1  = year, arg2 = month, arg3 = day
        String month_name = new DateFormatSymbols().getMonths()[arg2];
        String date_text = arg3 + " " + month_name + " " + arg1;
        return date_text;
    }

    @Override
    public void setText(EditText edit_text, String text){
        if(edit_text != null){
            edit_text.setText(text);
        }
        else{
            Log.d("null", "Button not found.");
        }
    }

    @Override
    public void showPickerFragment(FragmentManager fragment_mgr, Object listener, Bundle args){
        DatePickerDialogFragment date_picker = new DatePickerDialogFragment();
        date_picker.setArguments(args);
        if(listener instanceof DatePickerDialog.OnDateSetListener) {
            date_picker.setCallBack( (DatePickerDialog.OnDateSetListener) listener);
        }
        else{
            Log.d("error", "unable to case listener to ondatesetlistener");
        }
        date_picker.show(fragment_mgr, "dialog");
    }
}
