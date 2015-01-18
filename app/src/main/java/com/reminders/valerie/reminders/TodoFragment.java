package com.reminders.valerie.reminders;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.reminders.valerie.reminders.DatePickerDialogFragment;
import com.reminders.valerie.reminders.NewTaskActivity;
import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.TaskDBHandler;
import java.util.Calendar;
import java.text.DateFormatSymbols;

public class TodoFragment extends ListFragment implements View.OnClickListener {


    TaskDBHandler dbhandler;

    //date picker items
    int year;
    int month;
    int day;
    String month_name;
    private Button button_date_picker;

    DatePickerDialog.OnDateSetListener date_set_listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int listener_year, int listener_month, int listener_day) {
            year = listener_year;
            month = listener_month;
            day = listener_day;
            setDateButtonText();
            try {
                updateTaskList();
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Could not retrieve tasks", Toast.LENGTH_SHORT);
            }
        }
    };

    //set button date text
    private void setDateButtonText(){
        month_name = new DateFormatSymbols().getMonths()[month];
        if(button_date_picker != null){
            //display text on button
            button_date_picker.setText( day + " " + month_name + " " + year);
        }
    }

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

        setDateButtonText();

        //button on click listener
        button_date_picker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

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

    private void showDatePicker() {
        DatePickerDialogFragment date_picker = new DatePickerDialogFragment();
        Bundle date_args = new Bundle();
        date_args.putInt("year", year);
        date_args.putInt("month", month);
        date_args.putInt("day", day);
        date_picker.setArguments(date_args);
        date_picker.setCallBack(date_set_listener);


        //show fragment
        FragmentManager fragment_mgr = getActivity().getSupportFragmentManager();
        date_picker.show(fragment_mgr, "dialog");
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
        /*
        int id = item.getItemId();

        if (id == R.id.action_new_task) {
            Intent new_task_intent = new Intent(getActivity(), NewTaskActivity.class);
            startActivity(new_task_intent);
            return true;
        }*/

        FragmentManager fragment_mgr =  getFragmentManager();
        FragmentTransaction fragment_transaction = fragment_mgr.beginTransaction();
        Fragment new_task_fragment = new NewTaskFragment();
        fragment_transaction.replace(R.id.new_task_input, new_task_fragment);
        fragment_transaction.commit();

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View v) {

    }
}
