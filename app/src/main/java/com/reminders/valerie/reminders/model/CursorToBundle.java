package com.reminders.valerie.reminders.model;


import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.reminders.valerie.reminders.TaskDBHandler;

public class CursorToBundle {
    public static Bundle getTaskByPosition(Cursor cursor, int position){
        cursor.moveToPosition(position);
        Bundle args = new Bundle();
        if(cursor != null && cursor.getCount() > 0){
            Log.d("cursor", "notempty");
        }
        int title_index = cursor.getColumnIndex(TaskDBHandler.KEY_TASKTITLE);
        Log.d("index", ""+position);
        args.putString("title", cursor.getString(title_index));
        int date_index = cursor.getColumnIndex(TaskDBHandler.KEY_TASKDATE);
        String date = cursor.getString(date_index);
        //get year month day
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7));
        int day = Integer.parseInt(date.substring(8,10));
        args.putInt("task_year", year);
        args.putInt("task_month", month);
        args.putInt("task_day", day);

        //get hour and minute
        int time_index = cursor.getColumnIndex(TaskDBHandler.KEY_TASKTIME);
        String time = cursor.getString(time_index);
        int hour = Integer.parseInt(time.substring(0,2));
        int minute = Integer.parseInt(time.substring(3,5));
        args.putInt("task_hour", hour);
        args.putInt("task_minute", minute);
        return args;
    }
}
