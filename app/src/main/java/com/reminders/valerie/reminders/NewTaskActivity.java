package com.reminders.valerie.reminders;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;


public class NewTaskActivity extends ActionBarActivity {

    private TextView text_date_output;
    private Button button_change_date;
    private int year;
    private int month;
    private int day;



    static final int DATE_PICKER_ID = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
/*
        text_date_output = (TextView) findViewById(R.id.date_output);
        button_change_date = (Button) findViewById(R.id.change_date);

        //get current date by calendar
        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        //show current date
        text_date_output.setText(month+1 + "-" + day + "-" + year);

        //button listener to show date picker dialog
        button_change_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);
            }
        });*/
    }

/*
    @Override
    protected Dialog onCreateDialog(int id){
        switch(id){
            case DATE_PICKER_ID:
                return new DatePickerDialog(this, pickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int selected_year, int monthOfYear, int dayOfMonth) {
            year = selected_year;
            month = monthOfYear;
            day = dayOfMonth;

            //show selected date
            text_date_output.setText(month+1 + "-" + day + "-" + year);
        }
    };*/
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    */
}
