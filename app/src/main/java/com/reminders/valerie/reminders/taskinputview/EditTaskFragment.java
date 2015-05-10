package com.reminders.valerie.reminders.taskinputview;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.Reminder;
import com.reminders.valerie.reminders.model.ReminderSorter;
import com.reminders.valerie.reminders.model.Task;
import com.reminders.valerie.reminders.notificationservice.IdGenerator;
import com.reminders.valerie.reminders.notificationservice.NotificationReceiver;
import com.reminders.valerie.reminders.scheduleview.ExistingScheduleFragment;
import com.reminders.valerie.reminders.TaskDBHandler;

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
        category = args.getString("category");
        importance = args.getDouble("importance");
        setTaskContents();
        getReminders(task);
        if(reminder_list == null || reminder_list.size() == 0){
            final Calendar cal = Calendar.getInstance();
            rem_year = cal.get(Calendar.YEAR);
            rem_month = cal.get(Calendar.MONTH);
            rem_day = cal.get(Calendar.DAY_OF_MONTH);
            rem_hour = cal.get(Calendar.HOUR_OF_DAY);
            rem_minute = cal.get(Calendar.MINUTE);
        }
        else{
            Reminder rem = reminder_list.get(0);
            rem_year = rem.getYear();
            rem_month = rem.getMonth();
            rem_day = rem.getDay();
            rem_hour = rem.getHour();
            rem_minute = rem.getMinute();
        }

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
        else{
            rem_date.setText(date_et_mgr.buildText(rem_year, rem_month, rem_day));
            rem_time.setText(time_et_mgr.buildText(rem_hour, rem_minute, 0));
        }

        //set category
        TaskDBHandler dbhandler = new TaskDBHandler(getActivity().getApplicationContext());
        Cursor cursor = dbhandler.getCategoryNames();
        int position = -1;
        category_spinner.setSelection(0);
        do{
            cursor.moveToNext();
            position++;
            String category_name = cursor.getString(cursor.getColumnIndex("_id"));
            Log.d("category name", category_name);
            Log.d("category", category);
            if(category.equals(category_name)){
                category_spinner.setSelection(position);
                break;
            }
        }while(!cursor.isLast());
        cursor.close();

        //setimportance
        if(importance == Task.IMPORTANCE_HIGH) importance_high.toggle();
        else if(importance == Task.IMPORTANCE_MEDIUM) importance_medium.toggle();
        else importance_low.toggle();
        completed_button.setOnClickListener(this);

        getReminders(task);
        deletion_list = new ArrayList<Reminder>();
        added_reminders = new ArrayList<Reminder>();
    }

    @Override
    public void getReminders(Task task) {
        //TODO RETRIEVE REMINDERS FROM DB
        TaskDBHandler dbhandler = new TaskDBHandler(getActivity().getApplicationContext());
        reminder_list = dbhandler.getUnfiredReminders(task.getTask_id());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_task_button:
                try {
                    setTaskContents();
                    Reminder next_reminder = buildReminder(task);
                    next_reminder.setTask_id(task.getTask_id());
                    Reminder last_rem = new Reminder();
                    last_rem.setYear(task.getYear());
                    last_rem.setMonth(task.getMonth());
                    last_rem.setDay(task.getDay());
                    last_rem.setHour(task.getHour());
                    last_rem.setMinute(task.getMinute());
                    if(ReminderSorter.is_after(next_reminder, last_rem) == -1){
                        throw new Exception("The first reminder should not be after your task");
                    }
                    //TODO RETRIEVE REMAINING REMINDERS
                    ExistingScheduleFragment schedule_fragment = new ExistingScheduleFragment();
                    if(reminder_list.size() > 0){
                        //get next reminder time
                        while(ReminderSorter.is_after(reminder_list.get(0), next_reminder) == 1){
                            deletion_list.add(reminder_list.get(0));
                            reminder_list.remove(0);
                        }
                        if(ReminderSorter.is_after(reminder_list.get(0), next_reminder) == -1){
                            reminder_list.add(0, next_reminder);
                            added_reminders.add(next_reminder);
                        }
                        //delete reminders after new task time
                        int index = reminder_list.size()-1;
                        //set last reminder to task time
                        Reminder last_reminder = reminder_list.get(index);
                        last_reminder.setYear(task.getYear());
                        last_reminder.setMonth(task.getMonth());
                        last_reminder.setDay(task.getDay());
                        last_reminder.setHour(task.getHour());
                        last_reminder.setMinute(task.getMinute());
                        index--;
                        while(ReminderSorter.is_after(last_reminder, reminder_list.get(index)) == 1){
                            deletion_list.add(reminder_list.get(index));
                            reminder_list.remove(index);
                        }
                    }
                    //add all three lists to schedule_fragment
                    schedule_fragment.setReminderArrayList(reminder_list);
                    schedule_fragment.setDeletionList(deletion_list);
                    schedule_fragment.setAddedList(added_reminders);
                    schedule_fragment.setTask(task);
                    FragmentTransaction fragment_transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragment_transaction.add(R.id.task_fragment_container, schedule_fragment, null);
                    fragment_transaction.addToBackStack(null);
                    fragment_transaction.hide(this);
                    fragment_transaction.commit();
                }
                catch(Exception e){
                    Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            case R.id.completed_button:
                TaskDBHandler dbhandler = new TaskDBHandler(getActivity().getApplicationContext());
                task.setCompleted(1);
                //remove pending intent
                Reminder next_reminder = dbhandler.getNextReminder(task.getTask_id());
                if(next_reminder!=null) {
                    Intent notifcation_intent = new Intent(getActivity().getApplicationContext(), NotificationReceiver.class);
                    PendingIntent pending_intent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), IdGenerator.generateID(task.getTask_id(), next_reminder.getId()), notifcation_intent, PendingIntent.FLAG_ONE_SHOT);
                    if (pending_intent != null) {
                        pending_intent.cancel();
                    }
                }
                if(!dbhandler.markTaskAsComplete(task)){
                    Toast.makeText(getActivity().getApplicationContext(), "Failed to mark this task as complete", Toast.LENGTH_SHORT).show();
                    getActivity().setResult(Activity.RESULT_CANCELED);
                }
                else {
                    TodoFragment todo_fragment = (TodoFragment)getActivity().getSupportFragmentManager().findFragmentById(R.layout.todo_fragment);
                    if(todo_fragment != null){
                        try {
                            todo_fragment.updateTaskList();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    getActivity().setResult(getActivity().RESULT_OK);
                }
                dbhandler.close();
                getActivity().finish();
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    private void setTaskContents(){
        if(task == null){
            task = new Task();
        }
        task.setTitle(task_title.getText().toString());
        task.setYear(task_year);
        task.setHour(task_hour);
        task.setMinute(task_minute);
        task.setMonth(task_month);
        task.setDay(task_day);
        task.setTask_id(task_id);
        task.setSame_rem_task(same_datetime.isChecked() ? 1 : 0);
        task.setCompleted(0);
        task.setImportance(importance);
        task.setCategory(category);
    }

    private boolean taskDateTimeUnchanged(){
        if (task.getYear() == task_year && task.getMonth() == task_month && task.getDay() == task_day &&
                task.getHour() == task_hour && task.getMinute() == task_minute){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean reminderDateTimeUnchanged(Reminder reminder){
        if(reminder.getYear() == rem_year && reminder.getMonth() == rem_month && reminder.getDay() == rem_day &&
                reminder.getHour() == rem_hour && reminder.getMinute() == rem_minute){
            return true;
        }
        else{
            return false;
        }
    }
}
