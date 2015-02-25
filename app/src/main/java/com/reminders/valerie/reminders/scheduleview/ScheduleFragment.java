package com.reminders.valerie.reminders.scheduleview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.DateEditTextManager;
import com.reminders.valerie.reminders.model.DateTimeEditTextMgr;
import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.Reminder;
import com.reminders.valerie.reminders.model.Task;
import com.reminders.valerie.reminders.model.TimeEditTextManager;

import java.util.ArrayList;


public class ScheduleFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ArrayList<Reminder> reminder_arraylist;
    private ListView reminder_listview;
    private ScheduleListAdapter list_adapter;
    public Button save_button;

    private Task task;
    private int reminder_selected;

    public void setTask(Task task){
        this.task = task;
    }

    DateTimeDialogFragment.OnDateTimeSetListener datetime_listener = new DateTimeDialogFragment.OnDateTimeSetListener() {
        @Override
        public void OnDateTimeSet(Bundle args) {
            if(reminder_selected != -1) {
                Reminder reminder = reminder_arraylist.get(reminder_selected);
                reminder.setHour(args.getInt("hour"));
                reminder.setMinute(args.getInt("minute"));
                reminder.setYear(args.getInt("year"));
                reminder.setMonth(args.getInt("month"));
                reminder.setDay(args.getInt("day"));
                reminder_selected = -1;
                //rearrange arraylist first
                list_adapter.notifyDataSetChanged();
            }
        }
    };

    DeleteDialogFragment.OnDeleteSetListener delete_listener = new DeleteDialogFragment.OnDeleteSetListener(){
        @Override
        public void OnDeleteSet(int choice) {
            if(reminder_selected != -1) {
                switch (choice) {
                    case 0: //cancel
                        //do nothing;
                        break;
                    case 1:
                        reminder_arraylist.remove(reminder_selected);
                        list_adapter.notifyDataSetChanged();
                        break;
                    default:
                        Log.d("Error", "invalid choice");
                }
                reminder_selected = -1;
            }
            else{
                Log.d("Error","No reminder selected");
            }
        }
    };

    ReminderDialog.OnActionSelectedListener action_listener = new ReminderDialog.OnActionSelectedListener() {
        @Override
        public void onActionSelected(int position) {
        if (reminder_selected != -1) {
            switch (position) {
                case 0:
                Reminder rem = reminder_arraylist.get(reminder_selected);
                int tmp = rem.getWith_audio();
                tmp = tmp + (int) Math.pow(-1, tmp);
                rem.setWith_audio(tmp);
                list_adapter.notifyDataSetChanged();
                break;
            case 1:
                Bundle args = new Bundle();
                args.putInt("year", reminder_arraylist.get(reminder_selected).getYear());
                args.putInt("month", reminder_arraylist.get(reminder_selected).getMonth());
                args.putInt("day", reminder_arraylist.get(reminder_selected).getDay());
                args.putInt("hour", reminder_arraylist.get(reminder_selected).getHour());
                args.putInt("minute", reminder_arraylist.get(reminder_selected).getMinute());
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

    private DateTimeEditTextMgr date_et_mgr, time_et_mgr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reminder_schedule_fragment, container, false);
        ActionBar actionbar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        reminder_selected = -1;
        //edittext managers
        date_et_mgr = new DateEditTextManager();
        time_et_mgr = new TimeEditTextManager();
        save_button = (Button) rootView.findViewById(R.id.save_task_button);
        save_button.setOnClickListener(this);
        reminder_listview = (ListView) rootView.findViewById(R.id.reminder_list);

        //populate list
        list_adapter = new ScheduleListAdapter(getActivity(), reminder_arraylist);
        reminder_listview.setAdapter(list_adapter);

        reminder_listview.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == save_button.getId()) {
            TaskDBHandler handler = new TaskDBHandler(getActivity());
            long task_id = handler.addNewTask(task);
            handler.addReminders(reminder_arraylist, task_id);
            getActivity().setResult(getActivity().RESULT_OK);
            getActivity().finish();
        }
    }

    public void setReminderArrayList(ArrayList<Reminder> rem_list) {
        this.reminder_arraylist = rem_list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        reminder_selected = position;
        if(view.findViewById(R.id.schedule_item_datetime) != null){
            TextView dialog_title = (TextView) view.findViewById(R.id.schedule_item_datetime);
            Bundle args =  new Bundle();
            args.putString("date_time", dialog_title.getText().toString());
            args.putInt("with_audio", reminder_arraylist.get(position).getWith_audio());
            args.putInt("position", position);
            ReminderDialog action_fragment = new ReminderDialog();
            action_fragment.setArguments(args);
            action_fragment.setCallBack(action_listener);
            action_fragment.show(getActivity().getSupportFragmentManager(), "dialog");
        }
    }

}
