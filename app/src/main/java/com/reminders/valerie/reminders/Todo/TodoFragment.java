package com.reminders.valerie.reminders.Todo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.reminders.valerie.reminders.NewTaskActivity;
import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.TaskDBHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TodoFragment extends ListFragment implements View.OnClickListener {

    ListView todoListView;
    ArrayAdapter todoArrayAdapter;
    ArrayList todoList = new ArrayList();
    TaskDBHandler dbhandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        /*for (int i = 0; i < 10; i++){
            todoList.add(i);
        }
        View rootView = inflater.inflate(R.layout.todo_fragment, container, false);
        todoArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, todoList);
        setListAdapter(todoArrayAdapter);*/

        try {
            dbhandler = new TaskDBHandler(getActivity());

            addNewTask("task1");
            addNewTask("task2");
            addNewTask("task3");

            Cursor cursor = dbhandler.query(dbhandler.TABLE_TASKS, dbhandler.KEY_TASKID);
            String[] from = new String[]{dbhandler.KEY_TASKID, dbhandler.KEY_TASKTITLE};
            int[] to = {android.R.id.text1, android.R.id.text2};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2, cursor, from, to, 0);
            setListAdapter(adapter);
        }
        catch(Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            View rootView = inflater.inflate(R.layout.todo_fragment, container, false);
            return rootView;
        }

    }

    //temp original data
    public void addNewTask(String title) throws Exception {
        ContentValues values = new ContentValues();
        values.put(dbhandler.KEY_TASKTITLE, title);
        //values.put(dbhandler.KEY_TASKDATE, time.toString());
        //values.put(dbhandler.KEY_TASKTIME, date.toString());
        values.put(dbhandler.KEY_COMPLETED, 0);

        dbhandler.insert(dbhandler.TABLE_TASKS, values);
    }

    @Override
    public void onClick(View v) {
        //date selector
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_todo_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_task) {
            Intent new_task_intent = new Intent(getActivity(), NewTaskActivity.class);
            //startActivityForResult(new_task_intent, 1); -- 2nd param is the request code, need to look up
            startActivity(new_task_intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
