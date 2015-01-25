package com.reminders.valerie.reminders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class NewTaskActivity extends ActionBarActivity implements View.OnClickListener{

    TaskDBHandler dbhandler;

    //task details
    private int task_hour, task_minute;
    private int task_day, task_month, task_year;
    private Spinner category_spinner;
    private DateTimeEditTextMgr date_edittext_mgr, time_edittext_mgr;

    //UI
    private EditText task_title_edittext, task_time_edittext, task_date_edittext;;
    private Button continue_task_button;
    private Button cancel_task_button;

    //reminder items
    private int reminder_day, reminder_month, reminder_year;
    private int reminder_hour, reminder_minute;
    private EditText reminder_date_edittext, reminder_time_edittext;


    DatePickerDialog.OnDateSetListener task_date_listener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            task_day = dayOfMonth;
            task_month = monthOfYear;
            task_year = year;
            if(date_edittext_mgr != null) {
                String date_text = date_edittext_mgr.buildText(task_year, task_month, task_day);
                date_edittext_mgr.setText(task_date_edittext, date_text);
            }
            else{
                Toast.makeText(getApplicationContext(), "Error setting date", Toast.LENGTH_SHORT);
            }
        }
    };

    TimePickerDialog.OnTimeSetListener task_time_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
            task_hour = hourOfDay;
            task_minute = minuteOfHour;
            if(time_edittext_mgr != null){
                String time_text = time_edittext_mgr.buildText(task_hour, task_minute, 0);
                time_edittext_mgr.setText(task_time_edittext, time_text);
            }
            else{
                Toast.makeText(getApplicationContext(), "Error setting time", Toast.LENGTH_SHORT);
            }
        }
    };

    //reminder listeners
    DatePickerDialog.OnDateSetListener reminder_date_listener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            reminder_day = dayOfMonth;
            reminder_month = monthOfYear;
            reminder_year = year;
            if(date_edittext_mgr != null) {
                String date_text = date_edittext_mgr.buildText(reminder_year, reminder_month, reminder_day);
                date_edittext_mgr.setText(reminder_date_edittext, date_text);
            }
            else{
                Toast.makeText(getApplicationContext(), "Error setting date", Toast.LENGTH_SHORT);
            }
        }
    };

    TimePickerDialog.OnTimeSetListener reminder_time_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
            reminder_hour = hourOfDay;
            reminder_minute = minuteOfHour;
            if(time_edittext_mgr != null){
                String time_text = time_edittext_mgr.buildText(reminder_hour, reminder_minute, 0);
                time_edittext_mgr.setText(reminder_time_edittext, time_text);
            }
            else{
                Toast.makeText(getApplicationContext(), "Error setting time", Toast.LENGTH_SHORT);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_input);

        //initialization of values
        final Calendar cal = Calendar.getInstance();
        task_year = cal.get(Calendar.YEAR);
        task_month = cal.get(Calendar.MONTH);
        task_day = cal.get(Calendar.DAY_OF_MONTH);
        task_hour = cal.get(Calendar.HOUR_OF_DAY);
        task_minute = cal.get(Calendar.MINUTE);
        reminder_year = cal.get(Calendar.YEAR);
        reminder_month = cal.get(Calendar.MONTH);
        reminder_day = cal.get(Calendar.DAY_OF_MONTH);
        reminder_hour = cal.get(Calendar.HOUR_OF_DAY);
        reminder_minute = cal.get(Calendar.MINUTE);

        //task title
        task_title_edittext = (EditText) findViewById(R.id.task_title_edittext);

        //edittext managers
        date_edittext_mgr = new DateEditTextManager();
        time_edittext_mgr = new TimeEditTextManager();

        //task date
        task_date_edittext = (EditText) findViewById(R.id.task_date_edittext);
        task_date_edittext.setClickable(true);
        task_date_edittext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle date_args = new Bundle();
                date_args.putInt("year", task_year);
                date_args.putInt("month", task_month);
                date_args.putInt("day", task_day);
                date_edittext_mgr.showPickerFragment(getSupportFragmentManager(), task_date_listener, date_args);
            }
        });

        //task time
        task_time_edittext = (EditText) findViewById(R.id.task_time_edittext);
        task_time_edittext.setClickable(true);
        task_time_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle time_args = new Bundle();
                time_args.putInt("hour", task_hour);
                time_args.putInt("minute", task_minute);
                time_edittext_mgr.showPickerFragment(getSupportFragmentManager(), task_time_listener, time_args);
            }
        });

        //reminder date
        reminder_date_edittext = (EditText) findViewById(R.id.first_rem_date_edittext);
        reminder_date_edittext.setClickable(true);
        reminder_date_edittext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle date_args = new Bundle();
                date_args.putInt("year", reminder_year);
                date_args.putInt("month", reminder_month);
                date_args.putInt("day", reminder_day);
                date_edittext_mgr.showPickerFragment(getSupportFragmentManager(), reminder_date_listener, date_args);
            }
        });

        //reminder time
        reminder_time_edittext = (EditText) findViewById(R.id.first_rem_time_edittext);
        reminder_time_edittext.setClickable(true);
        reminder_time_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle time_args = new Bundle();
                time_args.putInt("hour", reminder_hour);
                time_args.putInt("minute", reminder_minute);
                time_edittext_mgr.showPickerFragment(getSupportFragmentManager(), reminder_time_listener, time_args);
            }
        });

        //category spinner
        category_spinner = (Spinner) findViewById(R.id.task_category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(adapter);

        //save task button
        continue_task_button = (Button) findViewById(R.id.continue_task_button);
        continue_task_button.setOnClickListener(this);

        //cancel task button
        cancel_task_button = (Button) findViewById(R.id.cancel_task_button);
        cancel_task_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == continue_task_button.getId()) {
            //checks

            /*
            Bundle new_task_args = new Bundle();
            new_task_args.putString("task_title", task_title_edittext.getText().toString());
            new_task_args.putString("task_date", task_day + "-" + (task_month + 1) + "-" + task_year);
            new_task_args.putString("task_time", time_edittext_mgr.buildText(task_hour, task_minute, 0));
            dbhandler = new TaskDBHandler(this);
            dbhandler.addNewTask(new_task_args);

            setResult(RESULT_OK);*/
            Fragment schedule_fragment = new ScheduleFragment();
            this.getSupportFragmentManager().beginTransaction().replace(R.id.reminder_schedule_fragment, schedule_fragment, null).commit();

        }
        else{
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
