package com.reminders.valerie.reminders.model;

import android.content.Context;
import android.util.Log;

import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.myprofile.ProfileFragment;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;


public class ScheduleCalculator {

    public static final double W_DELAY_PM = -1;
    public static final double W_ASSOC_PM = 0.5;
    public static final double W_MOT_IMPT = 0.3f;
    public static final double W_AGE_PM = 0.5;
    public static final double MAX = 2.822079641;
    public static final double MIN = -5.124146431;
    public static final double ALPHA = 0.5;
    public static final double BETA = 0.5;
    public static final double GAMMA = 0.5;
    public static final double RHO = 0.001;
    public static final double R_C = 5;
    public static final double Q_C = 5;
    public static final double S_C = 5;

    private static ScheduleCalculator instance = null;
    private Task task;
    private static TaskDBHandler dbhandler;
    private static Context context;

    public void setTask(Task task){
        this.task = task;
    }

    public Task getTask(){
        return task;
    }
    public static ScheduleCalculator getInstance(Context applicationContext){
        if(instance == null){
            instance =  new ScheduleCalculator();
            context = applicationContext;
            dbhandler = new TaskDBHandler(context);
        }
        return instance;
    }


    public ArrayList<Reminder> buildReminderList(Task task, Reminder reminder){
        ArrayList<Reminder> reminder_list = new ArrayList<Reminder>();
        reminder_list.add(reminder);
        if(task.getYear() == reminder.getYear() &&
                task.getMonth() == reminder.getMonth() &&
                task.getDay() ==  reminder.getDay() &&
                task.getHour() == reminder.getHour() &&
                task.getMinute() == reminder.getMinute()) return reminder_list;
        //get pm rating
        double pm_rating = calculatePM(task, reminder);
        //get number of reminders
        if(pm_rating >= 0) {
            Log.i("ScheduleCalculator.buildReminderList", ""+pm_rating);
            Calendar task_cal = Calendar.getInstance();
            task_cal.set(task.getYear(), task.getMonth(), task.getDay(), task.getHour(), task.getMinute(), 0);
            Calendar reminder_cal = Calendar.getInstance();
            reminder_cal.set(reminder.getYear(), reminder.getMonth(), reminder.getDay(), reminder.getHour(), reminder.getMinute(),0);
            double t_w = (task_cal.getTimeInMillis() - reminder_cal.getTimeInMillis());
            int num_reminders = getNumReminders(pm_rating);
            double a = coefficientValue(num_reminders);
            long t_0 = reminder_cal.getTimeInMillis();
            for (int i = 1; i <= num_reminders; i++) {
                long t = (long) (t_w * (1 - Math.exp(-a * i)));
                t = t + t_0;
                Calendar next_cal = Calendar.getInstance();
                next_cal.setTimeInMillis(t);
                Reminder next_reminder = new Reminder();
                next_reminder.setTask_id(task.getTask_id());
                next_reminder.setYear(next_cal.get(Calendar.YEAR));
                next_reminder.setMonth(next_cal.get(Calendar.MONTH));
                next_reminder.setDay(next_cal.get(Calendar.DAY_OF_MONTH));
                next_reminder.setHour(next_cal.get(Calendar.HOUR_OF_DAY));
                next_reminder.setMinute(next_cal.get(Calendar.MINUTE));
                next_reminder.setTask(task);
                next_reminder.setIs_fired(0);
                DailyActivity ongoing = getOngoingActivity(next_cal);
                if(ongoing == null){
                    next_reminder.setWith_audio(1);
                }
                else{
                    if(ongoing.isNoisy() == 1){
                        next_reminder.setWith_audio(1);
                    }
                    else{
                        next_reminder.setWith_audio(0);
                    }
                }
                reminder_list.add(next_reminder);
            }
            //build last reminder on task
            Reminder last_reminder = new Reminder();
            last_reminder.setTask(task);
            last_reminder.setTask_id(task.getTask_id());
            last_reminder.setHour(task.getHour());
            last_reminder.setMinute(task.getMinute());
            last_reminder.setDay(task.getDay());
            last_reminder.setYear(task.getYear());
            last_reminder.setMonth(task.getMonth());
            Calendar cal = Calendar.getInstance();
            cal.set(task.getYear(), task.getMonth(), task.getDay(), task.getHour(), task.getMinute(), 0);
            DailyActivity last_ongoing = getOngoingActivity(cal);
            if(last_ongoing == null){
                last_reminder.setWith_audio(1);
            }
            else{
                if(last_ongoing.isNoisy() == 1){
                    last_reminder.setWith_audio(1);
                }
                else{
                    last_reminder.setWith_audio(0);
                }
            }
            reminder_list.add(last_reminder);
        }
        return reminder_list;
    }

