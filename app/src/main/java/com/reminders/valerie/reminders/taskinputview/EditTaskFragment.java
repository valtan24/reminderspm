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

    private long task_id;
    private Task task;
    private ArrayList<Reminder> deletion_list;
    private ArrayList<Reminder> reminder_list;
    private ArrayList<Reminder> added_reminders;

    @Override
    public void setContents() {
        Bundle args = getArguments();
        task_hour = args.getInt("task_hour");
        task_day = args.getInt("task_day");
        task_minute = args.getInt("task_minute");
        task_month = args.getInt("task_month");
        task_year = args.getInt("task_year");
        task_id = args.getLong("task_id");
        setTaskContents();

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
        getReminders(task);

    }

    @Override
    public void getReminders(Task task) {
        //TODO RETRIEVE REMINDERS FROM DB
        TaskDBHandler dbhandler = new TaskDBHandler(getActivity().getApplicationContext());
        reminder_list = dbhandler.getUnfiredReminders(task.getId());
    }

    @Override
    public Reminder buildReminder(Task task) throws Exception {
        //TODO RETRIEVE NEXT REMINDER, CHECK IF SAME AS INPUT
        /*if the next reminder is different from input, rebuild next reminder and recalculate schedule. retrieve reminders and add to deletion list
        if is the same, simply retrieve reminders*/

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
                    setTaskContents();
                    Reminder reminder = buildReminder(task);
                    //TODO RETRIEVE REMAINING REMINDERS
                    TaskDBHandler dbhandler = new TaskDBHandler(getActivity().getApplicationContext());
                    ExistingScheduleFragment schedule_fragment = new ExistingScheduleFragment();
                    //TODO UPDATE ARRAYLISTS
                    //set last reminder in reminder_list to be the same as task date and time
                    //add all three lists to schedule_fragment

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
            case R.id.same_datetime_checkbox:
                //TODO UPDATE REMINDER ARRAYLISTS
                if(((CheckBox) v).isChecked()){
                    //update last reminder to task date and time, move remaining to deletion_list
                }
                else{
                    //if task.getsame_rem_time is true, add new reminders into BOTH reminder_list and added_reminders
                    //if is false, move all reminders from deletion_list to reminder_list
                }
                super.onClick(v);
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    private void setTaskContents(){
        task.setYear(task_year);
        task.setHour(task_hour);
        task.setMinute(task_minute);
        task.setMonth(task_month);
        task.setDay(task_day);
        task.setId(task_id);
        task.setSame_rem_task(same_datetime.isChecked() ? 1 : 0);
    }
}
