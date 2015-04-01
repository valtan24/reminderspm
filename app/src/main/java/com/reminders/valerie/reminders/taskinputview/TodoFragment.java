package com.reminders.valerie.reminders.taskinputview;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.reminders.valerie.reminders.EditTaskActivity;
import com.reminders.valerie.reminders.NewTaskActivity;
import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.CursorToBundle;
import com.reminders.valerie.reminders.model.DateEditTextManager;

public class TodoFragment extends ListFragment implements View.OnClickListener {

    private final static int TODO_FRAGMENT = 100;
    TaskDBHandler dbhandler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.todo_fragment, container, false);
        try {
            updateTaskList();
        }
        catch(Exception e) {

        }
        finally {
            return rootView;
        }

    }

    public void updateTaskList() throws Exception{
        dbhandler = new TaskDBHandler(getActivity().getApplicationContext());
        Cursor cursor = dbhandler.getUncompletedTasks(dbhandler.KEY_TASKDATE);
        String[] from = new String[]{dbhandler.KEY_TASKTITLE, dbhandler.KEY_TASKDATE, dbhandler.KEY_TASKTIME};
        int[] to = {R.id.title_text, R.id.date_text, R.id.time_text};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.task_list_item, cursor, from, to, 0);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder(){
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if(i == cursor.getColumnIndex(dbhandler.KEY_TASKDATE)){
                    String date_original = cursor.getString(i);
                    int year = Integer.parseInt(date_original.substring(0,4));
                    int month = Integer.parseInt(date_original.substring(5,7));
                    int day = Integer.parseInt(date_original.substring(8,10));
                    String date_display = (new DateEditTextManager()).buildText(year, month, day);
                    TextView textview = (TextView) view;
                    textview.setText(date_display);
                    return true;
                }
                return false;
            }
        });
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
                    Toast.makeText(getActivity().getApplicationContext(), "Result saved", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        //retrieve data from database
        Cursor cursor = dbhandler.getUncompletedTasks(dbhandler.KEY_TASKDATE);
        Bundle args = CursorToBundle.getTaskByPosition(cursor, position);
        Intent edit_task_intent = new Intent(getActivity(), EditTaskActivity.class);
        edit_task_intent.putExtra("arguments", args);
        startActivityForResult(edit_task_intent, TODO_FRAGMENT);
    }
}
