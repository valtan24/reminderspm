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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.PerDayEvent;

import java.util.ArrayList;

public class RoutineFragment extends Fragment implements AdapterView.OnItemClickListener{

    private String[] days;
    private ListView days_listview;
    private ArrayAdapter list_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.routine_fragment, container, false);
        days = getActivity().getResources().getStringArray(R.array.days_list);
        list_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, days);
        days_listview = (ListView) rootView.findViewById(R.id.days_listview);
        days_listview.setAdapter(list_adapter);
        days_listview.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //open new fragment for schedule
        FragmentManager fragment_mgr = getActivity().getSupportFragmentManager();
        FragmentTransaction fragment_transaction = fragment_mgr.beginTransaction();
        DailyRoutineFragment daily_routine = new DailyRoutineFragment();
        daily_routine.setDay(days[position]);
        ArrayList<PerDayEvent> routine = setDummyData();
        daily_routine.setRoutine_list(routine);
        fragment_transaction.addToBackStack(null);
        fragment_transaction.replace(R.id.content_frame, daily_routine);
        fragment_transaction.commit();

    }

    //dummy data to be removed
    private ArrayList<PerDayEvent> setDummyData(){
        ArrayList<PerDayEvent> routine = new ArrayList<PerDayEvent>();
        PerDayEvent event1 = new PerDayEvent();
        event1.setStart_hour(8);
        event1.setStart_minute(0);
        event1.setEnd_hour(9);
        event1.setEnd_minute(0);
        event1.setName("Breakfast");
        routine.add(event1);
        return routine;
    }
}
