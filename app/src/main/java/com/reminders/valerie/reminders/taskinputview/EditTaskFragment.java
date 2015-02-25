package com.reminders.valerie.reminders.taskinputview;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.Reminder;
import com.reminders.valerie.reminders.model.ScheduleCalculator;
import com.reminders.valerie.reminders.model.Task;
import com.reminders.valerie.reminders.scheduleview.ExistingScheduleFragment;
import com.reminders.valerie.reminders.scheduleview.NewScheduleFragment;
import com.reminders.valerie.reminders.scheduleview.ScheduleFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class EditTaskFragment extends TaskInputFragment {
    @Override
    public void setContents() {
        Bundle args = getArguments();
        task_hour = args.getInt("task_hour");
        task_day = args.getInt("task_day");
        task_minute = args.getInt("task_minute");
        task_month = args.getInt("task_month");
        task_year = args.getInt("task_year");

        //temp
        final Calendar cal = Calendar.getInstance();
        rem_year = cal.get(Calendar.YEAR);
        rem_month = cal.get(Calendar.MONTH);
        rem_day = cal.get(Calendar.DAY_OF_MONTH);
        rem_hour = cal.get(Calendar.HOUR_OF_DAY);
        rem_minute = cal.get(Calendar.MINUTE);

        reminder_header.setText(getActivity().getResources().getText(R.string.next_reminder_details));
        task_title.setText(args.getString("title"));

        task_date.setText(date_et_mgr.buildText(task_year, task_month, task_day));
        task_time.setText(time_et_mgr.buildText(task_hour, task_minute, 0));

        if(args.getInt("same_datetime") == 1){
            same_datetime.setChecked(true);
            reminder_header.setVisibility(View.GONE);
            reminder_header_underline.setVisibility(View.GONE);
            rem_time.setVisibility(View.GONE);
            rem_date.setVisibility(View.GONE);
            rem_day = task_day;
            rem_year = task_year;
            rem_month = task_month;
            rem_hour = task_hour;
            rem_minute = task_minute;
        }

    }

    @Override
    public void getReminders(Task task) {
        //TODO RETRIEVE REMINDERS FROM DB
    }

    @Override
    public Reminder buildReminder(Task task) throws Exception {
        //TODO RETRIEVE NEXT REMINDER

        //temp
        Reminder reminder = new Reminder();
        reminder.setYear(task.getYear());
        reminder.setMonth(task.getMonth());
        reminder.setDay(task.getDay());
        reminder.setHour(task.getHour());
        reminder.setMinute(task.getMinute());
        reminder.setWith_audio(0);
        reminder.setTask(task);
        return reminder;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_task_button:
                try {
                    Task task = buildTask();
                    Reminder reminder = buildReminder(task);
                    //TODO RETRIEVE REMAINING REMINDERS

                    //temp
                    ArrayList<Reminder> reminder_list = ScheduleCalculator.buildReminderList(task, reminder);

                    NewScheduleFragment schedule_fragment = new ExistingScheduleFragment();
                    schedule_fragment.setReminderArrayList(reminder_list);
                    schedule_fragment.setTask(task);
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
                break;
        }
    }


}
