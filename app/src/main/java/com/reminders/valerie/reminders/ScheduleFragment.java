package com.reminders.valerie.reminders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class ScheduleFragment extends Fragment implements View.OnClickListener {

    private ArrayList<Reminder> reminder_arraylist;
    private ArrayList<String> reminder_string_arraylist;
    private ListView reminder_listview;
    private ArrayAdapter reminder_adapter;
    private Button save_button;

    private DateTimeEditTextMgr date_et_mgr, time_et_mgr;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reminder_schedule_fragment, container, false);
        //edittext managers
        date_et_mgr = new DateEditTextManager();
        time_et_mgr = new TimeEditTextManager();
        save_button = (Button) rootView.findViewById(R.id.save_task_button);
        reminder_listview = (ListView) rootView.findViewById(R.id.reminder_list);

        //populate list
        reminder_string_arraylist = new ArrayList<String>();
        reminder_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, reminder_string_arraylist);
        reminder_listview.setAdapter(reminder_adapter);

        populateStringArrayList();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == save_button.getId()) {
            getActivity().setResult(getActivity().RESULT_OK);
            getActivity().finish();
        }
    }

    public void setReminderArrayList(ArrayList<Reminder> rem_list) {
        this.reminder_arraylist = rem_list;
    }

    public void populateStringArrayList(){
        for(int i = 0; i < reminder_arraylist.size(); i++){
            Reminder rem_tmp = reminder_arraylist.get(i);
            String date_text = date_et_mgr.buildText(rem_tmp.getYear(), rem_tmp.getMonth(), rem_tmp.getDay());
            String time_text = time_et_mgr.buildText(rem_tmp.getHour(), rem_tmp.getMinute(), 0);
            reminder_string_arraylist.add(date_text + ", " + time_text);
            reminder_adapter.notifyDataSetChanged();
        }
    }
}
