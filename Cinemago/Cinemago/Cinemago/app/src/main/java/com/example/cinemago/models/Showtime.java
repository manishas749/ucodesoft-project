package com.example.cinemago.models;

import java.io.Serializable;

public class Showtime implements Serializable {
    private String day;
    private String time;

    public Showtime() {
        // Default constructor required for Firebase
    }

    public Showtime(String day, String time) {
        this.day = day;
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
