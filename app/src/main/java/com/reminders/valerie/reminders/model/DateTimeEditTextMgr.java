package com.reminders.valerie.reminders.model;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Button;
import android.widget.EditText;

public interface DateTimeEditTextMgr {

    public String buildText(int arg1, int arg2, int arg3);
    public void setText(EditText edit_text, String text);
    public void showPickerFragment(FragmentManager fragment_mgr, Object listener, Bundle args);
}
