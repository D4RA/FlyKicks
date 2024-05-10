package com.example.cs4048project;

public class UserList {
    private String username;
    private String pictureUrl;
    private String uid;

    public UserList(String username, String pictureUrl, String uid) {
        this.username = username;
        this.pictureUrl = pictureUrl;
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getUid() {
        return uid;
    }
}
