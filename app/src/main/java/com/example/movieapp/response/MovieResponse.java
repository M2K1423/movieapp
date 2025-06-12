package com.example.movieapp.response;

import com.example.movieapp.models.MovieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// This response is for Single Movie
public class MovieResponse {

    @SerializedName("items")
    private List<MovieModel> items;

    public List<MovieModel> getItems() {
        return items;
    }
}