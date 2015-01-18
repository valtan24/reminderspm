package com.reminders.valerie.reminders;

import android.widget.Button;

public interface DateTimeButtonMgr {

    public String buildButtonText(int arg1, int arg2, int arg3);
    public void setButtonText(Button button, String text);
}
