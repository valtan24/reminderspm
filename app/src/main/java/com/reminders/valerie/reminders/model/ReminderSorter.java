package com.reminders.valerie.reminders.model;

import android.util.Log;

import java.util.ArrayList;


public class ReminderSorter {

    public static int is_after(Reminder r1, Reminder r2){
        //1 if r1 is before r2, -1 if r1 is after r2, 0 if same
        //compare day first
        if(r1.getYear() < r2.getYear()){
            return 1;
        }
        else if (r1.getYear() > r2.getYear()){
            return -1;
        }
        else{
            //compare month
            if(r1.getMonth() < r2.getMonth()){
                return 1;
            }
            else if(r1.getMonth() > r2.getMonth()){
                return -1;
            }
            else{
                //compare day
                if(r1.getDay() < r2.getDay()){
                    return 1;
                }
                else if(r1.getDay() > r2.getDay()){
                    return -1;
                }
                else{
                    //compare hour
                    if(r1.getHour() < r2.getHour()){
                        return 1;
                    }
                    else if(r1.getHour() > r2.getHour()){
                        return -1;
                    }
                    else{
                        //compare minute
                        if(r1.getMinute() < r2.getMinute()){
                            return 1;
                        }
                        else if(r1.getMinute() > r2.getMinute()){
                            return -1;
                        }
                    }
                }
            }
        }
        return 0;
    }

    public static ArrayList<Reminder> merge_sort(ArrayList<Reminder> reminder_list, int first, int last){
        //TODO SORT
        if(first >= last){
            ArrayList<Reminder> sorted = new ArrayList<Reminder>();
            sorted.add(reminder_list.get(first));
            return sorted;
        }
        int mid = (first + last)/2;
        ArrayList<Reminder> first_half = merge_sort(reminder_list, first, mid);
        ArrayList<Reminder> second_half = merge_sort(reminder_list, mid+1, last);
        //pass by reference or value?
        ArrayList<Reminder> sorted = merge(first_half, second_half);

        //print sorted
        for(int i = 0; i < sorted.size(); i++){
            Reminder rem = sorted.get(i);
            Log.i("ReminderSorter mergesort", "" + rem.getMonth() + "-" + rem.getDay() + " " + rem.getHour() + ":" + rem.getMinute());
        }
        return sorted;
    }

    public static ArrayList<Reminder> merge(ArrayList<Reminder> first_half, ArrayList<Reminder> second_half){
        //TODO
        ArrayList<Reminder> sorted = new ArrayList<Reminder>();
        int first_ptr = 0, second_ptr = 0;
        while(first_ptr < first_half.size() && second_ptr < second_half.size()){
            if(is_after(second_half.get(second_ptr),first_half.get(first_ptr)) == 1){
                sorted.add(second_half.get(second_ptr++));
            }
            else if(is_after(first_half.get(first_ptr), second_half.get(second_ptr)) == 1){
                sorted.add(first_half.get(first_ptr++));
            }
            else{
                sorted.add(first_half.get(first_ptr++));
                sorted.add(second_half.get(second_ptr++));
            }
        }
        if(first_ptr < first_half.size()){
            for(int i = first_ptr; i < first_half.size(); i++){
                sorted.add(first_half.get(i));
            }
        }
        else if(second_ptr < second_half.size()){
            for(int j = second_ptr; j < second_half.size(); j++ ){
                sorted.add(second_half.get(j));
            }
        }
        return sorted;
    }
}
