package com.reminders.valerie.reminders.notificationservice;

public class IdGenerator {

    public static int generateID(long task, long reminder){
        String id_str = ""+ ((int) task) + ((int) reminder);
        return Integer.parseInt(id_str);
    }
}
