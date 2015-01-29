package com.reminders.valerie.reminders.model;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.EditText;

import com.reminders.valerie.reminders.taskinputview.TimePickerDialogFragment;

public class TimeEditTextManager implements DateTimeEditTextMgr {

    @Override
    public String buildText(int arg1, int arg2, int arg3) {
        //arg1 = hour, arg2 = minute, arg3 is unused, can be used to indicate format
        String time_text = "";
        if(arg1 < 10){
            time_text = time_text + "0" + arg1;
        }
        else{
            time_text = time_text + arg1;
        }
        if(arg2< 10){
            time_text = time_text + ":0" + arg2;
        }
        else{
            time_text = time_text + ":" + arg2;
        }
        return time_text;
    }

    @Override
    public void setText(EditText edit_text, String text) {
        if(edit_text != null){
            edit_text.setText(text);
        }
    }

    @Override
    public void showPickerFragment(FragmentManager fragment_mgr, Object listener, Bundle args){
        TimePickerDialogFragment time_picker = new TimePickerDialogFragment();
        time_picker.setArguments(args);
        if(listener instanceof TimePickerDialog.OnTimeSetListener){
            time_picker.setCallBack( (TimePickerDialog.OnTimeSetListener) listener);
        }
        else{
            Log.d("error", "Unable to cast to ontimesetlistener");
        }
        time_picker.show(fragment_mgr, "dialog");
    }
}
