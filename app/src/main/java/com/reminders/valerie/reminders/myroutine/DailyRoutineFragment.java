package com.reminders.valerie.reminders.myroutine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.PerDayEvent;

import java.util.ArrayList;


public class DailyRoutineFragment extends Fragment {
    private String day;
    private TextView day_header;
    private ListView routine_listview;
    private DayRoutineAdapter list_adapter;

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
        return rootView;
    }
}
