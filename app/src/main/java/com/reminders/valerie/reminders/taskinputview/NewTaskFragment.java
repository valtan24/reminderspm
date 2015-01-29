package com.reminders.valerie.reminders.taskinputview;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.reminders.valerie.reminders.model.DateEditTextManager;
import com.reminders.valerie.reminders.model.DateTimeEditTextMgr;
import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.Reminder;
import com.reminders.valerie.reminders.model.ScheduleCalculator;
import com.reminders.valerie.reminders.model.Task;
import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.TimeEditTextManager;
import com.reminders.valerie.reminders.scheduleview.ScheduleFragment;

import java.util.ArrayList;
import java.util.Calendar;


public class NewTaskFragment extends Fragment implements View.OnClickListener{

    TaskDBHandler dbhandler;

    //task details
    private int task_hour, task_minute;
    private int task_day, task_month, task_year;
    private Spinner category_spinner;
    private DateTimeEditTextMgr date_edittext_mgr, time_edittext_mgr;

    //UI
    private EditText task_title_edittext, task_time_edittext, task_date_edittext;;
    private Button continue_task_button;

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
                Toast.makeText(getActivity().getApplicationContext(), "Error setting date", Toast.LENGTH_SHORT);
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
                Toast.makeText(getActivity().getApplicationContext(), "Error setting time", Toast.LENGTH_SHORT);
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
                Toast.makeText(getActivity().getApplicationContext(), "Error setting date", Toast.LENGTH_SHORT);
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
                Toast.makeText(getActivity().getApplicationContext(), "Error setting time", Toast.LENGTH_SHORT);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.task_input_fragment, container, false);
        ActionBar actionbar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
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
        task_title_edittext = (EditText) rootView.findViewById(R.id.task_title_edittext);

        //edittext managers
        date_edittext_mgr = new DateEditTextManager();
        time_edittext_mgr = new TimeEditTextManager();

        //task date
        task_date_edittext = (EditText) rootView.findViewById(R.id.task_date_edittext);
        task_date_edittext.setClickable(true);
        task_date_edittext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle date_args = new Bundle();
                date_args.putInt("year", task_year);
                date_args.putInt("month", task_month);
                date_args.putInt("day", task_day);
                date_edittext_mgr.showPickerFragment(getActivity().getSupportFragmentManager(), task_date_listener, date_args);
            }
        });

        //task time
        task_time_edittext = (EditText) rootView.findViewById(R.id.task_time_edittext);
        task_time_edittext.setClickable(true);
        task_time_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle time_args = new Bundle();
                time_args.putInt("hour", task_hour);
                time_args.putInt("minute", task_minute);
                time_edittext_mgr.showPickerFragment(getActivity().getSupportFragmentManager(), task_time_listener, time_args);
            }
        });

        //reminder date
        reminder_date_edittext = (EditText) rootView.findViewById(R.id.first_rem_date_edittext);
        reminder_date_edittext.setClickable(true);
        reminder_date_edittext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle date_args = new Bundle();
                date_args.putInt("year", reminder_year);
                date_args.putInt("month", reminder_month);
                date_args.putInt("day", reminder_day);
                date_edittext_mgr.showPickerFragment(getActivity().getSupportFragmentManager(), reminder_date_listener, date_args);
            }
        });

        //reminder time
        reminder_time_edittext = (EditText) rootView.findViewById(R.id.first_rem_time_edittext);
        reminder_time_edittext.setClickable(true);
        reminder_time_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle time_args = new Bundle();
                time_args.putInt("hour", reminder_hour);
                time_args.putInt("minute", reminder_minute);
                time_edittext_mgr.showPickerFragment(getActivity().getSupportFragmentManager(), reminder_time_listener, time_args);
            }
        });

        //category spinner
        category_spinner = (Spinner) rootView.findViewById(R.id.task_category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.category_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(adapter);

        //save task button
        continue_task_button = (Button) rootView.findViewById(R.id.continue_task_button);
        continue_task_button.setOnClickListener(this);

        //cancel task button
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.continue_task_button) {
            ArrayList<Reminder> reminder_list = setDummyData();
            ScheduleFragment schedule_fragment = new ScheduleFragment();
            schedule_fragment.setReminderArrayList(reminder_list);
            FragmentTransaction fragment_transaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragment_transaction.add(R.id.task_fragment_container, schedule_fragment, null);
            fragment_transaction.addToBackStack(null);
            fragment_transaction.hide(this);
            fragment_transaction.commit();
        }
    }

    private ArrayList<Reminder> setDummyData(){
        Task new_task =  new Task();
        new_task.setTitle(task_title_edittext.getText().toString());
        new_task.setDay(task_day);
        new_task.setYear(task_year);
        new_task.setMonth(task_month);
        new_task.setCompleted(0);
        ScheduleCalculator.getInstance().setTask(new_task);
        //create 3 reminders
        ArrayList<Reminder> reminder_list = new ArrayList<Reminder>();
        //1st reminder
        Reminder rem_1 = new Reminder();
        rem_1.setDay(reminder_day);
        rem_1.setMonth(reminder_month);
        rem_1.setYear(reminder_year);
        rem_1.setHour(reminder_hour);
        rem_1.setMinute(reminder_minute);
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
}
