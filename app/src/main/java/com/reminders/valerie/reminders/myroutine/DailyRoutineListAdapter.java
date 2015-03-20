package com.reminders.valerie.reminders.myroutine;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.DailyActivity;

import java.util.ArrayList;

public class DailyRoutineListAdapter extends BaseAdapter {

    private ArrayList<DailyActivity> routine_list;
    private static LayoutInflater inflater = null;

    public DailyRoutineListAdapter(Activity activity, ArrayList<DailyActivity> routine_list){
        this.routine_list = routine_list;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return routine_list.size();
    }

    @Override
    public Object getItem(int position) {
        return routine_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View list_row = convertView;
        if(convertView == null){
            list_row = inflater.inflate(R.layout.daily_routine_list_item, null);
        }

        TextView time_text = (TextView) list_row.findViewById(R.id.time_text);
        TextView event_text = (TextView) list_row.findViewById(R.id.event_text);

        time_text.setText(routine_list.get(position).getTime());
        event_text.setText(routine_list.get(position).getName());
        return list_row;
    }
}
