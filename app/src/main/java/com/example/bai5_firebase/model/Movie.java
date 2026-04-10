package com.example.bai5_firebase.model;

public class Movie {
    private String id;
    private String title;
    private String genre;
    private long durationMin;
    private String description;
    private String imageUrl;

    public Movie() {
    }

    public Movie(String id, String title, String genre, long durationMin, String description, String imageUrl) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.durationMin = durationMin;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public long getDurationMin() {
        return durationMin;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
