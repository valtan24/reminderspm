package com.reminders.valerie.reminders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class NewTaskActivity extends ActionBarActivity implements View.OnClickListener{

    TaskDBHandler dbhandler;

    private Button task_time_button;
    private int task_hour, task_minute;

    //date picker items
    private Button task_date_button;
    private DateButtonManager date_button_mgr;
    private TimeButtonManager time_button_mgr;
    private int task_day, task_month, task_year;


    private EditText task_title_edittext;
    private Button save_task_button;
    private Button cancel_task_button;

    DatePickerDialog.OnDateSetListener date_set_listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            task_day = dayOfMonth;
            task_month = monthOfYear;
            task_year = year;
            if(date_button_mgr != null) {
                String date_text = date_button_mgr.buildButtonText(task_year, task_month, task_day);
                date_button_mgr.setButtonText(task_date_button, date_text);
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
                time_button_mgr.setButtonText(task_time_button, time_text);
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

        //task title
        task_title_edittext = (EditText) findViewById(R.id.task_title_edittext);

        //date button
        date_button_mgr = new DateButtonManager();
        task_date_button = (Button) findViewById(R.id.task_date_button);
        task_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        date_button_mgr.setButtonText(task_date_button, date_text);

        //time button
        task_time_button = (Button) findViewById(R.id.task_time_button);
        time_button_mgr = new TimeButtonManager();
        task_time_button.setOnClickListener(new View.OnClickListener() {
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
        time_button_mgr.setButtonText(task_time_button, time_button_mgr.buildButtonText(task_hour, task_minute, 0));

        //save task button
        save_task_button = (Button) findViewById(R.id.save_task_button);
        save_task_button.setOnClickListener(this);

        //cancel task button
        cancel_task_button = (Button) findViewById(R.id.cancel_task_button);
        cancel_task_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == save_task_button.getId()) {
            Bundle new_task_args = new Bundle();
            new_task_args.putString("task_title", task_title_edittext.getText().toString());
            new_task_args.putString("task_date", task_day + "-" + (task_month + 1) + "-" + task_year);
            new_task_args.putString("task_time", time_button_mgr.buildButtonText(task_hour, task_minute, 0));
            dbhandler = new TaskDBHandler(this);
            dbhandler.addNewTask(new_task_args);

            setResult(RESULT_OK);
        }
        else{
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}
