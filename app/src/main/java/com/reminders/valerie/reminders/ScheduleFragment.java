package com.reminders.valerie.reminders;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ScheduleFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ArrayList<Reminder> reminder_arraylist;
    private ArrayList<String> reminder_string_arraylist;
    private ListView reminder_listview;
    private ArrayAdapter reminder_adapter;
    private ScheduleListAdapter list_adapter;
    private Button save_button;

    ReminderDialog.OnActionSelectedListener action_listener = new ReminderDialog.OnActionSelectedListener() {
        @Override
        public void onActionSelected(int position) {
            Log.d("selected", ""+position);
        }
    };

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
        list_adapter = new ScheduleListAdapter(getActivity(), reminder_arraylist);
        reminder_listview.setAdapter(list_adapter);

        reminder_listview.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(view.findViewById(R.id.schedule_item_datetime) != null){
            TextView dialog_title = (TextView) view.findViewById(R.id.schedule_item_datetime);
            Bundle args =  new Bundle();
            args.putString("date_time", dialog_title.getText().toString());
            args.putInt("with_audio", reminder_arraylist.get((int) id).getWith_audio());
            ReminderDialog action_fragment = new ReminderDialog();
            action_fragment.setArguments(args);
            action_fragment.setCallBack(action_listener);
            action_fragment.show(getActivity().getSupportFragmentManager(), "dialog");
        }
    }

}
