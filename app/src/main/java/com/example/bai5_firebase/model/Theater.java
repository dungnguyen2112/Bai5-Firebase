package com.example.bai5_firebase.model;

public class Theater {
    private String id;
    private String name;
    private String address;

    public Theater() {
    }

    public Theater(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
