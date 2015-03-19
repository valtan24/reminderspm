package com.reminders.valerie.reminders.scheduleview;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.TaskInputActivity;
import com.reminders.valerie.reminders.model.Reminder;
import com.reminders.valerie.reminders.scheduleservice.ScheduleClient;

import java.util.Calendar;


public class NewScheduleFragment extends ScheduleFragment{

    DateTimeDialogFragment.OnDateTimeSetListener datetime_listener = new DateTimeDialogFragment.OnDateTimeSetListener() {
        @Override
        public void OnDateTimeSet(Bundle args) {
            if(reminder_selected != null) {
                reminder_selected.setHour(args.getInt("hour"));
                reminder_selected.setMinute(args.getInt("minute"));
                reminder_selected.setYear(args.getInt("year"));
                reminder_selected.setMonth(args.getInt("month"));
                reminder_selected.setDay(args.getInt("day"));
                reminder_selected = null;
            }
            else{//new reminder
                Reminder new_reminder = new Reminder();
                new_reminder.setHour(args.getInt("hour"));
                new_reminder.setMinute(args.getInt("minute"));
                new_reminder.setYear(args.getInt("year"));
                new_reminder.setMonth(args.getInt("month"));
                new_reminder.setDay(args.getInt("day"));
                new_reminder.setTask(task);
                reminder_list.add(new_reminder);
            }
            //rearrange arraylist first
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
                        reminder_list.remove(reminder_selected); //removed and stored in a separate list for existing schedule fragment
                        list_adapter.notifyDataSetChanged();
                        break;
                    default:
                        Log.d("Error", "invalid choice");
                }
                reminder_selected = null;

            }
            else{
                Log.d("Error","No reminder selected");
            }
        }
    };

    ReminderDialog.OnActionSelectedListener action_listener = new ReminderDialog.OnActionSelectedListener() {
        @Override
        public void onActionSelected(int position) {
            if(reminder_selected != null) {
                switch (position) {
                    case 0:
                        int tmp = reminder_selected.getWith_audio();
                        tmp = tmp + (int) Math.pow(-1, tmp);
                        reminder_selected.setWith_audio(tmp);
                        list_adapter.notifyDataSetChanged();
                        break;
                    case 1:
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
                TaskDBHandler handler = new TaskDBHandler(getActivity());
                long task_id = handler.addNewTask(task);
                handler.addReminders(reminder_list, task_id);
                Calendar c = Calendar.getInstance();
                Reminder first_reminder = reminder_list.get(0);
                c.set(first_reminder.getYear(), first_reminder.getMonth(), first_reminder.getDay());
                c.set(Calendar.HOUR_OF_DAY, first_reminder.getHour());
                c.set(Calendar.MINUTE, first_reminder.getMinute());
                c.set(Calendar.SECOND, 0);
                //bind schedule service
                ScheduleClient client = ((TaskInputActivity) getActivity()).getSchedule_client();
               client.setAlarmForNotification(c);
                getActivity().setResult(getActivity().RESULT_OK);
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