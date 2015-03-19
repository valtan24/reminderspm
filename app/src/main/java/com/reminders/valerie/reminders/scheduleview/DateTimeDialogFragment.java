package com.reminders.valerie.reminders.scheduleview;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.reminders.valerie.reminders.model.DateEditTextManager;
import com.reminders.valerie.reminders.model.DateTimeEditTextMgr;
import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.TimeEditTextManager;

public class DateTimeDialogFragment extends DialogFragment {

    private TextView date_text;
    private TextView time_text;
    private ImageView ic_date, ic_time;
    private int year, month, day, hour, minute;

    DateTimeEditTextMgr date_et_mgr, time_et_mgr;
    OnDateTimeSetListener listener;

    //icon listeners
    DatePickerDialog.OnDateSetListener date_listener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year_picker, int monthOfYear, int dayOfMonth) {
            year = year_picker;
            month = monthOfYear;
            day = dayOfMonth;
            date_text.setText(date_et_mgr.buildText(year, month, day));
        }
    };

    TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute_picker) {
            hour = hourOfDay;
            minute = minute_picker;
            time_text.setText(time_et_mgr.buildText(hour, minute, 0));
        }
    };

    public interface OnDateTimeSetListener{
        public void OnDateTimeSet(Bundle args);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        year = getArguments().getInt("year");
        month = getArguments().getInt("month");
        day = getArguments().getInt("day");
        hour = getArguments().getInt("hour");
        minute = getArguments().getInt("minute");
        date_et_mgr = new DateEditTextManager();
        time_et_mgr = new TimeEditTextManager();

        builder.setTitle(getArguments().getString("title"));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog_content = inflater.inflate(R.layout.datetime_dialog_fragment, null);
        //views in content
        date_text = (TextView) dialog_content.findViewById(R.id.reminder_date_text);
        time_text = (TextView) dialog_content.findViewById(R.id.reminder_time_text);
        date_text.setText(date_et_mgr.buildText(year, month, day));
        time_text.setText(time_et_mgr.buildText(hour, minute, 0));
        ic_date = (ImageView) dialog_content.findViewById(R.id.ic_date);
        ic_time = (ImageView) dialog_content.findViewById(R.id.ic_time);

        //clickable icons
        ic_date.setClickable(true);
        ic_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle date_args = new Bundle();
                date_args.putInt("year", year);
                date_args.putInt("month", month);
                date_args.putInt("day", day);
                date_et_mgr.showPickerFragment(getActivity().getSupportFragmentManager(), date_listener, date_args);
            }
        });

        ic_time.setClickable(true);
        ic_time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Bundle time_args = new Bundle();
                time_args.putInt("hour",hour);
                time_args.putInt("minute", minute);
                time_et_mgr.showPickerFragment(getActivity().getSupportFragmentManager(), time_listener, time_args);
            }
        });


        builder.setView(dialog_content);
        builder.setPositiveButton(R.string.save_button, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle args = new Bundle();
                args.putInt("hour", hour);
                args.putInt("minute", minute);
                args.putInt("year", year);
                args.putInt("month", month);
                args.putInt("day", day);
                if(listener != null && listener instanceof OnDateTimeSetListener){
                    listener.OnDateTimeSet(args);
                }
                else{
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    public void setCallBack(OnDateTimeSetListener listener){
        this.listener = listener;
    }

}
