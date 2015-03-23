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

import com.reminders.valerie.reminders.model.Category;
import com.reminders.valerie.reminders.model.DateTimeConverter;
import com.reminders.valerie.reminders.model.Reminder;
import com.reminders.valerie.reminders.model.Task;
import com.reminders.valerie.reminders.model.CursorToBundle;

import java.util.ArrayList;

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
    public static final String KEY_MOTIVATION = "motivation";

    //reminders table
    public static final String TABLE_REMINDERS = "reminders_reminders";
    public static final String KEY_REMDATE = "remdate";
    public static final String KEY_REMTIME = "remtime";
    public static final String KEY_TASKFK = "task_id";
    public static final String KEY_AUDIO = "with_audio";
    public static final String KEY_FIRED = "is_fired";

    //daily routine events
    public static final String TABLE_ACTIVITIES = "reminders_activities";
    public static final String KEY_DAY = "day";
    public static final String KEY_START = "start_time";
    public static final String KEY_END = "end_time";
    public static final String KEY_COMPLEXITY = "complexity";
    public static final String KEY_ACT_CATEGORY = "category";

    public TaskDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_category_table = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + "( _id TEXT PRIMARY KEY, " + KEY_AUDIOURI + " TEXT, " + KEY_MOTIVATION + " REAL )";
        db.execSQL(create_category_table);

        //TODO ADD DEFAULT ONLY IN FIRST RUN USING SHARED PREFERENCES
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
                + KEY_REMTIME + " TEXT, " + KEY_TASKFK + " INTEGER, " + KEY_AUDIO + " INTEGER, " + KEY_FIRED + " INTEGER, FOREIGN KEY (" + KEY_TASKFK
                + ") REFERENCES " + TABLE_TASKS + "(_id))";
        db.execSQL(create_reminders_table);

        String create_activities_table = "CREATE TABLE IF NOT EXISTS " + TABLE_ACTIVITIES +  " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DAY + " INTEGER, "
                 + KEY_START + " TEXT, " + KEY_END + " TEXT, " + KEY_COMPLEXITY + " REAL, " + KEY_ACT_CATEGORY + " TEXT, FOREIGN KEY (" + KEY_ACT_CATEGORY + ") REFERENCES " + TABLE_CATEGORIES + "(_id))";
        db.execSQL(create_activities_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public Cursor getUncompletedTasks(String ordered_by){
        String where_args[] = {"0"};
        String[] select_columns = {KEY_TASKTITLE, KEY_TASKTIME, KEY_TASKDATE, KEY_TASKID, KEY_SAMETASKREM, KEY_CATEGORY, KEY_IMPORTANCE};
        return getReadableDatabase().query(TABLE_TASKS, select_columns, KEY_COMPLETED + " = ? ", where_args, null, null, ordered_by);
    }

    //TODO CHANGE TO INT TO RETRIEVE ID OF TASK
    public long addNewTask(Task task){
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
        long row_id = db.insert(TABLE_TASKS, null, values);
        db.close();
        return row_id;

    }

    //for default categories
    public void addNewCategory(Bundle args, SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put("_id", args.getString("cat_name"));
        cv.put(KEY_MOTIVATION, 0.5);
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        cv.put(KEY_AUDIOURI, alert.toString());
        db.insert(TABLE_CATEGORIES, null, cv);
    }

    //TODO CHANGE TO BOOLEAN
    public void addNewCategory(Category c){
        ContentValues values = new ContentValues();
        values.put("_id", c.getCategory_title());
        values.put(KEY_AUDIOURI, c.getAudio_uri());
        values.put(KEY_MOTIVATION, c.getMotivation());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    public Cursor getCategoryNames() {
        String[] select_columns = {"_id"};
        return getReadableDatabase().query(TABLE_CATEGORIES, select_columns, null, null, null, null, "_id");
    }

    public Category getCategory(int position) {
        Category c = new Category();
        String[] select_columns = {"_id", KEY_AUDIOURI, KEY_MOTIVATION};
        Cursor cursor = getReadableDatabase().query(TABLE_CATEGORIES, select_columns, null, null, null, null, "_id");
        cursor.moveToPosition(position);
        int title_index = cursor.getColumnIndex("_id");
        c.setCategory_title(cursor.getString(title_index));
        int uri_index = cursor.getColumnIndex(KEY_AUDIOURI);
        c.setAudio_uri(cursor.getString(uri_index));
        int motivation_index = cursor.getColumnIndex(KEY_MOTIVATION);
        c.setMotivation(cursor.getDouble(motivation_index));
        return c;
    }

    public boolean addReminders(ArrayList<Reminder> reminders, long task_id) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            if (reminders.size() <= 0) {
                Log.e("No reminders", "There are no reminders to be saved");
                return false;
            }
            for (int i = 0; i < reminders.size(); i++) {
                int year = reminders.get(i).getYear();
                int month = reminders.get(i).getMonth();
                int day = reminders.get(i).getDay();
                cv.put(KEY_REMDATE, DateTimeConverter.convertDateToDBText(year, month, day));
                int hour = reminders.get(i).getHour();
                int minute = reminders.get(i).getMinute();
                cv.put(KEY_REMTIME, DateTimeConverter.convertTimeToDBText(hour, minute));
                cv.put(KEY_TASKFK, task_id);
                cv.put(KEY_AUDIO, reminders.get(i).getWith_audio());
                cv.put(KEY_FIRED, 0);
                db.insert(TABLE_REMINDERS, null, cv);
            }
            db.close();
            return true;
        }
        catch (Exception e) {
            Log.e("Exception", e.getMessage());
            return false;
        }
    }

    public boolean updateReminder(Reminder reminder){
        ContentValues update_value = new ContentValues();
        update_value.put(KEY_REMDATE, DateTimeConverter.convertDateToDBText(reminder.getYear(), reminder.getMonth(), reminder.getDay()));
        update_value.put(KEY_REMTIME, DateTimeConverter.convertTimeToDBText(reminder.getHour(), reminder.getMinute()));
        update_value.put(KEY_TASKFK, reminder.getTask_id());
        update_value.put(KEY_AUDIO, reminder.getWith_audio());
        update_value.put(KEY_FIRED, reminder.getIs_fired());
        SQLiteDatabase db = getWritableDatabase();
        String[] where_args = {"" + reminder.getId()};
        if(db.updateWithOnConflict(TABLE_REMINDERS, update_value, "_id = ?", where_args, SQLiteDatabase.CONFLICT_ROLLBACK) == 1){
            db.close();
            return true;
        }
        else{
            Log.e("db error", "failed to update reminder "+reminder.getId());
            return false;
        }
    }

    public boolean updateTask(Task task){
        ContentValues update_value = new ContentValues();
        update_value.put(KEY_TASKTITLE, task.getTitle());
        update_value.put(KEY_TASKDATE, task.getTaskDate());
        update_value.put(KEY_TASKTIME, task.getTaskTime());
        update_value.put(KEY_COMPLETED, task.getCompleted());
        update_value.put(KEY_IMPORTANCE, task.getImportance());
        update_value.put(KEY_CATEGORY, task.getCategory());
        update_value.put(KEY_SAMETASKREM, task.getSame_rem_task());
        SQLiteDatabase db = getWritableDatabase();
        String[] where_args = {""+task.getTask_id()};
        if(db.updateWithOnConflict(TABLE_TASKS, update_value, KEY_TASKID + " = ?", where_args, SQLiteDatabase.CONFLICT_ROLLBACK) == 1){
            db.close();
            return true; //successful update
        }
        else{
            //TODO THROW EXCEPTION? OR TOAST HERE?
            Log.d("db error", "failed to update task");
            return false;
        }
    }

    public boolean deleteReminder(Reminder reminder){
        SQLiteDatabase db = getWritableDatabase();
        String[] where_args = {""+reminder.getId()};
        if(db.delete(TABLE_REMINDERS, "_id = ?", where_args) == 1){
            return true;
        }
        else{
            return false;
        }
    }

    public ArrayList<Reminder> getUnfiredReminders(long task_id){
        ArrayList<Reminder> reminder_list = new ArrayList<Reminder>();
        String[] where_args = {""+task_id, "0"};
        Cursor cursor = getReadableDatabase().query(TABLE_REMINDERS, null, KEY_TASKFK + " = ? AND " + KEY_FIRED + " = ?", where_args, null, null, KEY_REMDATE + ", " + KEY_REMTIME );
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            reminder_list.add(CursorToBundle.getReminderFromCursor(cursor));
            cursor.moveToNext();
        }

        return reminder_list;
    }

    public boolean markTaskAsComplete(Task task){
        ContentValues update_value = new ContentValues();
        update_value.put(KEY_COMPLETED, task.getCompleted());
        SQLiteDatabase db = getWritableDatabase();
        String[] where_args = {""+task.getTask_id()};
        if(db.updateWithOnConflict(TABLE_TASKS, update_value, KEY_TASKID + " = ?", where_args, SQLiteDatabase.CONFLICT_ROLLBACK) == 1){
            //TODO MARK REMINDERS AS FIRED
            db.close();
            return true; //successful update
        }
        else{
            //TODO THROW EXCEPTION? OR TOAST HERE?
            Log.d("db error", "failed to update task");
            return false;
        }
    }

    public Reminder getNextReminder(long task_id){
        ArrayList<Reminder> reminder_list = getUnfiredReminders(task_id);
        return reminder_list.get(0);
    }

    public Bundle getTaskById(long task_id){
        String[] where_args = {""+task_id, "0"};
        Cursor cursor = getReadableDatabase().query(
                TABLE_TASKS, null, KEY_TASKID + " = ? AND " + KEY_COMPLETED + " = ?",
                where_args, null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()){
            return CursorToBundle.getTaskFromCursor(cursor);
        }
        return null;
    }

    public boolean markReminderAsFired(long reminder_id){
        ContentValues update_value = new ContentValues();
        update_value.put(KEY_FIRED, 1);
        SQLiteDatabase db = getWritableDatabase();
        String[] where_args = {""+reminder_id};
        if(db.updateWithOnConflict(TABLE_REMINDERS, update_value, "_id = ?", where_args, SQLiteDatabase.CONFLICT_ROLLBACK) == 1){
            db.close();
            return true;
        }
        return false;
    }

}
