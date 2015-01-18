package com.reminders.valerie.reminders;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Button;

public interface DateTimeButtonMgr {

    public String buildButtonText(int arg1, int arg2, int arg3);
    public void setButtonText(Button button, String text);
    public void showPickerFragment(FragmentManager fragment_mgr, Object listener, Bundle args);
}
