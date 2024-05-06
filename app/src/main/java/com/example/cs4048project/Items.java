package com.example.cs4048project;

public class Items {
    private String title;
    private int  picUrl;
    private String price;

    public Items(String title, int picUrl, String price) {
        this.title = title;
        this.picUrl = picUrl;
        this.price = price;
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
}
