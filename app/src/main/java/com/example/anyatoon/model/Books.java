package com.example.anyatoon.model;

import java.io.Serializable;

public class Books implements Serializable {
    private String title;
    private String author;
    private String genre;
    private String status;
    private int likes;
    private int popularity;
    private String coverImageUrl;

    public Books(String title, String author, String genre, String status, int likes, int popularity, String coverImageUrl) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.status = status;
        this.likes = likes;
        this.popularity = popularity;
        this.coverImageUrl = coverImageUrl;
    }

    // Getter và Setter cho các thuộc tính
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

}
