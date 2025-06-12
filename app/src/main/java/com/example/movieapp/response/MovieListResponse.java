package com.example.movieapp.response;

import com.example.movieapp.models.MovieModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieListResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private MovieResponse data;

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public List<MovieModel> getMovieModelList() {
        return data != null ? data.getItems() : null;
    }
}