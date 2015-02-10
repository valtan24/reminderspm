package com.reminders.valerie.reminders.taskinputview;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.DateEditTextManager;
import com.reminders.valerie.reminders.model.DateTimeEditTextMgr;
import com.reminders.valerie.reminders.model.Reminder;
import com.reminders.valerie.reminders.model.ScheduleCalculator;
import com.reminders.valerie.reminders.model.Task;
import com.reminders.valerie.reminders.model.TimeEditTextManager;
import com.reminders.valerie.reminders.scheduleview.ScheduleFragment;

import java.util.ArrayList;


public abstract class TaskInputFragment extends Fragment implements View.OnClickListener{
    public int task_hour, task_minute, task_day, task_month, task_year;
    public int rem_hour, rem_minute, rem_day, rem_month, rem_year;
    public Spinner category_spinner;
    public DateTimeEditTextMgr date_et_mgr, time_et_mgr;
    public EditText task_title, task_time, task_date, rem_time, rem_date;
    private Button continue_button;
    public TextView reminder_header;
    public Button completed_button;
    public View button_gap;

    DatePickerDialog.OnDateSetListener task_date_listener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            task_day = dayOfMonth;
            task_month = monthOfYear;
            task_year = year;
            if(date_et_mgr != null) {
                String date_text = date_et_mgr.buildText(task_year, task_month, task_day);
                date_et_mgr.setText(task_date, date_text);
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), "Error setting date", Toast.LENGTH_SHORT);
            }
        }
    };

    TimePickerDialog.OnTimeSetListener task_time_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
            task_hour = hourOfDay;
            task_minute = minuteOfHour;
            if(time_et_mgr != null){
                String time_text = time_et_mgr.buildText(task_hour, task_minute, 0);
                time_et_mgr.setText(task_time, time_text);
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), "Error setting time", Toast.LENGTH_SHORT);
            }
        }
    };

    DatePickerDialog.OnDateSetListener reminder_date_listener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            rem_day = dayOfMonth;
            rem_month = monthOfYear;
            rem_year = year;
            if(date_et_mgr != null) {
                String date_text = date_et_mgr.buildText(rem_year, rem_month, rem_day);
                date_et_mgr.setText(rem_date, date_text);
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), "Error setting date", Toast.LENGTH_SHORT);
            }
        }
    };

    TimePickerDialog.OnTimeSetListener reminder_time_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
            rem_hour = hourOfDay;
            rem_minute = minuteOfHour;
            if(time_et_mgr != null){
                String time_text = time_et_mgr.buildText(rem_hour, rem_minute, 0);
                time_et_mgr.setText(rem_time, time_text);
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), "Error setting time", Toast.LENGTH_SHORT);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.task_input_fragment, container, false);
        ActionBar actionbar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        task_title = (EditText) rootView.findViewById(R.id.task_title_edittext);
        date_et_mgr = new DateEditTextManager();
        time_et_mgr = new TimeEditTextManager();

        //task date
        task_date = (EditText) rootView.findViewById(R.id.task_date_edittext);
        task_date.setClickable(true);
        task_date.setOnClickListener(this);

        //task time
        task_time = (EditText) rootView.findViewById(R.id.task_time_edittext);
        task_time.setClickable(true);
        task_time.setOnClickListener(this);

        //reminder date
        rem_date = (EditText) rootView.findViewById(R.id.first_rem_date_edittext);
        rem_date.setClickable(true);
        rem_date.setOnClickListener(this);

        //reminder time
        rem_time = (EditText) rootView.findViewById(R.id.first_rem_time_edittext);
        rem_time.setClickable(true);
        rem_time.setOnClickListener(this);

        //category spinner
        category_spinner = (Spinner) rootView.findViewById(R.id.task_category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.category_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(adapter);

        //save task button
        continue_button = (Button) rootView.findViewById(R.id.continue_task_button);
        continue_button.setOnClickListener(this);

        reminder_header = (TextView) rootView.findViewById(R.id.reminder_header);

        completed_button = (Button) rootView.findViewById(R.id.completed_button);
        button_gap = rootView.findViewById(R.id.button_gap);
        setContents();
        return rootView;
    }

    @Override
    public void onClick(View v){
        Bundle args;
        switch(v.getId()) {
            case R.id.task_date_edittext:
                args = new Bundle();
                args.putInt("year", task_year);
                args.putInt("month", task_month);
                args.putInt("day", task_day);
                date_et_mgr.showPickerFragment(getActivity().getSupportFragmentManager(), task_date_listener, args);
                break;
            case R.id.task_time_edittext:
                args = new Bundle();
                args.putInt("hour", task_hour);
                args.putInt("minute", task_minute);
                time_et_mgr.showPickerFragment(getActivity().getSupportFragmentManager(), task_time_listener, args);
                break;
            case R.id.first_rem_date_edittext:
                args = new Bundle();
                args.putInt("year", rem_year);
                args.putInt("month", rem_month);
                args.putInt("day", rem_day);
                date_et_mgr.showPickerFragment(getActivity().getSupportFragmentManager(), reminder_date_listener, args);
                break;
            case R.id.first_rem_time_edittext:
                args = new Bundle();
                args.putInt("hour", rem_hour);
                args.putInt("minute", rem_minute);
                time_et_mgr.showPickerFragment(getActivity().getSupportFragmentManager(), reminder_time_listener, args);
                break;
        }
    }

    public Task buildTask(){
        Task new_task =  new Task();
        new_task.setTitle(task_title.getText().toString());
        new_task.setDay(task_day);
        new_task.setYear(task_year);
        new_task.setMonth(task_month);
        new_task.setCompleted(0);
        return new_task;
    }

    public ArrayList<Reminder> setDummyData(){
        Task new_task =  new Task();
        new_task.setTitle(task_title.getText().toString());
        new_task.setDay(task_day);
        new_task.setYear(task_year);
        new_task.setMonth(task_month);
        new_task.setCompleted(0);
        ScheduleCalculator.getInstance().setTask(new_task);
        //create 3 reminders
        ArrayList<Reminder> reminder_list = new ArrayList<Reminder>();
        //1st reminder
        Reminder rem_1 = new Reminder();
        rem_1.setDay(rem_day);
        rem_1.setMonth(rem_month);
        rem_1.setYear(rem_year);
        rem_1.setHour(rem_hour);
        rem_1.setMinute(rem_minute);
        rem_1.setWith_audio(0);
        reminder_list.add(rem_1);
        //2nd reminder
        int tmp = task_minute;
        while(tmp != 0) {
            Reminder rem_2 = new Reminder();
            rem_2.setDay(task_day);
            rem_2.setMonth(task_month);
            rem_2.setYear(task_year);
            rem_2.setMinute(tmp-1);
            rem_2.setHour(task_hour);
            rem_2.setWith_audio(1);
            reminder_list.add(rem_2);
            tmp -= 1;
        }

        //last reminder during task
        Reminder rem_3 = new Reminder();
        rem_3.setDay(task_day);
        rem_3.setMonth(task_month);
        rem_3.setYear(task_year);
        rem_3.setHour(task_hour);
        rem_3.setMinute(task_minute);
        rem_3.setWith_audio(1);
        reminder_list.add(rem_3);
        return reminder_list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract void setContents();

}
