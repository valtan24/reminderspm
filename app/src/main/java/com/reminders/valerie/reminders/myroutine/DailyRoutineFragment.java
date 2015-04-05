package com.reminders.valerie.reminders.myroutine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.reminders.valerie.reminders.MainActivity;
import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.DailyActivity;

import java.util.ArrayList;


public class DailyRoutineFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, ActivityInputFragment.OnSaveActivityListener{
    private String day;
    private int day_int;
    private TextView day_header;
    private ListView routine_listview;
    private DailyRoutineListAdapter list_adapter;
    private ImageView add_icon, duplicate_icon;

    private ArrayList<DailyActivity> routine_list;

    private Button clear_all_button, save_button;

    public ArrayList<DailyActivity> getRoutine_list() {
        return routine_list;
    }

    public void setRoutine_list(ArrayList<DailyActivity> routine_list) {
        this.routine_list = routine_list;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setDayInt(int day){
        day_int = day;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.d("view", "creating");
        View rootView = inflater.inflate(R.layout.daily_routine_fragment, null);
        ActionBar actionbar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        MainActivity activity = (MainActivity) getActivity();
        activity.disableDrawer();
        day_header = (TextView) rootView.findViewById(R.id.day_textview);
        day_header.setText(this.day);
        routine_listview = (ListView) rootView.findViewById(R.id.daily_routine_list);
        list_adapter = new DailyRoutineListAdapter(getActivity(), routine_list);
        routine_listview.setAdapter(list_adapter);
        clear_all_button = (Button) rootView.findViewById(R.id.clear_all_button);
        save_button = (Button) rootView.findViewById(R.id.save_button);
        add_icon = (ImageView) rootView.findViewById(R.id.add_icon);
        add_icon.setClickable(true);
        add_icon.setOnClickListener(this);
        clear_all_button.setOnClickListener(this);
        save_button.setOnClickListener(this);
        routine_listview.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragment_mgr;
        FragmentTransaction fragment_transaction;
        switch(v.getId()){
            case R.id.duplicate_icon:
                //duplicate from other days
                break;
            case R.id.add_icon:
                //add event
                fragment_mgr = getActivity().getSupportFragmentManager();
                fragment_transaction = fragment_mgr.beginTransaction();
                Fragment new_event = new NewActivityFragment();
                ((ActivityInputFragment)new_event).setDay(day_int);
                ((ActivityInputFragment) new_event).setCallBack(this);
                fragment_transaction.addToBackStack(null);
                fragment_transaction.replace(R.id.content_frame, new_event);
                fragment_transaction.commit();
                break;
            case R.id.clear_all_button:
                //open confirmation dialog
                Toast.makeText(getActivity().getApplicationContext(), "Clearing all", Toast.LENGTH_SHORT).show();
                break;
            case R.id.save_button:
                //save data and return
                Toast.makeText(getActivity().getApplicationContext(), "data saved", Toast.LENGTH_SHORT).show();
                fragment_mgr = getActivity().getSupportFragmentManager();
                if(fragment_mgr.getBackStackEntryCount() > 0){
                    fragment_mgr.popBackStack();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentManager fragment_mgr = getActivity().getSupportFragmentManager();
        FragmentTransaction fragment_transaction = fragment_mgr.beginTransaction();
        Fragment edit_fragment = new EditActivityFragment();
        Bundle args = new Bundle();
        DailyActivity daily_activity = (DailyActivity) list_adapter.getItem(position);
        ((ActivityInputFragment)edit_fragment).setDay(day_int);
        ((ActivityInputFragment) edit_fragment).setCallBack(this);
        args.putString("title", daily_activity.getName());
        args.putInt("start_hour", daily_activity.getStart_hour());
        args.putInt("start_minute", daily_activity.getStart_minute());
        args.putInt("end_hour", daily_activity.getEnd_hour());
        args.putInt("end_minute", daily_activity.getEnd_hour());
        args.putInt("is_noisy", daily_activity.isNoisy());
        args.putDouble("complexity", daily_activity.getComplexity());
        args.putString("category", daily_activity.getCategory());
        args.putLong("activity_id", daily_activity.getId());
        ((EditActivityFragment) edit_fragment).setArgs(args);
        fragment_transaction.addToBackStack(null);
        fragment_transaction.replace(R.id.content_frame, edit_fragment).commit();
    }

    @Override
    public void onSaveActivity() {
        TaskDBHandler dbhandler = new TaskDBHandler(getActivity().getApplicationContext());
        routine_list = null;
        routine_list = dbhandler.getActivitiesByDay(day_int);
        list_adapter.notifyDataSetChanged();
        dbhandler.close();
    }
}
