package com.reminders.valerie.reminders.model;

import java.util.ArrayList;

/**
 * Created by valerie on 1/27/2015.
 */
public class ScheduleCalculator {

    private static ScheduleCalculator instance = null;
    private Task task;
    public void setTask(Task task){
        this.task = task;
    }

    public Task getTask(){
        return task;
    }
    public static ScheduleCalculator getInstance(){
        if(instance == null){
            instance =  new ScheduleCalculator();
        }
        return instance;
    }


    public static ArrayList<Reminder> buildReminderList(Task task, Reminder reminder){
        ArrayList<Reminder> reminder_list = new ArrayList<Reminder>();
        reminder_list.add(reminder);
        //TODO USE MODEL TO CREATE REMAINING REMINDERS
        Reminder last_rem = new Reminder();
        last_rem.setTask(task);
        last_rem.setYear(task.getYear());
        last_rem.setMonth(task.getMonth());
        last_rem.setDay(task.getDay());
        last_rem.setHour(task.getHour());
        last_rem.setMinute(task.getMinute());
        reminder_list.add(last_rem);
        return reminder_list;
    }
}
