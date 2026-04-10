package com.example.bai5_firebase.model;

public class Showtime {
    private String id;
    private String movieId;
    private String theaterId;
    private long startTimeMillis;
    private double price;

    public Showtime() {
    }

    public Showtime(String id, String movieId, String theaterId, long startTimeMillis, double price) {
        this.id = id;
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.startTimeMillis = startTimeMillis;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTheaterId() {
        return theaterId;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public double getPrice() {
        return price;
    }
}
