package com.reminders.valerie.reminders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public TaskDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_tasks_table = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + "( " + KEY_TASKID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TASKTITLE + " TEXT, " + KEY_TASKDATE + " TEXT, " + KEY_TASKTIME + " TEXT, " + KEY_COMPLETED + " INTEGER)";
        db.execSQL(create_tasks_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public long insert(String table_name, ContentValues values) throws Exception {
        validate(values);
        return getWritableDatabase().insert(table_name, null, values);
    }

    private void validate(ContentValues values) throws Exception {
        if (!values.containsKey(KEY_TASKTITLE) || values.getAsString(KEY_TASKTITLE) == null || values.getAsString(KEY_TASKTITLE).isEmpty()) {

            throw new Exception("There must be task title");

        }
    }

    public Cursor query(String table_name, String ordered_by) {

        String[] projection = {KEY_TASKID, KEY_TASKTITLE};

        return getReadableDatabase().query(TABLE_TASKS, projection, null, null, null, null, ordered_by);
    }

    //============ CRUD ============//
    //add new task
    /*public void addTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASKTITLE, task.getTaskTitle());
        values.put(KEY_TASKDATE, task.getTaskDate().toString());
        values.put(KEY_TASKTIME, task.getTaskTime().toString());
        values.put(KEY_COMPLETED, (task.isCompleted() ? 1 : 0));
        // Inserting Row
        db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
    }*/



}
