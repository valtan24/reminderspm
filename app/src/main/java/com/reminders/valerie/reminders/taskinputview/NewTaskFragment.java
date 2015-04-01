package com.reminders.valerie.reminders.taskinputview;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.Reminder;
import com.reminders.valerie.reminders.model.ScheduleCalculator;
import com.reminders.valerie.reminders.model.Task;
import com.reminders.valerie.reminders.scheduleview.NewScheduleFragment;
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
                //call calculator open schedule fragment

                NewScheduleFragment schedule_fragment = new NewScheduleFragment();
                try{
                    Task task = buildTask();
                    Reminder reminder = buildReminder(task);
                    ScheduleCalculator calculator = ScheduleCalculator.getInstance(getActivity().getApplicationContext());
                    ArrayList<Reminder> reminder_list = calculator.buildReminderList(task, reminder);
                    schedule_fragment.setTask(task);
                    schedule_fragment.setReminderArrayList(reminder_list);
                    calculator.killInstance();
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


}
