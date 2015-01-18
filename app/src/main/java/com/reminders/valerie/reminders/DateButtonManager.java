package com.reminders.valerie.reminders;


import android.util.Log;
import android.widget.Button;

import java.text.DateFormatSymbols;

public class DateButtonManager implements DateTimeButtonMgr {
    @Override
    public String buildButtonText(int arg1, int arg2, int arg3) {
        //arg1  = year, arg2 = month, arg3 = day
        String month_name = new DateFormatSymbols().getMonths()[arg2];
        String date_text = arg3 + " " + month_name + " " + arg1;
        return date_text;
    }

    @Override
    public void setButtonText(Button button, String text){
        if(button != null){
            button.setText(text);
        }
        else{
            Log.d("null", "Button not found.");
        }
    }
}
