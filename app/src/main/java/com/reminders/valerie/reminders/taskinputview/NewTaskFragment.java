package com.reminders.valerie.reminders.taskinputview;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.Reminder;
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
                //call calculator open schedule fragment
                ArrayList<Reminder> reminder_list = setDummyData();
                ScheduleFragment schedule_fragment = new ScheduleFragment();
                schedule_fragment.setReminderArrayList(reminder_list);
                Task task = new Task();
                task.setTitle(task_title.getText().toString());
                task.setYear(task_year);
                task.setMonth(task_month);
                task.setDay(task_day);
                task.setHour(task_hour);
                task.setMinute(task_minute);
                task.setCompleted(0);
                task.setSame_rem_task(same_datetime.isChecked()? 1 : 0);
                schedule_fragment.setTask(task);
                FragmentTransaction fragment_transaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragment_transaction.add(R.id.task_fragment_container, schedule_fragment, null);
                fragment_transaction.addToBackStack(null);
                fragment_transaction.hide(this);
                fragment_transaction.commit();
                break;
            default:
                super.onClick(v);
        }
    }



}
