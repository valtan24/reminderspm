package com.reminders.valerie.reminders;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class TodoFragment extends ListFragment implements View.OnClickListener {

    ListView todoListView;
    ArrayAdapter todoArrayAdapter;
    ArrayList todoList = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        for (int i = 0; i < 10; i++){
            todoList.add(i);
        }
        View rootView = inflater.inflate(R.layout.todo_fragment, container, false);

        todoArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, todoList);
        setListAdapter(todoArrayAdapter);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        //date selector
    }
}
