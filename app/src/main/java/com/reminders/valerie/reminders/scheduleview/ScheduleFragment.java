package com.reminders.valerie.reminders.scheduleview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.reminders.valerie.reminders.model.DateEditTextManager;
import com.reminders.valerie.reminders.model.DateTimeEditTextMgr;
import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.Reminder;
import com.reminders.valerie.reminders.model.Task;
import com.reminders.valerie.reminders.model.TimeEditTextManager;

import java.util.ArrayList;


public abstract class ScheduleFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{

    protected ArrayList<Reminder> reminder_list; //new reminders for existingschedulefragment should be in a separate arraylist
    //new reminders for existingschedulefragment should be in a separate arraylist
    private ListView reminder_listview;
    protected ScheduleListAdapter list_adapter;
    protected Button save_button;
    protected Task task;
    protected Reminder reminder_selected;
    private ImageView plus_icon;
    private DateTimeEditTextMgr date_et_mgr, time_et_mgr;

    public void setTask(Task task){
        this.task = task;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reminder_schedule_fragment, container, false);
        ActionBar actionbar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        reminder_selected = null;
        //edittext managers
        date_et_mgr = new DateEditTextManager();
        time_et_mgr = new TimeEditTextManager();
        save_button = (Button) rootView.findViewById(R.id.save_task_button);
        save_button.setOnClickListener(this);
        reminder_listview = (ListView) rootView.findViewById(R.id.reminder_list);
        plus_icon = (ImageView) rootView.findViewById(R.id.plus_icon);
        plus_icon.setClickable(true);
        plus_icon.setOnClickListener(this);
        //populate list
        list_adapter = new ScheduleListAdapter(getActivity(), reminder_list);
        reminder_listview.setAdapter(list_adapter);
        reminder_listview.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public abstract void onClick(View v);

    public void setReminderArrayList(ArrayList<Reminder> reminder_list){
        this.reminder_list = reminder_list;
    }

    @Override
    public abstract void onItemClick(AdapterView<?> parent, View view, int position, long id);

}
