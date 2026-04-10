package com.example.bai5_firebase.model;

import java.util.List;

public class Ticket {
    private String id;
    private String userId;
    private String showtimeId;
    private String movieTitle;
    private String theaterName;
    private long showtimeMillis;
    private long quantity;
    private List<String> selectedSeats;
    private double totalPrice;
    private String status;
    private long createdAt;

    public Ticket() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public String getShowtimeId() {
        return showtimeId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getTheaterName() {
        return theaterName;
    }

    public long getShowtimeMillis() {
        return showtimeMillis;
    }

    public long getQuantity() {
        return quantity;
    }

    public List<String> getSelectedSeats() {
        return selectedSeats;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
