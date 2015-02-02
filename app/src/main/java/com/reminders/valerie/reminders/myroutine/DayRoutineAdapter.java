package com.reminders.valerie.reminders.myroutine;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.PerDayEvent;

import java.util.ArrayList;

public class DayRoutineAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<PerDayEvent> routine_list;
    private static LayoutInflater inflater = null;

    public DayRoutineAdapter(Activity activity, ArrayList<PerDayEvent> event_list){
        this.activity = activity;
        this.routine_list = event_list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View list_row = convertView;
        if(convertView == null){
            list_row = inflater.inflate(R.layout.routine_list_item, null);
        }

        TextView routine_time = (TextView) list_row.findViewById(R.id.routine_time);
        TextView routine_name = (TextView) list_row.findViewById(R.id.routine_name);
        ImageView delete_icon = (ImageView) list_row.findViewById(R.id.routine_item_delete);
        ImageView edit_icon = (ImageView) list_row.findViewById(R.id.routine_item_edit);

        String time_text = "";
        PerDayEvent event = routine_list.get(position);

        time_text = time_text + ( event.getStart_hour() < 10 ? "0" : "") + event.getStart_hour();
        time_text = time_text + ":";
        time_text = time_text + ( event.getStart_minute() < 10 ? "0" : "") + event.getStart_minute();
        time_text = time_text + " - ";
        time_text = time_text + ( event.getEnd_hour() < 10 ? "0" : "") + event.getEnd_hour();
        time_text = time_text + ":";
        time_text = time_text + ( event.getEnd_minute() < 10 ? "0" : "") + event.getEnd_minute();
        routine_time.setText(time_text);

        routine_name.setText(routine_list.get(position).getName());

        delete_icon.setClickable(true);
        delete_icon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Deleting "+routine_list.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        edit_icon.setClickable(true);
        edit_icon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(v.getContext(), "editing "+routine_list.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        return list_row;
    }

}
