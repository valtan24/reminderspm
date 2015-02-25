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
        //TODO CALCULATE AND ADD REMINDER
        //dummy data - calculate middle reminder
        int time = (task.getYear() - reminder.getYear()) * 365 + (task.getMonth() - reminder.getMonth()) * 30 + (task.getDay() - reminder.getDay());
        if(time == 0){
            time = task.getHour() - reminder.getHour();
            if(time == 0){
                time = task.getMinute() - reminder.getMinute();
                if(time == 0){
                    return reminder_list;
                }
            }
            else{
                Reminder new_rem = new Reminder();
                new_rem.setYear(task.getYear());
                new_rem.setMonth(task.getMonth());
                new_rem.setDay(task.getDay());
                new_rem.setHour(Math.abs(task.getHour() + time) % 24);
                new_rem.setTask(task);
                new_rem.setWith_audio(1);
                reminder_list.add(new_rem);
            }
        }
        else {
            int year_diff = time / 365;
            int month_diff = (time % 365) / 30;
            int day_diff = (time % 365) % 30;
            int rem_day = task.getDay() + day_diff;
            int rem_month = task.getMonth() + month_diff + (rem_day / 31);
            int rem_year = task.getYear() + year_diff;
            Reminder new_rem = new Reminder();
            new_rem.setYear(rem_year);
            new_rem.setMonth(rem_month);
            new_rem.setDay(rem_day);
            new_rem.setTask(task);
            new_rem.setHour(task.getHour());
            new_rem.setMinute(task.getMinute());
            new_rem.setWith_audio(1);
            reminder.setTask(task);
        }
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