    private double calculatePM(Task task, Reminder first_reminder){
        if(dbhandler == null){
            Log.e("ScheduleCalculator", "TaskDBHandler is null");
            return -1; // pm rating range is [0,1]
        }
        //concept values
        double c_delay, c_impt, c_motivation, c_age, c_complexity, c_assoc;
        c_delay = getDelayConceptValue(task, first_reminder);
        Log.i("ScheduleCalculator.calculatePM", "Delay concept value = "+c_delay);
        c_impt = getImportanceConceptValue(task);
        Log.i("ScheduleCalculator.calculatePM", "Importance concept value = " + c_impt);
        c_motivation = getMotivationConceptValue(task);
        Log.i("ScheduleCalculator.calculatePM", "Motivation concept value = " + c_motivation);
        c_age = -2 * f_age_input(R_C);
        Log.i("ScheduleCalculator.calculatePM", "Age concept value = " + c_age);
        c_complexity = getComplexityConceptValue(task);
        Log.i("ScheduleCalculator.calculatePM", "Complexity concept value = " + c_complexity);
        c_assoc = getAssociativityConceptValue(task);
        Log.i("ScheduleCalculator.calculatePM", "Assoc concept value = " + c_assoc);

        //weights
        double w_age_pm, w_mot_pm, w_complexity_pm, w_impt_pm;
        w_complexity_pm = -(1-BETA)*(f_age_input(S_C) + 0.5) - BETA;
        w_mot_pm = (1-ALPHA)*f_age_input(Q_C) + 0.5 + ALPHA;
        w_impt_pm = ((1-GAMMA) / 2) *  (c_assoc + 1) + GAMMA;

        //new value for importance
        c_impt = c_impt + W_MOT_IMPT*c_motivation;

        //x
        double x = c_delay * W_DELAY_PM;
        x += c_age * W_AGE_PM;
        x += c_assoc * W_ASSOC_PM;
        x += c_motivation * w_mot_pm;
        x += c_impt * w_impt_pm;
        x += c_complexity * w_complexity_pm;

        //compression
        double c_pm = (x - MIN)/(MAX - MIN);

        return c_pm;
    }

    private double getDelayConceptValue(Task task, Reminder reminder){
        Calendar task_cal = Calendar.getInstance();
        task_cal.set(task.getYear(), task.getMonth(), task.getDay(), task.getHour(), task.getMinute(), 0);
        Calendar reminder_cal = Calendar.getInstance();
        long time_diff_millis = task_cal.getTimeInMillis() - reminder_cal.getTimeInMillis();
        long time_diff_min = time_diff_millis / (60 * 1000);
        Log.i("ScheduleCalculator.getDelayConceptValue", ""+time_diff_min);
        double concept = -(1.0 / Math.pow((RHO * time_diff_min + 1),2)) + 1;
        return concept;
    }

    private double getAssociativityConceptValue(Task task){
        Calendar cal = Calendar.getInstance();
        cal.set(task.getYear(), task.getMonth(), task.getDay(), task.getHour(), task.getMinute(), 0);
        DailyActivity ongoing = getOngoingActivity(cal);
        if(ongoing != null){
            if(ongoing.getCategory().equals(task.getCategory())){
                return DailyActivity.ASSOCIATIVITY_HIGH;
            }
            else{
                return DailyActivity.ASSOCIATIVITY_LOW;
            }
        }
        //default value to return if task does not collide with any activities
        return DailyActivity.ASSOCIATIVITY_LOW;
    }

    private double getMotivationConceptValue(Task task){
        String category = task.getCategory();
        return dbhandler.getMotivationFromCategory(category);
    }

    private double getImportanceConceptValue(Task task){
        if(task.getImportance() == Task.IMPORTANCE_HIGH){
            return 0.7;
        }
        else if(task.getImportance() == Task.IMPORTANCE_MEDIUM){
            return 0;
        }
        else{
            return -0.7;
        }
    }

