package com.reminders.valerie.reminders.historyview;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.DateEditTextManager;


public class HistoryFragment extends ListFragment {

    private TaskDBHandler dbhandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.history_fragment, container, false);
        dbhandler = new TaskDBHandler(getActivity());
        try{
            getCompletedTaskList();
        }
        catch(Exception e){

        }
        finally{
            return rootView;
        }
    }

    private void getCompletedTaskList() throws Exception{
        Cursor cursor = dbhandler.getCompletedTasks();
        String [] from = new String[] {dbhandler.KEY_TASKTITLE, dbhandler.KEY_TASKDATE, dbhandler.KEY_TASKTIME};
        int[] to = {R.id.title_text, R.id.date_text, R.id.time_text};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.task_list_item, cursor, from, to, 0);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder(){
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if(columnIndex == cursor.getColumnIndex(dbhandler.KEY_TASKDATE)){
                    String date_original = cursor.getString(columnIndex);
                    int year = Integer.parseInt(date_original.substring(0,4));
                    int month = Integer.parseInt(date_original.substring(5,7));
                    int day = Integer.parseInt(date_original.substring(8,10));
                    String date_display = (new DateEditTextManager()).buildText(year, month, day);
                    TextView textview = (TextView) view;
                    textview.setText(date_display);
                }
                return false;
            }
        });
        setListAdapter(adapter);
    }

    @Override
    public void onDestroy(){
        dbhandler.close();
        super.onDestroy();
    }
}
