package com.example.movieapp.models;

import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("id")
    private String id;
    
    @SerializedName("author")
    private String userName;
    
    @SerializedName("content")
    private String content;
    
    @SerializedName("created_at")
    private String date;
    
    @SerializedName("rating")
    private float rating;
    
    private int likes;
    private boolean isLiked;

    public Review(String id, String userName, String content, String date, float rating) {
        this.id = id;
        this.userName = userName;
        this.content = content;
        this.date = date;
        this.rating = rating;
        this.likes = 0;
        this.isLiked = false;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public float getRating() {
        return rating;
    }

    public int getLikes() {
        return likes;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void toggleLike() {
        isLiked = !isLiked;
        likes += isLiked ? 1 : -1;
    }
} 