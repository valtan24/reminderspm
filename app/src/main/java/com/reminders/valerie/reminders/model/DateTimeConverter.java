package com.reminders.valerie.reminders.model;

/**
 * Created by valerie on 2/12/2015.
 */
public class DateTimeConverter {

    public static String convertDateToDBText(int year, int month, int day){
        String date_text = "";
        date_text = date_text + year + "-" + (month < 10 ? "0" : "") + month + "-" + (day < 10 ? "0" : "") + day;
        return date_text;
    }

    public static String convertTimeToDBText(int hour, int minute){
        String task_time = "";
        task_time = task_time + (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute;
        return task_time;
    }
}
