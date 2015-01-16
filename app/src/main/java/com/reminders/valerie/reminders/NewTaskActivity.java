package com.reminders.valerie.reminders;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;


public class NewTaskActivity extends ActionBarActivity {

    private TimePicker time_picker;
    private Button time_button;
    private int task_hour, task_minute;

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

        time_button = (Button) findViewById(R.id.button_task_time);
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
