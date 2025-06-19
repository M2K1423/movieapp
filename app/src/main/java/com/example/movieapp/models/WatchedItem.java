package com.example.movieapp.models;

public class WatchedItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_MOVIE = 1;

    private int type;
    private String headerTitle;
    private WatchedMovie movie;

    public WatchedItem(int type, String headerTitle, WatchedMovie movie) {
        this.type = type;
        this.headerTitle = headerTitle;
        this.movie = movie;
    }

    public int getType() {
        return type;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public WatchedMovie getMovie() {
        return movie;
    }

    @Override
    public String toString() {
        return "WatchedItem{" +
                "type=" + type +
                ", headerTitle='" + headerTitle + '\'' +
                ", movie=" + movie +
                '}';
    }
}

