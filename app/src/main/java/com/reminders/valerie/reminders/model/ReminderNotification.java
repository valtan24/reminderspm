package com.reminders.valerie.reminders.model;


import java.text.DateFormatSymbols;

public class ReminderNotification {

    private long reminder_id;
    private String task_title;

    public long getTask_id() {
        return task_id;
    }

    public void setTask_id(long task_id) {
        this.task_id = task_id;
    }

    private long task_id;

    //date time for TASK
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public ReminderNotification(long reminder_id){
        this.reminder_id = reminder_id;
    }
    public long getReminder_id() {
        return reminder_id;
    }

    public void setReminder_id(long reminder_id) {
        this.reminder_id = reminder_id;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getDateString(){
        String month_name = new DateFormatSymbols().getMonths()[month];
        String date_text = "" + day + " " + month_name + " " + year;
        return date_text;
    }

    public String getTimeString(){
        String time_text = "";
        if(hour < 10){
            time_text = time_text + "0" + hour;
        }
        else{
            time_text = time_text + hour;
        }
        if(minute< 10){
            time_text = time_text + ":0" + minute;
        }
        else{
            time_text = time_text + ":" + minute;
        }
        return time_text;
    }

}
