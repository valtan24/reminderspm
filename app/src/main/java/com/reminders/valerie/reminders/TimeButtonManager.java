package com.reminders.valerie.reminders;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Button;

public class TimeButtonManager implements DateTimeButtonMgr{

    @Override
    public String buildButtonText(int arg1, int arg2, int arg3) {
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
    public void setButtonText(Button button, String text) {
        if(button != null){
            button.setText(text);
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
