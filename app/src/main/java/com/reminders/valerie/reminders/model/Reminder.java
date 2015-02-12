package com.reminders.valerie.reminders.model;


import android.os.Bundle;

public class Reminder {


    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int task_id;
    private int with_audio;
    private Task task;

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

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getWith_audio() {
        return with_audio;
    }

    public void setWith_audio(int with_audio) {
        this.with_audio = with_audio;
    }

    public void setTask(Task task){
        this.task = task;
    }

    public Task getTask(){
        return task;
    }
}
