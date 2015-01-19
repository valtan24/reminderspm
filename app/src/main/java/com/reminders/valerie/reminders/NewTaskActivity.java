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
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.Calendar;


public class NewTaskActivity extends ActionBarActivity {

    //Navigation bar items

    private TimePicker time_picker;
    private Button time_button;
    private int task_hour, task_minute;

    //date picker items
    private Button date_button;
    private DateButtonManager date_button_mgr;
    private TimeButtonManager time_button_mgr;
    private int task_day, task_month, task_year;
    private String task_month_name;

    DatePickerDialog.OnDateSetListener date_set_listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            task_day = dayOfMonth;
            task_month = monthOfYear;
            task_year = year;
            if(date_button_mgr != null) {
                String date_text = date_button_mgr.buildButtonText(task_year, task_month, task_day);
                date_button_mgr.setButtonText(date_button, date_text);
            }
            else{
                Toast.makeText(getApplicationContext(), "date_button_mgr is null", Toast.LENGTH_SHORT);
            }
        }
    };


    TimePickerDialog.OnTimeSetListener time_set_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
            task_hour = hourOfDay;
            task_minute = minuteOfHour;
            if(time_button_mgr != null){
                String time_text = time_button_mgr.buildButtonText(task_hour, task_minute, 0);
                time_button_mgr.setButtonText(time_button, time_text);
            }
            else{
                Toast.makeText(getApplicationContext(), "time_button_mgr is null", Toast.LENGTH_SHORT);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        //date button
        date_button_mgr = new DateButtonManager();
        date_button = (Button) findViewById(R.id.task_date_button);
        date_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Bundle date_args = new Bundle();
                date_args.putInt("year", task_year);
                date_args.putInt("month", task_month);
                date_args.putInt("day", task_day);
                date_button_mgr.showPickerFragment(getSupportFragmentManager(), date_set_listener, date_args);
            }
        });
        task_year = Calendar.getInstance().get(Calendar.YEAR);
        task_month = Calendar.getInstance().get(Calendar.MONTH);
        task_day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String date_text = date_button_mgr.buildButtonText(task_year, task_month, task_day);
        date_button_mgr.setButtonText(date_button, date_text);

        //time button
        time_button = (Button) findViewById(R.id.task_time_button);
        time_button_mgr = new TimeButtonManager();
        time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle time_args = new Bundle();
                time_args.putInt("task_hour", task_hour);
                time_args.putInt("task_minute", task_minute);
                time_button_mgr.showPickerFragment(getSupportFragmentManager(), time_set_listener, time_args);
            }
        });
        task_hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        task_minute = Calendar.getInstance().get(Calendar.MINUTE);
        time_button_mgr.setButtonText(time_button, time_button_mgr.buildButtonText(task_hour, task_minute, 0));

    }

}
