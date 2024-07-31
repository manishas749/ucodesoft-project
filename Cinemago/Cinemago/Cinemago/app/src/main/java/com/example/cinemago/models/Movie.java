package com.example.cinemago.models;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {
    private String id;
    private String name;
    private String description;
    private String duration;
    private String language;
    private String poster;
    private String time;
    private double ticketprice;
    private boolean featured;
    private List<Showtime> showtimes;  // Added list of showtimes

    public Movie() {
        // Default constructor required for Firebase
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }


    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public double getTicketprice() { return ticketprice; }
    public void setTicketprice(double ticketprice) { this.ticketprice = ticketprice; }

    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<Showtime> getShowtimes() { return showtimes; }
    public void setShowtimes(List<Showtime> showtimes) { this.showtimes = showtimes; }

}

