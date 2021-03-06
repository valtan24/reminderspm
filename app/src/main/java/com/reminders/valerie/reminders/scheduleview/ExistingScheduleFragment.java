package com.reminders.valerie.reminders.scheduleview;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.TaskDBHandler;

import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.Reminder;
import com.reminders.valerie.reminders.model.ReminderSorter;
import com.reminders.valerie.reminders.notificationservice.AlarmTask;
import com.reminders.valerie.reminders.notificationservice.DismissNotification;
import com.reminders.valerie.reminders.notificationservice.IdGenerator;
import com.reminders.valerie.reminders.notificationservice.NotificationReceiver;

import java.util.ArrayList;
import java.util.Calendar;

public class ExistingScheduleFragment extends ScheduleFragment {

    private ArrayList<Reminder> marked_for_deletion;
    private ArrayList<Reminder> added_reminders;

    public void setDeletionList(ArrayList<Reminder> deletion_list){
        marked_for_deletion = deletion_list;
    }

    public void setAddedList(ArrayList<Reminder> added_list){
        added_reminders = added_list;
    }

    DateTimeDialogFragment.OnDateTimeSetListener datetime_listener = new DateTimeDialogFragment.OnDateTimeSetListener() {
        @Override
        public void OnDateTimeSet(Bundle args) {
            Reminder tmp = new Reminder();
            tmp.setHour(args.getInt("hour"));
            tmp.setMinute(args.getInt("minute"));
            tmp.setYear(args.getInt("year"));
            tmp.setMonth(args.getInt("month"));
            tmp.setDay(args.getInt("day"));
            if(!notAfterTaskTime(tmp, task)){
                Toast.makeText(getActivity().getApplicationContext(), "Reminders should not be after your task", Toast.LENGTH_SHORT).show();
                tmp = null; //free memory
                return;
            }
            if(reminder_selected != null) {
                reminder_selected.setHour(args.getInt("hour"));
                reminder_selected.setMinute(args.getInt("minute"));
                reminder_selected.setYear(args.getInt("year"));
                reminder_selected.setMonth(args.getInt("month"));
                reminder_selected.setDay(args.getInt("day"));
                reminder_selected = null;
            }
            else{ //new reminder
                Reminder new_reminder = new Reminder();
                new_reminder.setHour(args.getInt("hour"));
                new_reminder.setMinute(args.getInt("minute"));
                new_reminder.setYear(args.getInt("year"));
                new_reminder.setMonth(args.getInt("month"));
                new_reminder.setDay(args.getInt("day"));
                if(added_reminders == null || added_reminders.size() == 0){
                    added_reminders = new ArrayList<Reminder>();
                }
                added_reminders.add(new_reminder);
                reminder_list.add(new_reminder);
            }
            reminder_list = ReminderSorter.merge_sort(reminder_list, 0, reminder_list.size()-1);
            list_adapter.set_reminder_list(reminder_list);
            list_adapter.notifyDataSetChanged();
        }
    };

    DeleteDialogFragment.OnDeleteSetListener delete_listener = new DeleteDialogFragment.OnDeleteSetListener(){
        @Override
        public void OnDeleteSet(int choice) {
            if(reminder_selected != null) {
                switch (choice) {
                    case 0: //cancel
                        //do nothing;
                        break;
                    case 1:
                        if(marked_for_deletion == null || marked_for_deletion.size() == 0){
                            marked_for_deletion = new ArrayList<Reminder>();
                        }
                        if(reminder_selected.getId() != 0){ //existst in database
                            marked_for_deletion.add(reminder_selected);
                        }
                        reminder_list.remove(reminder_selected);
                        list_adapter.notifyDataSetChanged();
                        break;
                    default:
                        Log.d("Error", "invalid choice");
                }
                reminder_selected = null;
            }
            else{
                Log.d("Error", "No reminder selected");
            }
        }
    };

