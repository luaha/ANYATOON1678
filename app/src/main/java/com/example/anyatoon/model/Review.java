package com.example.anyatoon.model;

public class Review {
    private float rating;
    private String ratingDescription;
    private String reviewText;
    private String username;
    private int userImage;

    public Review(float rating, String ratingDescription, String reviewText, String username, int userImage) {
        this.rating = rating;
        this.ratingDescription = ratingDescription;
        this.reviewText = reviewText;
        this.username = username;
        this.userImage = userImage;
    }

    public float getRating() { return rating; }
    public String getRatingDescription() { return ratingDescription; }
    public String getReviewText() { return reviewText; }
    public String getUsername() { return username; }
    public int getUserImage() { return userImage; }
}
