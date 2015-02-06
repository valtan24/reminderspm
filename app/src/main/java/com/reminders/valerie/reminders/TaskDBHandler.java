package com.reminders.valerie.reminders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import com.reminders.valerie.reminders.model.Task;

public class TaskDBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "remindersDB";
    private static final int DB_VERSION = 1;

    //tasks table
    public static final String TABLE_TASKS = "reminders_tasks";
    public static final String KEY_TASKID = "_id";
    public static final String KEY_TASKTITLE = "title";
    public static final String KEY_TASKDATE = "taskdate";
    public static final String KEY_TASKTIME = "tasktime";
    public static final String KEY_COMPLETED = "completed";
    public static final String KEY_IMPORTANCE = "importance";
    public static final String KEY_CATEGORY = "category";

    //category table
    public static final String TABLE_CATEGORIES = "reminders_categories";

    //reminders table
    public static final String TABLE_REMINDERS = "reminders_reminders";
    public static final String KEY_REMDATE = "remdate";
    public static final String KEY_REMTIME = "remtime";
    public static final String KEY_TASKFK = "task_id";
    public static final String KEY_AUDIO = "with_audio";

    public TaskDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_category_table = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + "( _id TEXT PRIMARY KEY )";
        db.execSQL(create_category_table);

        //add default
        Bundle args = new Bundle();
        args.putString("cat_name", "General");
        addNewCategory(args, db);
        args.putString("cat_name", "Study");
        addNewCategory(args, db);
        String create_tasks_table = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " ( " + KEY_TASKID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_TASKTITLE + " TEXT, " + KEY_TASKDATE + " TEXT, " + KEY_TASKTIME + " TEXT, " + KEY_IMPORTANCE + " REAL, " + KEY_CATEGORY +" TEXT, "
                + KEY_COMPLETED + " INTEGER, FOREIGN KEY(" + KEY_CATEGORY + ") REFERENCES " + TABLE_CATEGORIES +"(_id))";
        db.execSQL(create_tasks_table);
        String create_reminders_table = "CREATE TABLE IF NOT EXISTS " + TABLE_REMINDERS + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_REMDATE + " TEXT, "
                + KEY_REMTIME + " TEXT, " + KEY_TASKFK + " INTEGER, " + KEY_AUDIO + " INTEGER, FOREIGN KEY (" + KEY_TASKFK + ") REFERENCES " + TABLE_TASKS + "(_id))";
        db.execSQL(create_reminders_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public Cursor getUncompletedTasks(String ordered_by){
        String where_args[] = {"0"};
        String[] select_columns = {KEY_TASKTITLE, KEY_TASKTIME, KEY_TASKDATE, KEY_TASKID};
        return getReadableDatabase().query(TABLE_TASKS, select_columns, KEY_COMPLETED + " = ? ", where_args, null, null, ordered_by);
    }

    public void addNewTask(Bundle args){
        String insert_query = "INSERT INTO " + TABLE_TASKS + " ( \"" + KEY_TASKTITLE + "\", '" +
                KEY_TASKDATE + "', '" + KEY_TASKTIME+ "', '" + KEY_COMPLETED + "' ) VALUES ( '" +
                args.getString("task_title") + "', '" + args.getString("task_date") + "', '" +
                args.getString("task_time") + "', 0)";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(insert_query);
    }

    public void addNewTask(Task task){
        String insert_query = "INSERT INTO " + TABLE_TASKS + " ( \"" + KEY_TASKTITLE + "\", '" +
                KEY_TASKDATE + "', '" + KEY_TASKTIME+ "', '" + KEY_COMPLETED + "' ) VALUES ( '" +
                task.getTitle() + "', '" + task.getTaskDate() + "', '" +
                task.getTaskTime() + "', 0)";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(insert_query);
    }

    public void addNewCategory(Bundle args, SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put("_id", args.getString("cat_name"));
        db.insert(TABLE_CATEGORIES, null, cv);
    }

}
