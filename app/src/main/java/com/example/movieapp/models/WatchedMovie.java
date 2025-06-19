package com.example.movieapp.models;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class WatchedMovie {
    private long id;
    private String title;
    private String thumbnailUrl;

    private String watchedAt;
    private String content;

    private User user;

    public WatchedMovie() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getWatchedAt() {
        return watchedAt;
    }

    public void setWatchedAt(String watchedAt) {
        this.watchedAt = watchedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "WatchedMovie{" +
                "title='" + title + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", watchedAt='" + watchedAt + '\'' +
                ", content='" + content + '\'' +
                ", user=" + user +
                '}';
    }
}
