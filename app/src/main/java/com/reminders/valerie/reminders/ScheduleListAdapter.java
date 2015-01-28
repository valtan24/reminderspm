package com.reminders.valerie.reminders;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ScheduleListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Reminder> reminder_list;
    private static LayoutInflater inflater = null;

    DateTimeEditTextMgr date_et_mgr,time_et_mgr;


    public ScheduleListAdapter(Activity activity, ArrayList<Reminder> reminder_list){
        this.activity = activity;
        this.reminder_list = reminder_list;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        date_et_mgr = new DateEditTextManager();
        time_et_mgr = new TimeEditTextManager();
    }

    @Override
    public int getCount() {
        return reminder_list.size();
    }

    @Override
    public Object getItem(int position) {
        return reminder_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View list_row = convertView;
        if(convertView == null) {
            list_row = inflater.inflate(R.layout.schedule_list_item, null);
        }
        TextView date_time = (TextView) list_row.findViewById(R.id.schedule_item_datetime);
        ImageView audio_icon = (ImageView) list_row.findViewById(R.id.schedule_item_audio);
        ImageView edit_icon = (ImageView) list_row.findViewById(R.id.schedule_item_edit); //may be can remove this

        Reminder reminder = reminder_list.get(position);
        String date_text = date_et_mgr.buildText(reminder.getYear(), reminder.getMonth(), reminder.getDay());
        String time_text = time_et_mgr.buildText(reminder.getHour(), reminder.getMinute(), 0);
        date_time.setText(date_text + ", " + time_text);

        //check if audio
        if(reminder.getWith_audio() == 0){
            audio_icon.setVisibility(View.INVISIBLE);
        }
        return list_row;
    }
}
