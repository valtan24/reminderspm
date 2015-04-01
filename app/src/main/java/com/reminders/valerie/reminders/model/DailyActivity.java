package com.reminders.valerie.reminders.model;


public class DailyActivity {

    public static final double COMPLEXITY_HIGH = -1;
    public static final double COMPLEXITY_MEDIUM = 0;
    public static final double COMPLEXITY_LOW = 1;

    public static final double ASSOCIATIVITY_HIGH = 1;
    public static final double ASSOCIATIVITY_LOW = -1;

    private int start_minute;
    private int end_minute;
    private int start_hour;
    private int end_hour;
    private double complexity;
    private String category;
    private String name;
    private int day;
    private int noisy;
    private long id;

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public int isNoisy(){
        return noisy;
    }

    public void setNoisy(int noisy){
        this.noisy = noisy;
    }

    public int getDay(){
        return day;
    }

    public void setDay(int day){
        this.day = day;
    }

    public double getComplexity() {
        return complexity;
    }

    public void setComplexity(double complexity) {
        this.complexity = complexity;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStart_hour() {
        return start_hour;
    }

    public void setStart_hour(int start_hour) {
        this.start_hour = start_hour;
    }


    public int getEnd_hour() {
        return end_hour;
    }

    public void setEnd_hour(int end_hour) {
        this.end_hour = end_hour;
    }

    public int getStart_minute() {
        return start_minute;
    }

    public void setStart_minute(int start_minute) {
        this.start_minute = start_minute;
    }

    public int getEnd_minute() {
        return end_minute;
    }

    public void setEnd_minute(int end_minute) {
        this.end_minute = end_minute;
    }

    public String getTime(){
        String time_text = getStartTime();
        time_text = time_text + " - ";
        time_text = time_text + getEndTime();
        return time_text;
    }

    public String getStartTime(){
        String time_text = "";
        time_text = time_text + (start_hour < 10 ? "0" : "") + start_hour;
        time_text = time_text + ":";
        time_text = time_text + (start_minute < 10 ? "0":"") + start_minute;
        return time_text;
    }

    public String getEndTime(){
        String time_text = "";
        time_text = time_text + (end_hour < 10 ? "0" : "") + end_hour;
        time_text = time_text + ":";
        time_text = time_text + (end_minute < 10 ? "0" : "") + end_minute;
        return time_text;
    }
}
