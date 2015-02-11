package com.reminders.valerie.reminders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.RingtoneManager;
import android.net.Uri;
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
    public static final String KEY_SAMETASKREM = "rem_task_same";

    //category table
    public static final String TABLE_CATEGORIES = "reminders_categories";
    public static final String KEY_AUDIOURI = "audio_uri";

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
        String create_category_table = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + "( _id TEXT PRIMARY KEY, " + KEY_AUDIOURI + " TEXT )";
        db.execSQL(create_category_table);

        //add default
        Bundle args = new Bundle();
        args.putString("cat_name", "General");
        addNewCategory(args, db);
        args.putString("cat_name", "Study");
        addNewCategory(args, db);
        args.putString("cat_name", "Work");
        addNewCategory(args, db);
        args.putString("cat_name", "Social");
        addNewCategory(args, db);
        String create_tasks_table = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " ( " + KEY_TASKID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_TASKTITLE + " TEXT, " + KEY_TASKDATE + " TEXT, " + KEY_TASKTIME + " TEXT, " + KEY_IMPORTANCE + " REAL, " + KEY_CATEGORY +" TEXT, "
                + KEY_COMPLETED + " INTEGER, " + KEY_SAMETASKREM + " INTEGER, FOREIGN KEY(" + KEY_CATEGORY + ") REFERENCES " + TABLE_CATEGORIES +"(_id))";
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
        String[] select_columns = {KEY_TASKTITLE, KEY_TASKTIME, KEY_TASKDATE, KEY_TASKID, KEY_SAMETASKREM};
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
        ContentValues values = new ContentValues();
        values.put(KEY_TASKTITLE, task.getTitle());
        values.put(KEY_TASKDATE, task.getTaskDate());
        values.put(KEY_TASKTIME, task.getTaskTime());
        values.put(KEY_COMPLETED, task.getCompleted());
        values.put(KEY_SAMETASKREM, task.getSame_rem_task());
        values.put(KEY_IMPORTANCE, task.getImportance());
        values.put(KEY_CATEGORY, task.getCategory());
        /*String insert_query = "INSERT INTO " + TABLE_TASKS + " ( \"" + KEY_TASKTITLE + "\", '" +
                KEY_TASKDATE + "', '" + KEY_TASKTIME+ "', '" + KEY_COMPLETED + "', '" + KEY_SAMETASKREM + "' ) VALUES ( '" +
                task.getTitle() + "', '" + task.getTaskDate() + "', '" +
                task.getTaskTime() + "', 0, " + task.getSame_rem_task() +")";*/
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TASKS, null, values);
    }

    public void addNewCategory(Bundle args, SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put("_id", args.getString("cat_name"));
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        cv.put(KEY_AUDIOURI, alert.toString());
        db.insert(TABLE_CATEGORIES, null, cv);
    }

    public Cursor getCategoryNames() {
        String[] select_columns = {"_id"};
        return getReadableDatabase().query(TABLE_CATEGORIES, select_columns, null, null, null, null, null);
    }
}
