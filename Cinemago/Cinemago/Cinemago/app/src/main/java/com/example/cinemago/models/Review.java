package com.example.cinemago.models;

public class Review {
    private String userId;
    private String comment;
    private int rating;

    public Review() {
        // Default constructor required for calls to DataSnapshot.getValue(Review.class)
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
