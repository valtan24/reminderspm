package com.reminders.valerie.reminders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.DateFormatSymbols;


public class NewTaskActivity extends ActionBarActivity {

    //Navigation bar items

    private TimePicker time_picker;
    private Button time_button;
    private int task_hour, task_minute;

    //date picker items
    private Button date_button;
    private DateTimeButtonMgr date_button_mgr, time_button_mgr;
    private int task_day, task_month, task_year;
    private String task_month_name;

    DatePickerDialog.OnDateSetListener date_set_listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            task_day = dayOfMonth;
            task_month = monthOfYear;
            task_year = year;
            if(date_button_mgr != null) {
                String date_text = date_button_mgr.buildButtonText(task_day, task_month, task_year);
                date_button_mgr.setButtonText(date_button, date_text);
            }
            else{
                Log.d("null", "date button manager not found");
            }
        }
    };

    private void setDateButtonText() {
        task_month_name = new DateFormatSymbols().getMonths()[task_month];
        if(date_button != null){
            date_button.setText( task_day + " " + task_month_name + " " + task_year);
        }
    }

    TimePickerDialog.OnTimeSetListener time_set_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
            task_hour = hourOfDay;
            task_minute = minuteOfHour;
            setTimeButtonText();
        }
    };

    private String buildTimeText(){
        String time_text = "";
        if(task_hour < 10){
            time_text = time_text + "0" + task_hour;
        }
        else{
            time_text = time_text + task_hour;
        }
        if(task_minute < 10){
            time_text = time_text + ":0" + task_minute;
        }
        else{
            time_text = time_text + ":" + task_minute;
        }
        return time_text;
    }

    private void setTimeButtonText() {
        String time_text = buildTimeText();
        if(time_button != null){
            time_button.setText(time_text);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        date_button_mgr = new DateButtonManager();
        time_button = (Button) findViewById(R.id.task_time_button);
        time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        setTimeButtonText();

        //get current time
        task_hour = Time.HOUR;
        task_minute = Time.MINUTE;
    }

    private void showTimePicker(){
        TimePickerDialogFragment time_picker = new TimePickerDialogFragment();
        Bundle time_args = new Bundle();
        time_args.putInt("task_hour", task_hour);
        time_args.putInt("task_minute", task_minute);
        time_picker.setArguments(time_args);
        time_picker.setCallBack(time_set_listener);

        FragmentManager fragment_mgr = getSupportFragmentManager();
        time_picker.show(fragment_mgr, "dialog");
    }

}
