package com.reminders.valerie.reminders.Todo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.reminders.valerie.reminders.NewTaskActivity;
import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.TaskDBHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TodoFragment extends ListFragment implements View.OnClickListener {

    ListView todoListView;
    ArrayAdapter todoArrayAdapter;
    ArrayList todoList = new ArrayList();
    TaskDBHandler dbhandler;

    //date picker items
    int year;
    int month;
    String month_name;
    int day;
    private Button button_date_picker;
    private static final int DATE_PICKER_ID = 1111;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.todo_fragment, container, false);
        //pick date
        button_date_picker = (Button) rootView.findViewById(R.id.button_task_list_date);

        //get current date by calendar
        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat month_date = new SimpleDateFormat("MMMMMMMMM");
        month_name = month_date.format(cal.getTime());

        //display text on button
        button_date_picker.setText( day + " " + month_name + " " + year);

        try {
            dbhandler = new TaskDBHandler(getActivity());
            Cursor cursor = dbhandler.query(dbhandler.TABLE_TASKS, dbhandler.KEY_TASKID);
            String[] from = new String[]{dbhandler.KEY_TASKID, dbhandler.KEY_TASKTITLE};
            int[] to = {android.R.id.text1, android.R.id.text2};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2, cursor, from, to, 0);
            setListAdapter(adapter);
        }
        catch(Exception e) {
        }
        finally {
            return rootView;
        }

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

    @Override
    public void onClick(View v) {

    }
}
