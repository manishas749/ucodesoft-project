package com.example.cinemago.models;

import java.util.List;

public class Booking {
    private String cinemaId;
    private String date;
    private String movieId;
    private List<Integer> seats;  // Assuming seats are stored as a list of integers
    private String status;
    private String time;
    private int totalPrice;
    private String userId;

    // Constructor without parameters
    public Booking() {
    }

    // Constructor with all parameters
    public Booking(String cinemaId, String date, String movieId, List<Integer> seats, String status, String time, int totalPrice, String userId) {
        this.cinemaId = cinemaId;
        this.date = date;
        this.movieId = movieId;
        this.seats = seats;
        this.status = status;
        this.time = time;
        this.totalPrice = totalPrice;
        this.userId = userId;
    }

    // Getter and setter methods
    public String getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public List<Integer> getSeats() {
        return seats;
    }

    public void setSeats(List<Integer> seats) {
        this.seats = seats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
