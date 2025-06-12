package com.example.movieapp.response;

import com.example.movieapp.models.MovieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieSearchResponse {
    @SerializedName("items")
    @Expose
    private List<MovieModel> movieModelList;

    @SerializedName("status")
    private boolean status;





    public boolean isStatus() {
        return status;
    }


    public int getTotalCount() {
        return movieModelList.size();
    }

    public List<MovieModel> getMovieModelList() {
        return movieModelList;
    }

    @Override
    public String toString() {
        return "MovieSearchResponse{" +
                "movieModelList=" + movieModelList +
                '}';
    }
}