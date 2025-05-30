package com.example.movieapp.response;

import com.example.movieapp.models.MovieVideo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieVideoResponse {
    @SerializedName("results")
    @Expose
    private List<MovieVideo> videos;

    public List<MovieVideo> getVideos() {
        return videos;
    }

    public void setVideos(List<MovieVideo> videos) {
        this.videos = videos;
    }
} 