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
import android.widget.AdapterView;
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
import com.reminders.valerie.reminders.scheduleview.DeleteDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;


public class ProfileFragment extends Fragment implements AdapterView.OnItemClickListener{

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

    //add category listener
    AddCategoryDialog.OnAddSetListener add_cat_listener = new AddCategoryDialog.OnAddSetListener() {
        @Override
        public void OnAddSet(int choice) {
            switch(choice){
                case 0: //cancel
                    break;
                case 1:
                    //check inputs, add into database
                    Toast.makeText(getActivity().getApplicationContext(), "Category added", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    DeleteDialogFragment.OnDeleteSetListener delete_listener = new DeleteDialogFragment.OnDeleteSetListener(){
        @Override
        public void OnDeleteSet(int choice) {
            switch (choice) {
                case 0: //cancel
                    //do nothing;
                    break;
                case 1:
                    Toast.makeText(getActivity().getApplicationContext(), "category deleted", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Log.d("Error", "invalid choice");
            }
        }
    };

    EditCategoryDialog.OnAddSetListener edit_cat_listener = new EditCategoryDialog.OnAddSetListener(){

        @Override
        public void OnAddSet(int choice) {
            switch(choice){
                case 0:
                    //cancel
                    break;
                case 1:

                    Toast.makeText(getActivity().getApplicationContext(), "category saved", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    DeleteDialogFragment delete_fragment = new DeleteDialogFragment();
                    delete_fragment.setTitle("Delete Category");
                    delete_fragment.setCallBack(delete_listener);
                    delete_fragment.show(getActivity().getSupportFragmentManager(), "dialog");
                    break;
            }

        }
    };

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
                FragmentManager fragment_manager = getActivity().getSupportFragmentManager();
                AddCategoryDialog add_category = new AddCategoryDialog();
                add_category.setCallBack(add_cat_listener);
                add_category.setTitle(getActivity().getResources().getText(R.string.add_cat_title));
                add_category.show(fragment_manager, "dialog");
            }
        });
        //populate category list
        cat_list = (ListView) rootView.findViewById(R.id.category_list);
        cat_strings = getActivity().getResources().getStringArray(R.array.category_list);
        list_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, cat_strings);
        cat_list.setAdapter(list_adapter);
        cat_list.setOnItemClickListener(this);
        return rootView;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EditCategoryDialog edit_cat = new EditCategoryDialog();
        edit_cat.setCallBack(edit_cat_listener);
        edit_cat.setSet_neutral_button(true);
        edit_cat.setTitle(getActivity().getResources().getText(R.string.edit_category));
        edit_cat.setCat_name(cat_strings[position]);
        edit_cat.show(getActivity().getSupportFragmentManager(), "dialog");
    }
}
