package com.reminders.valerie.reminders.scheduleview;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.reminders.valerie.reminders.TaskDBHandler;

import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.Reminder;

import java.util.ArrayList;

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
            if(reminder_selected != null) {
                reminder_selected.setHour(args.getInt("hour"));
                reminder_selected.setMinute(args.getInt("minute"));
                reminder_selected.setYear(args.getInt("year"));
                reminder_selected.setMonth(args.getInt("month"));
                reminder_selected.setDay(args.getInt("day"));
                reminder_selected = null;
                //TODO rearrange arraylist first
                list_adapter.notifyDataSetChanged();
            }
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
                        marked_for_deletion.add(reminder_selected);
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
        if (v.getId() == save_button.getId()) {
            //TODO SAVE EXISTING TASK AND REMINDERS
            TaskDBHandler dbhandler = new TaskDBHandler(getActivity().getApplicationContext());
            if(dbhandler.updateTask(task)){
                getActivity().setResult(getActivity().RESULT_OK);
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), "Failed to save editted details", Toast.LENGTH_SHORT).show();
                getActivity().setResult(getActivity().RESULT_CANCELED);
            }
            getActivity().finish();
        }
    }
}
