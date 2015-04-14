package com.reminders.valerie.reminders.myprofile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.Category;
import com.reminders.valerie.reminders.model.DateEditTextManager;
import com.reminders.valerie.reminders.model.DateTimeEditTextMgr;
import com.reminders.valerie.reminders.scheduleview.DeleteDialogFragment;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class ProfileFragment extends Fragment implements AdapterView.OnItemClickListener{

    /*********Views********/
    private Button save_button;
    private EditText profile_name, dob;
    private ImageView add_cat_icon;
    //list
    private ListView cat_list;
    private SimpleCursorAdapter list_adapter;

    /*********Data attributes ***********/
    private int dob_year, dob_month, dob_day;
    private DateTimeEditTextMgr date_et_mgr;

    public static final String INTERNAL_FILENAME = "reminders_pm_internal";

    private String category_selected;

    TaskDBHandler dbhandler;

    //add category listener
    CategoryDialog.OnCategorySetListener add_listener = new AddCategoryDialog.OnCategorySetListener() {
        @Override
        public void OnCategorySet(int choice) {
            switch(choice){
                case CategoryDialog.CANCEL: //cancel
                    //TODO CANCEL ADD CATEGORY
                    break;
                case CategoryDialog.SAVE:
                    Cursor cursor = dbhandler.getCategoryNames();
                    list_adapter.changeCursor(cursor);
                    break;
            }
        }
    };

    DeleteDialogFragment.OnDeleteSetListener delete_listener = new DeleteDialogFragment.OnDeleteSetListener(){
        @Override
        public void OnDeleteSet(int choice) {
            switch (choice) {
                case 0: //cancel
                    //TODO DELETE CANCELLED
                    break;
                case 1:
                    if(dbhandler != null){
                        if(dbhandler.deleteCategory(category_selected)){
                            Toast.makeText(getActivity().getApplicationContext(), "Category deleted", Toast.LENGTH_SHORT).show();
                            Cursor cursor = dbhandler.getCategoryNames();
                            list_adapter.changeCursor(cursor);
                            category_selected = "";
                        }
                        else{
                            Toast.makeText(getActivity().getApplicationContext(), "Sorry, category could not be deleted. Do you have task or activity under this category?", Toast.LENGTH_LONG).show();
                            category_selected = "";
                        }
                    }
                    break;
                default:
                    Log.d("Error", "invalid choice");
            }
        }
    };

    CategoryDialog.OnCategorySetListener edit_listener = new CategoryDialog.OnCategorySetListener() {
        @Override
        public void OnCategorySet(int choice) {
            switch(choice){
                case CategoryDialog.CANCEL:
                    //TODO EDIT CANCELED
                    category_selected = "";
                    break;
                case CategoryDialog.SAVE:
                    Cursor cursor = dbhandler.getCategoryNames();
                    list_adapter.changeCursor(cursor);
                    category_selected = "";
                    break;
                case CategoryDialog.DELETE:
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
                try {
                    FileOutputStream internal_file = getActivity().getApplicationContext().openFileOutput(INTERNAL_FILENAME, Context.MODE_PRIVATE);
                    internal_file.write(profile_name.getText().toString().getBytes());
                    internal_file.write(("\n").getBytes());
                    internal_file.write((""+dob_year+"\n").getBytes());
                    internal_file.write((""+dob_month+"\n").getBytes());
                    internal_file.write((""+dob_day+"\n").getBytes());
                    Toast.makeText(getActivity(), "Data saved", Toast.LENGTH_SHORT).show();
                }
                catch(FileNotFoundException e){
                    Log.e("Profile fragment", "file not found");
                }
                catch(IOException e){
                    Log.e("{Profile fragment", "Write error");
                }
            }
        });
        profile_name = (EditText) rootView.findViewById(R.id.profile_name_edittext);
        //get internal file
        try{
            FileInputStream internal_input = getActivity().getApplicationContext().openFileInput(INTERNAL_FILENAME);
            BufferedReader input_br = new BufferedReader(new InputStreamReader(new DataInputStream(internal_input)));
            String name_in = input_br.readLine();
            profile_name.setText(name_in);
            dob_year = Integer.parseInt(input_br.readLine());
            dob_month = Integer.parseInt(input_br.readLine());
            dob_day = Integer.parseInt(input_br.readLine());
        }
        catch(FileNotFoundException e){
            Log.i("Profile fragment", "internal file don't exist");
        }
        catch(IOException e){
            Log.e("Profile fragment", "unable to read from file");
        }

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
                add_category.setCallBack(add_listener);
                add_category.show(fragment_manager, "dialog");
            }
        });
        //populate category list
        cat_list = (ListView) rootView.findViewById(R.id.category_list);
        dbhandler = new TaskDBHandler(getActivity().getApplicationContext());
        Cursor cursor = dbhandler.getCategoryNames();
        String[] from = new String[]{"_id"};
        int[] to = {android.R.id.text1};
        list_adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, cursor, from, to, 0);
        cat_list.setAdapter(list_adapter);
        cat_list.setOnItemClickListener(this);

        if(dob_year != 0){
            date_et_mgr.setText(dob, date_et_mgr.buildText(dob_year, dob_month, dob_day));
        }

        return rootView;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CategoryDialog edit_cat = new EditCategoryDialog();
        edit_cat.setCallBack(edit_listener);
        Category category = dbhandler.getCategory(position);
        Bundle args = new Bundle();
        category_selected = category.getCategory_title();
        args.putString("category", category.getCategory_title());
        args.putString("uri", category.getAudio_uri());
        args.putDouble("motivation", category.getMotivation());
        edit_cat.setArguments(args);
        edit_cat.show(getActivity().getSupportFragmentManager(), "dialog");
    }
}
