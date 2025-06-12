package com.example.movieapp.response;

import com.example.movieapp.models.MovieVideo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieVideoResponse {
    @SerializedName("movie")
    @Expose
    private MovieVideo video;

    public MovieVideo getVideo() {
        return video;
    }

    public void setVideo(MovieVideo video) {
        this.video = video;
    }
}