    ReminderDialog.OnActionSelectedListener action_listener = new ReminderDialog.OnActionSelectedListener() {
        @Override
        public void onActionSelected(int position) {
            if (reminder_selected != null) {
                switch (position) {
                    case 0: // add/remove audio
                        int tmp = reminder_selected.getWith_audio();
                        tmp = tmp + (int) Math.pow(-1, tmp);
                        reminder_selected.setWith_audio(tmp);
                        list_adapter.notifyDataSetChanged();
                        break;
                    case 1: // edit reminder date and time
                        Bundle args = new Bundle();
                        args.putInt("year", reminder_selected.getYear());
                        args.putInt("month", reminder_selected.getMonth());
                        args.putInt("day", reminder_selected.getDay());
                        args.putInt("hour", reminder_selected.getHour());
                        args.putInt("minute", reminder_selected.getMinute());
                        args.putString("title", getActivity().getResources().getText(R.string.change_datetime_header).toString());
                        DateTimeDialogFragment datetime_fragment = new DateTimeDialogFragment();
                        datetime_fragment.setArguments(args);
                        datetime_fragment.setCallBack(datetime_listener);
                        datetime_fragment.show(getActivity().getSupportFragmentManager(), "dialog");
                        break;
                    case 2:
                        DeleteDialogFragment delete_fragment = new DeleteDialogFragment();
                        delete_fragment.setTitle("Delete Reminder");
                        delete_fragment.setCallBack(delete_listener);
                        delete_fragment.show(getActivity().getSupportFragmentManager(), "dialog");
                        break;
                    default:
                        Toast.makeText(getActivity().getApplicationContext(), "Invalid option", Toast.LENGTH_SHORT);
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.save_task_button:
                TaskDBHandler dbhandler = new TaskDBHandler(getActivity().getApplicationContext());
                Reminder next_reminder = dbhandler.getNextReminder(task.getTask_id());
                if(next_reminder!=null) {
                    Intent notifcation_intent = new Intent(getActivity().getApplicationContext(), NotificationReceiver.class);
                    PendingIntent pending_intent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), IdGenerator.generateID(task.getTask_id(), next_reminder.getId()), notifcation_intent, PendingIntent.FLAG_ONE_SHOT);
                    if (pending_intent != null) {
                        pending_intent.cancel();
                    }
                }
                if(dbhandler.updateTask(task)){
                    if(added_reminders.size() > 0) {
                        dbhandler.addReminders(added_reminders, task.getTask_id());
                    }
                    //update reminders in reminder_list
                    for(int j = 0; j < reminder_list.size(); j++){
                        dbhandler.updateReminder(reminder_list.get(j));
                    }
                    //delete unwanted reminders
                    for(int k = 0; k < marked_for_deletion.size(); k++){
                        dbhandler.deleteReminder(marked_for_deletion.get(k));
                    }

                    //reschedule notification
                    next_reminder = dbhandler.getNextReminder(task.getTask_id());
                    new AlarmTask(getActivity().getApplicationContext(), task.getTask_id(), next_reminder.getId()).run();
                    getActivity().setResult(getActivity().RESULT_OK);
                    getActivity().finish();
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Failed to save edited details", Toast.LENGTH_SHORT).show();
                    getActivity().setResult(getActivity().RESULT_CANCELED);
                }
                getActivity().finish();
                break;
            case R.id.plus_icon:
                Bundle args = new Bundle();
                final Calendar cal = Calendar.getInstance();
                args.putInt("year", cal.get(Calendar.YEAR));
                args.putInt("month", cal.get(Calendar.MONTH));
                args.putInt("day", cal.get(Calendar.DAY_OF_MONTH));
                args.putInt("hour", cal.get(Calendar.HOUR_OF_DAY));
                args.putInt("minute", cal.get(Calendar.MINUTE));
                args.putString("title", getActivity().getResources().getText(R.string.new_reminder).toString());
                DateTimeDialogFragment new_reminder_dialog = new DateTimeDialogFragment();
                new_reminder_dialog.setArguments(args);
                new_reminder_dialog.setCallBack(datetime_listener);
                new_reminder_dialog.show(getActivity().getSupportFragmentManager(), "dialog");
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        reminder_selected = reminder_list.get(position); //reminders that are marked for deletion should be in a separate arraylist
        if(view.findViewById(R.id.schedule_item_datetime) != null){
            TextView dialog_title = (TextView) view.findViewById(R.id.schedule_item_datetime);
            Bundle args =  new Bundle();
            args.putString("date_time", dialog_title.getText().toString());
            args.putInt("with_audio", reminder_selected.getWith_audio());
            args.putInt("position", position);
            ReminderDialog action_fragment = new ReminderDialog();
            action_fragment.setArguments(args);
            action_fragment.setCallBack(action_listener);
            action_fragment.show(getActivity().getSupportFragmentManager(), "dialog");
        }
    }
}