    private double f_age_input(double coefficient){
        try{
            FileInputStream internal_input = context.openFileInput(ProfileFragment.INTERNAL_FILENAME);
            BufferedReader input_br = new BufferedReader(new InputStreamReader(new DataInputStream(internal_input)));
            String name_in = input_br.readLine();
            int year = Integer.parseInt(input_br.readLine());
            int month = Integer.parseInt(input_br.readLine());
            int day = Integer.parseInt(input_br.readLine());
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            Calendar now_cal = Calendar.getInstance();
            long diff_millis = now_cal.getTimeInMillis() - cal.getTimeInMillis();
            long denom  = (long) 60*1000*60*24*365;
            double diff_years = diff_millis / denom;
            //compression
            double compressed_age = diff_years / 100;
            double tan_arg = coefficient * (compressed_age - 0.5);
            double value = Math.atan(tan_arg);
            value /= Math.PI;
            return value;
        }
        catch(FileNotFoundException e){
            Log.i("ScheduleCalculator.f_age_input", "internal file don't exist");
        }
        catch(IOException e){
            Log.e("ScheduleCalculator.f_age_input", "unable to read from file");
        }
        return 1.0f;
    }

    private double getComplexityConceptValue(Task task){
        Calendar cal = Calendar.getInstance();
        cal.set(task.getYear(), task.getMonth(), task.getDay(), task.getHour(), task.getMinute(), 0);
        DailyActivity ongoing = getOngoingActivity(cal);
        if(ongoing != null){
            return ongoing.getComplexity();
        }
        return DailyActivity.COMPLEXITY_MEDIUM;
    }

    private  DailyActivity getOngoingActivity(Calendar cal){
        ArrayList<DailyActivity> activities = new ArrayList<DailyActivity>();
        switch(cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                activities = dbhandler.getActivitiesByDay(1);
                break;
            case Calendar.TUESDAY:
                activities = dbhandler.getActivitiesByDay(2);
                break;
            case Calendar.WEDNESDAY:
                activities = dbhandler.getActivitiesByDay(3);
                break;
            case Calendar.THURSDAY:
                activities = dbhandler.getActivitiesByDay(4);
                break;
            case Calendar.FRIDAY:
                activities = dbhandler.getActivitiesByDay(5);
                break;
            case Calendar.SATURDAY:
                activities = dbhandler.getActivitiesByDay(6);
                break;
            case Calendar.SUNDAY:
                activities = dbhandler.getActivitiesByDay(7);
                break;
        }
        Calendar start = Calendar.getInstance();
        start.set(task.getYear(), task.getMonth(), task.getDay());
        Calendar end = Calendar.getInstance();
        end.set(task.getYear(), task.getMonth(), task.getDay());
        for (int i  = 0; i < activities.size(); i++) {
            DailyActivity activity = activities.get(i);
            start.set(Calendar.HOUR_OF_DAY, activity.getStart_hour());
            start.set(Calendar.MINUTE, activity.getStart_minute());
            start.set(Calendar.SECOND, 0);
            end.set(Calendar.SECOND, 0);
            end.set(Calendar.HOUR_OF_DAY, activity.getEnd_hour());
            end.set(Calendar.MINUTE, activity.getEnd_minute());
            if ((cal.compareTo(start) == 1 || cal.compareTo(start) == 0) && cal.compareTo(end) == -1) {
                return activity;
            }
        }
        return null;
    }

    private int getNumReminders(double pm_concept){
        if(pm_concept < 0.2){
            return 5;
        }
        else if(pm_concept >= 0.2 && pm_concept < 0.4){
            return 4;
        }
        else if(pm_concept >= 0.4 && pm_concept < 0.6){
            return 3;
        }
        else if(pm_concept >= 0.6 && pm_concept < 0.8){
            return 2;
        }
        else{
            return 1;
        }
    }

    private double coefficientValue(int num){
        switch(num){
            case 1:
                return 2;
            case 2:
                return 1.2;
            case 3:
                return 0.8;
            case 4:
                return 0.6;
            case 5:
                return 0.5;
            default:
                return 1;
        }
    }

    public void killInstance(){
        dbhandler.close();
        context = null;
        instance = null;
    }
}
