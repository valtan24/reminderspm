package com.reminders.valerie.reminders.myprofile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.TodoFragment;
import com.reminders.valerie.reminders.model.DateEditTextManager;
import com.reminders.valerie.reminders.model.DateTimeEditTextMgr;

import java.util.ArrayList;


public class ProfileFragment extends Fragment{

    /*********Views********/
    private Button save_button;
    private EditText profile_name, dob;
    private ImageView add_cat_icon;
    //list
    private ListView cat_list;
    //list - for temp data
    private String[] cat_strings;
    private ArrayAdapter list_adapter;

    /*********Data attributes ***********/
    private int dob_year, dob_month, dob_day;
    private DateTimeEditTextMgr date_et_mgr;

    DatePickerDialog.OnDateSetListener date_listener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dob_year = year;
            dob_month = monthOfYear;
            dob_day = dayOfMonth;
            if(date_et_mgr != null){
                String date_text = date_et_mgr.buildText(dob_year, dob_month, dob_day);
                date_et_mgr.setText(dob, date_text);
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), "Error settings date", Toast.LENGTH_SHORT);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        date_et_mgr = new DateEditTextManager();
        save_button = (Button) rootView.findViewById(R.id.save_profile_button);
        save_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //save items
                Toast.makeText(getActivity().getApplicationContext(), "Data saved", Toast.LENGTH_SHORT).show();
            }
        });
        profile_name = (EditText) rootView.findViewById(R.id.profile_name_edittext);
        dob = (EditText) rootView.findViewById(R.id.profile_dob);
        dob.setClickable(true);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle date_args = new Bundle();
                if(dob_year == 0){
                    dob_year = 1930;
                    dob_month = 0;
                    dob_day = 1;
                }
                date_args.putInt("year", dob_year);
                date_args.putInt("month", dob_month);
                date_args.putInt("day", dob_day);
                date_et_mgr.showPickerFragment(getActivity().getSupportFragmentManager(), date_listener, date_args);
            }
        });
        add_cat_icon = (ImageView) rootView.findViewById(R.id.plus_icon);
        add_cat_icon.setClickable(true);
        add_cat_icon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getActivity().getApplicationContext(), "Category saved", Toast.LENGTH_SHORT).show();
            }
        });
        //populate category list
        cat_list = (ListView) rootView.findViewById(R.id.category_list);
        cat_strings = getActivity().getResources().getStringArray(R.array.category_list);
        list_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, cat_strings);
        cat_list.setAdapter(list_adapter);
        return rootView;
    }

}
