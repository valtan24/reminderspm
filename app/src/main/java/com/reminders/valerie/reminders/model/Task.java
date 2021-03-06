package com.reminders.valerie.reminders.model;

/**
 * Created by valerie on 1/27/2015.
 */
public class Task {

    public static final double IMPORTANCE_HIGH = 0.9;
    public static final double IMPORTANCE_MEDIUM = 0.49;
    public static final double IMPORTANCE_LOW = 0.09;
    private long task_id;
    private String title;
    //date
    private int year;
    private int month;
    private int day;

    //time in 24-h format
    private int hour;
    private int minute;

    private int completed;
    private String category;
    private double importance;

    public int getSame_rem_task() {
        return same_rem_task;
    }

    public void setSame_rem_task(int same_rem_task) {
        this.same_rem_task = same_rem_task;
    }

    private int same_rem_task;

    public long getTask_id() {
        return task_id;
    }

    public void setTask_id(long task_id) {
        this.task_id = task_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getImportance() {
        return importance;
    }

    public void setImportance(double importance) {
        this.importance = importance;
    }



    //TODO REMOVE AND USE DateTimeConverter.convertDateToDBText()
    public String getTaskDate(){
        String date_text = "";
        date_text = date_text + year + "-" + (month < 10 ? "0" : "") + month + "-" + (day < 10 ? "0" : "") + day;
        return date_text;
    }

    public String getTaskTime(){
        String task_time = "";
        task_time = task_time + (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute;
        return task_time;
    }

}
