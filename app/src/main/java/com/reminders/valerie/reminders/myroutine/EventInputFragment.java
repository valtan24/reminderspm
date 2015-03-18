package com.reminders.valerie.reminders.myroutine;


import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.DateTimeEditTextMgr;
import com.reminders.valerie.reminders.model.TimeEditTextManager;

public abstract class EventInputFragment extends Fragment implements View.OnClickListener{


    private TextView action_header;
    private EditText event_title, start_time, end_time;
    private Button delete_button, save_button;
    private View button_space;
    private int start_hour, start_minute, end_hour, end_minute;

    private TimeEditTextManager time_et_mgr;

    public Spinner category_spinner;
    public String category;

    View.OnClickListener listener;

    TimePickerDialog.OnTimeSetListener start_listener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            addTimeText(start_hour, start_minute, hourOfDay, minute, start_time);
        }
    };

    TimePickerDialog.OnTimeSetListener end_listener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            addTimeText(end_hour, end_minute, hourOfDay, minute, end_time);
        }
    };

    private void addTimeText(int hour_to_set, int minute_to_set, int hour, int minute, EditText time_edittext){
        hour_to_set = hour;
        minute_to_set = minute;
        if(time_et_mgr != null){
            String time_text = time_et_mgr.buildText(hour_to_set, minute_to_set, 0);
            time_edittext.setText(time_text);
        }
    }

    public void setListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.event_input_fragment,null);
        action_header = (TextView) rootView.findViewById(R.id.action_header);
        event_title = (EditText) rootView.findViewById(R.id.event_title);
        start_time = (EditText) rootView.findViewById(R.id.start_time_edittext);
        end_time = (EditText) rootView.findViewById(R.id.end_time_edittext);

        delete_button = (Button) rootView.findViewById(R.id.delete_button);
        save_button = (Button) rootView.findViewById(R.id.save_button);
        time_et_mgr = new TimeEditTextManager();
        button_space = rootView.findViewById(R.id.button_space);
        //category spinner
        category_spinner = (Spinner) rootView.findViewById(R.id.category_spinner);
        TaskDBHandler dbhandler = new TaskDBHandler(getActivity().getApplicationContext());
        Cursor cursor = dbhandler.getCategoryNames();
        String[] from = new String[]{"_id"};
        int[] to = {android.R.id.text1};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, cursor, from, to, 0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(adapter);
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getItemAtPosition(position);
                category = c.getString(c.getColumnIndex("_id"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Cursor c = (Cursor) parent.getItemAtPosition(0);
                category = c.getString(c.getColumnIndex("_id"));
            }
        });

        setContents();

        start_time.setClickable(true);
        start_time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Bundle time_args = new Bundle();
                time_args.putInt("hour",start_hour);
                time_args.putInt("minute", start_minute);
                time_et_mgr.showPickerFragment(getActivity().getSupportFragmentManager(), start_listener, time_args);
            }
        });

        end_time.setClickable(true);
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle time_args = new Bundle();
                time_args.putInt("hour", end_hour);
                time_args.putInt("minute", end_minute);
                time_et_mgr.showPickerFragment(getActivity().getSupportFragmentManager(), end_listener, time_args);
            }
        });

        save_button.setOnClickListener(this);
        delete_button.setOnClickListener(this);

        return rootView;
    }

    public abstract void setContents(); //edit or add have different texts

    @Override
    public void onClick(View v){
        FragmentManager fragment_mgr = getActivity().getSupportFragmentManager();
        switch(v.getId()){
            case R.id.delete_button:
                Toast.makeText(getActivity().getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                if(fragment_mgr.getBackStackEntryCount() > 0){
                    fragment_mgr.popBackStack();
                }
                break;
            case R.id.save_button:
                Toast.makeText(getActivity().getApplicationContext(), "Event Added", Toast.LENGTH_SHORT).show();
                if(fragment_mgr.getBackStackEntryCount() > 0){
                    fragment_mgr.popBackStack();
                }
                break;
        }
    }

    public TextView getAction_header() {
        return action_header;
    }

    public EditText getEvent_title() {
        return event_title;
    }

    public Button getDelete_button() {
        return delete_button;
    }
    public Button getSave_button() {
        return save_button;
    }

    public void setSave_button(Button save_button) {
        this.save_button = save_button;
    }
    public void setStart_hour(int start_hour) {
        this.start_hour = start_hour;
    }

    public void setStart_minute(int start_minute) {
        this.start_minute = start_minute;
    }
    public void setEnd_hour(int end_hour) {
        this.end_hour = end_hour;
    }
    public void setEnd_minute(int end_minute) {
        this.end_minute = end_minute;
    }
    public View getButton_space() {
        return button_space;
    }
    public DateTimeEditTextMgr getTime_et_mgr(){
        return time_et_mgr;
    }
    public EditText getStart_time(){
        return start_time;
    }
    public EditText getEnd_time(){
        return end_time;
    }
    public int getStart_hour(){
        return start_hour;
    }
    public int getStart_minute(){
        return start_minute;
    }
    public int getEnd_hour(){
        return end_hour;
    }
    public int getEnd_minute(){
        return end_minute;
    }

}
