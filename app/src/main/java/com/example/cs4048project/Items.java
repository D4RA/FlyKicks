package com.example.cs4048project;

public class Items {
    private String itemId;
    private String title;
    private double price;
    private String picUrl;

    private String username;

    private String county;

    // Constructors, getters, and setters
    // Ensure to have a no-argument constructor for Firestore deserialization

    // No-argument constructor
    public Items() {
        // Required for Firestore deserialization
    }

    // Constructor with all fields
    public Items(String itemId, String title, double price, String picUrl, String username, String county) {
        this.itemId = itemId;
        this.title = title;
        this.price = price;
        this.picUrl = picUrl;
        this.username = username;
        this.county = county;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getters and setters
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}