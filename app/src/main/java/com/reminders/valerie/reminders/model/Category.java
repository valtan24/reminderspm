package com.reminders.valerie.reminders.model;

/**
 * Created by valerie on 2/11/2015.
 */
public class Category{

    private String audio_uri;
    private String category_title;

    public double getMotivation() {
        return 0.5;
    }

    public void setMotivation(double motivation) {
        this.motivation = motivation;
    }

    private double motivation;

    public Category(String title, String audio){
        this.category_title = title;
        this.audio_uri = audio;
    }
    public void setAudio_uri(String uri_text){
        this.audio_uri = uri_text;
    }

    public void setCategory_title(String title){
        this.category_title = title;
    }

    public String getAudio_uri(){
        return audio_uri;
    }

    public String getCategory_title(){
        return category_title;
    }

}
