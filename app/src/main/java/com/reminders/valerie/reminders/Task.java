package com.reminders.valerie.reminders;


import android.text.format.Time;

import java.util.Date;

public class Task {

    private String task_title;
    private Time task_time;
    private Date task_date;
    private boolean completed;

    public Task(){

    }

    //============ Getters ===============//
    public String getTaskTitle(){
        return task_title;
    }

    public Time getTaskTime(){
        return task_time;
    }

    public Date getTaskDate(){
        return task_date;
    }

    public boolean isCompleted(){
        return completed;
    }

    //============= Setters =============//

    public void setTaskTitle(String title){
        task_title = title;
    }

    public void setTaskTime(Time time){
        task_time = time;
    }

    public void setTaskDate(Date date){
        task_date = date;
    }

    public void markCompleted(boolean completed){
        this.completed = completed;
    }


}
