package com.reminders.valerie.reminders.myroutine;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.reminders.valerie.reminders.R;

public class RoutineFragment extends Fragment implements AdapterView.OnItemClickListener{

    private String[] days;
    private ListView days_listview;
    private ArrayAdapter list_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.routine_fragment, container, false);
        ActionBar actionbar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(false);
        days = getActivity().getResources().getStringArray(R.array.days_list);
        list_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, days);
        days_listview = (ListView) rootView.findViewById(R.id.days_listview);
        days_listview.setAdapter(list_adapter);
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //open new fragment for schedule
    }
}
