package com.example.cs4048project;

import android.opengl.Matrix;

import com.google.android.gms.maps.model.LatLng;

public class Items {
    private String title;
    private int  picUrl;
    private String price;
    private LatLng location;

    public Items(String title, int picUrl, String price,LatLng location) {
        this.title = title;
        this.picUrl = picUrl;
        this.price = price;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(int picUrl) {
        this.picUrl = picUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
