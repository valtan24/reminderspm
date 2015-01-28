package com.reminders.valerie.reminders;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.text.DateFormatSymbols;

public class TodoFragment extends ListFragment implements View.OnClickListener {

    private final static int TODO_FRAGMENT = 100;
    TaskDBHandler dbhandler;

    //date picker items
    int year;
    int month;
    int day;
    String month_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.todo_fragment, container, false);

        //get current date by calendar
        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);


        try {
            updateTaskList();
        }
        catch(Exception e) {

        }
        finally {
            return rootView;
        }

    }

    private void updateTaskList() throws Exception{
        dbhandler = new TaskDBHandler(getActivity());
        Cursor cursor = dbhandler.getTasksForDate(year, month, day, dbhandler.KEY_TASKTIME);
        String[] from = new String[]{dbhandler.KEY_TASKTITLE, dbhandler.KEY_TASKTIME};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2, cursor, from, to, 0);
        setListAdapter(adapter);
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

        int id = item.getItemId();

        if (id == R.id.action_new_task) {
            Intent new_task_intent = new Intent(getActivity(), NewTaskActivity.class);
            startActivityForResult(new_task_intent, TODO_FRAGMENT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == TodoFragment.TODO_FRAGMENT){
            if(resultCode == getActivity().RESULT_OK){
                try {
                    Toast.makeText(getActivity().getApplicationContext(), "Result saved", Toast.LENGTH_SHORT);
                    updateTaskList();
                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Unable to retrieve tasks", Toast.LENGTH_SHORT);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
