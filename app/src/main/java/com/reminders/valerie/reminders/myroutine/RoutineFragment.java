package com.reminders.valerie.reminders.myroutine;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.reminders.valerie.reminders.MainActivity;
import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.DailyActivity;

import java.util.ArrayList;

public class RoutineFragment extends Fragment implements AdapterView.OnItemClickListener{

    private String[] days;
    private ListView days_listview;
    private ArrayAdapter list_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.routine_fragment, container, false);
        days = getActivity().getResources().getStringArray(R.array.days_list);
        ((MainActivity) getActivity()).enableDrawer();
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
        ArrayList<DailyActivity> routine = setDummyData();
        daily_routine.setRoutine_list(routine);
        fragment_transaction.addToBackStack(null);
        fragment_transaction.replace(R.id.content_frame, daily_routine);
        fragment_transaction.commit();

    }

    //dummy data to be removed
    private ArrayList<DailyActivity> setDummyData(){
        ArrayList<DailyActivity> routine = new ArrayList<DailyActivity>();
        DailyActivity event1 = new DailyActivity();
        event1.setStart_hour(8);
        event1.setStart_minute(0);
        event1.setEnd_hour(9);
        event1.setEnd_minute(0);
        event1.setName("Breakfast");
        routine.add(event1);

        DailyActivity a2 = new DailyActivity();
        a2.setStart_hour(10);
        a2.setStart_minute(30);
        a2.setEnd_hour(12);
        a2.setEnd_minute(30);
        a2.setName("Lab");
        routine.add(a2);

        DailyActivity a3 = new DailyActivity();
        a3.setStart_hour(12);
        a3.setStart_minute(30);
        a3.setEnd_hour(14);
        a3.setEnd_minute(0);
        a3.setName("Lunch");
        routine.add(a3);

        DailyActivity a4 = new DailyActivity();
        a4.setStart_hour(16);
        a4.setStart_minute(30);
        a4.setEnd_hour(18);
        a4.setEnd_minute(0);
        a4.setName("Tutorial");
        routine.add(a4);
        return routine;
    }
}
