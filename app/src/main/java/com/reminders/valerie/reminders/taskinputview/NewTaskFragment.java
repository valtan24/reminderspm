package com.reminders.valerie.reminders.taskinputview;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.Reminder;
import com.reminders.valerie.reminders.model.ScheduleCalculator;
import com.reminders.valerie.reminders.model.Task;
import com.reminders.valerie.reminders.scheduleview.ScheduleFragment;

import java.util.ArrayList;
import java.util.Calendar;


public class NewTaskFragment extends TaskInputFragment{

    @Override
    public void setContents() {
        final Calendar cal = Calendar.getInstance();
        task_year = cal.get(Calendar.YEAR);
        task_month = cal.get(Calendar.MONTH);
        task_day = cal.get(Calendar.DAY_OF_MONTH);
        task_hour = cal.get(Calendar.HOUR_OF_DAY);
        task_minute = cal.get(Calendar.MINUTE);
        rem_year = cal.get(Calendar.YEAR);
        rem_month = cal.get(Calendar.MONTH);
        rem_day = cal.get(Calendar.DAY_OF_MONTH);
        rem_hour = cal.get(Calendar.HOUR_OF_DAY);
        rem_minute = cal.get(Calendar.MINUTE);
        reminder_header.setText(getActivity().getResources().getText(R.string.first_reminder_details));

        completed_button.setVisibility(View.GONE);
        button_gap.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_task_button:
                ScheduleFragment schedule_fragment = new ScheduleFragment();
                try{
                    Task task = buildTask();
                    Reminder reminder = buildReminder(task);
                    ArrayList<Reminder> reminder_list = ScheduleCalculator.buildReminderList(task, reminder);
                    schedule_fragment.setTask(task);
                    schedule_fragment.setReminderArrayList(reminder_list);
                    FragmentTransaction fragment_transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragment_transaction.add(R.id.task_fragment_container, schedule_fragment, null);
                    fragment_transaction.addToBackStack(null);
                    fragment_transaction.hide(this);
                    fragment_transaction.commit();
                }
                catch(Exception e){
                    Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onClick(v);
        }
    }

    @Override
    public void getReminders(Task task){

    }

    @Override
    public Reminder buildReminder(Task task) throws Exception{
        Reminder reminder = new Reminder();
        if(same_datetime.isChecked()){
            if(task_date.getText() == null || task_date.getText().equals("")){
                throw new Exception("Please enter a date for your task");
            }
            if(task_time.getText() == null || task_time.getText().equals("")){
                throw new Exception("Please enter a time for your task");
            }
            reminder.setYear(task.getYear());
            reminder.setTask(task);
            reminder.setYear(task.getYear());
            reminder.setMonth(task.getMonth());
            reminder.setDay(task.getDay());
            reminder.setWith_audio(1); //TODO CHECK ROUTINE BEFORE ADDING AUDIO
            reminder.setMinute(task.getMinute());
            reminder.setHour(task.getHour());
        }
        else{
            if(rem_date.getText() == null || rem_date.getText().equals("")){
                throw new Exception("Please enter a date for your first reminder");
            }
            if(rem_time.getText() == null || rem_time.getText().equals("")){
                throw new Exception("Please enter a time for your first reminder");
            }
            reminder.setYear(rem_year);
            reminder.setMonth(rem_month);
            reminder.setDay(rem_day);
            reminder.setHour(rem_hour);
            reminder.setMinute(rem_minute);
        }
        return reminder;
    }

}
