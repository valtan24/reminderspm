package com.reminders.valerie.reminders;

/**
 * Created by valerie on 1/27/2015.
 */
public class ScheduleCalculator {

    private static ScheduleCalculator instance = null;

    public static ScheduleCalculator getInstance(){
        if(instance == null){
            instance =  new ScheduleCalculator();
        }
        return instance;
    }
    private Task task;

    public void setTask(Task task){
        this.task = task;
    }

    public Task getTask(){
        return task;
    }
}
