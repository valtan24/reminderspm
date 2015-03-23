package com.reminders.valerie.reminders.model;


import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.reminders.valerie.reminders.TaskDBHandler;

public class CursorToBundle {
    public static Bundle getTaskByPosition(Cursor cursor, int position){
        cursor.moveToPosition(position);
        Bundle args = getTaskFromCursor(cursor);
        return args;
    }

    public static Reminder getReminderFromCursor(Cursor cursor){
        int column_index = cursor.getColumnIndex("_id");
        Reminder reminder = new Reminder();
        reminder.setId(cursor.getLong(column_index));

        column_index = cursor.getColumnIndex(TaskDBHandler.KEY_REMDATE);
        String date = cursor.getString(column_index);
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7));
        int day = Integer.parseInt(date.substring(8,10));
        reminder.setYear(year);
        reminder.setMonth(month);
        reminder.setDay(day);

        column_index = cursor.getColumnIndex(TaskDBHandler.KEY_REMTIME);
        String time = cursor.getString(column_index);
        int hour = Integer.parseInt(time.substring(0,2));
        int minute = Integer.parseInt(time.substring(3,5));
        reminder.setHour(hour);
        reminder.setMinute(minute);

        column_index = cursor.getColumnIndex(TaskDBHandler.KEY_TASKFK);
        reminder.setTask_id(cursor.getLong(column_index));

        column_index = cursor.getColumnIndex(TaskDBHandler.KEY_AUDIO);
        reminder.setWith_audio(cursor.getInt(column_index));

        column_index = cursor.getColumnIndex(TaskDBHandler.KEY_FIRED);
        reminder.setIs_fired(cursor.getInt(column_index));
        return reminder;
    }

    public static Bundle getTaskFromCursor(Cursor cursor){
        Bundle args = new Bundle();
        int column_index = cursor.getColumnIndex(TaskDBHandler.KEY_TASKTITLE);
        args.putString("title", cursor.getString(column_index));
        column_index = cursor.getColumnIndex(TaskDBHandler.KEY_TASKDATE);
        String date = cursor.getString(column_index);
        //get year month day
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7));
        int day = Integer.parseInt(date.substring(8,10));
        args.putInt("task_year", year);
        args.putInt("task_month", month);
        args.putInt("task_day", day);

        //get hour and minute
        column_index = cursor.getColumnIndex(TaskDBHandler.KEY_TASKTIME);
        String time = cursor.getString(column_index);
        int hour = Integer.parseInt(time.substring(0,2));
        int minute = Integer.parseInt(time.substring(3,5));
        args.putInt("task_hour", hour);
        args.putInt("task_minute", minute);
        column_index = cursor.getColumnIndex(TaskDBHandler.KEY_SAMETASKREM);
        args.putInt("same_datetime", cursor.getInt(column_index));
        int id_index = cursor.getColumnIndex("_id");
        args.putLong("task_id", cursor.getLong(id_index));

        column_index = cursor.getColumnIndex(TaskDBHandler.KEY_CATEGORY);
        args.putString("category", cursor.getString(column_index));

        column_index = cursor.getColumnIndex(TaskDBHandler.KEY_IMPORTANCE);
        args.putDouble("importance", cursor.getDouble(column_index));

        column_index = cursor.getColumnIndex(TaskDBHandler.KEY_COMPLETED);
        args.putInt("completed", cursor.getInt(column_index));

        return args;
    }
}
