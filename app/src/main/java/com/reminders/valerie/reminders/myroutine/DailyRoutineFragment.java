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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.PerDayEvent;

import java.util.ArrayList;


public class DailyRoutineFragment extends Fragment implements View.OnClickListener{
    private String day;
    private TextView day_header;
    private ListView routine_listview;
    private DayRoutineAdapter list_adapter;

    private ImageView add_icon, duplicate_icon;
    private Button clear_all_button, save_button;

    public ArrayList<PerDayEvent> getRoutine_list() {
        return routine_list;
    }

    public void setRoutine_list(ArrayList<PerDayEvent> routine_list) {
        this.routine_list = routine_list;
    }

    private ArrayList<PerDayEvent> routine_list;

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.daily_routine_fragment, null);
        ActionBar actionbar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        day_header = (TextView) rootView.findViewById(R.id.day_textview);
        day_header.setText(this.day);
        routine_listview = (ListView) rootView.findViewById(R.id.daily_routine_list);
        list_adapter = new DayRoutineAdapter(getActivity(), routine_list);
        routine_listview.setAdapter(list_adapter);

        add_icon = (ImageView) rootView.findViewById(R.id.add_icon);
        add_icon.setClickable(true);
        duplicate_icon = (ImageView) rootView.findViewById(R.id.duplicate_icon);
        duplicate_icon.setClickable(true);

        clear_all_button = (Button) rootView.findViewById(R.id.clear_all_button);
        save_button = (Button) rootView.findViewById(R.id.save_button);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_icon:
                //open add category dialog fragment
                Toast.makeText(getActivity().getApplicationContext(), "adding category", Toast.LENGTH_SHORT).show();
                break;
            case R.id.duplicate_icon:
                //open duplicate dialog fragment
                Toast.makeText(getActivity().getApplicationContext(), "duplicating from other days", Toast.LENGTH_SHORT).show();
                break;
            case R.id.clear_all_button:
                //open confirmation dialog
                Toast.makeText(getActivity().getApplicationContext(), "Clearing all", Toast.LENGTH_SHORT).show();
                break;
            case R.id.save_button:
                //save data and return
                Toast.makeText(getActivity().getApplicationContext(), "data saved", Toast.LENGTH_SHORT).show();
                FragmentManager fragment_mgr = getActivity().getSupportFragmentManager();
                if(fragment_mgr.getBackStackEntryCount() > 0){
                    fragment_mgr.popBackStack();
                }
        }
    }
}
