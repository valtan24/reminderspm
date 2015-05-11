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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.DateTimeEditTextMgr;
import com.reminders.valerie.reminders.model.TimeEditTextManager;
import com.reminders.valerie.reminders.myprofile.CategoryDialog;

public abstract class ActivityInputFragment extends Fragment implements View.OnClickListener{


    protected TextView action_header;
    protected EditText event_title, start_time, end_time;
    protected Button delete_button, save_button;
    protected View button_space;
    protected int start_hour, start_minute, end_hour, end_minute;
    protected RadioGroup complexity_group;
    protected RadioButton complexity_high, complexity_medium, complexity_low;
    protected RadioGroup env_group;
    protected RadioButton env_noisy, env_dnd;

    protected int day;


    protected TimeEditTextManager time_et_mgr;

    public Spinner category_spinner;
    public String category;

    protected OnSaveActivityListener list_listener;

    View.OnClickListener listener;

    TimePickerDialog.OnTimeSetListener start_listener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            start_hour = hourOfDay;
            start_minute = minute;
            addTimeText(start_time, start_hour, start_minute);
        }
    };

    TimePickerDialog.OnTimeSetListener end_listener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if(end_hour < start_hour){
                Toast.makeText(getActivity().getApplicationContext(),"The end time should be after the start time", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(end_hour == start_hour){
                if(end_minute <= start_minute){
                    Toast.makeText(getActivity().getApplicationContext(), "The end time should be after the start time", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            end_hour = hourOfDay;
            end_minute = minute;
            addTimeText(end_time, end_hour, end_minute);
        }
    };

    public interface OnSaveActivityListener{
        public void onSaveActivity();
    }

    public void setCallBack(OnSaveActivityListener listener){
        list_listener = listener;
    }
    public void setDay(int day){
        this.day = day;
    }

    private void addTimeText(EditText time_edittext, int hour, int minute){
        if(time_et_mgr != null){
            String time_text = time_et_mgr.buildText(hour, minute, 0);
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

        complexity_group = (RadioGroup) rootView.findViewById(R.id.complexity_radiogroup);
        complexity_high = (RadioButton) rootView.findViewById(R.id.complexity_high);
        complexity_medium = (RadioButton) rootView.findViewById(R.id.complexity_medium);
        complexity_low = (RadioButton) rootView.findViewById(R.id.complexity_low);

        env_group = (RadioGroup) rootView.findViewById(R.id.environment_radiogroup);
        env_noisy = (RadioButton) rootView.findViewById(R.id.environment_noisy);
        env_dnd = (RadioButton) rootView.findViewById(R.id.environment_dnd);

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
                list_listener.onSaveActivity();
                if(fragment_mgr.getBackStackEntryCount() > 0){
                    fragment_mgr.popBackStack();
                }
                break;
            case R.id.save_button:
                Toast.makeText(getActivity().getApplicationContext(), "Event Added", Toast.LENGTH_SHORT).show();
                list_listener.onSaveActivity();
                if(fragment_mgr.getBackStackEntryCount() > 0){
                    fragment_mgr.popBackStack();
                }
                break;
        }
    }



}
