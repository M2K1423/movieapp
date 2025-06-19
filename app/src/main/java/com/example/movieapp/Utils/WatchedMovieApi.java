package com.example.movieapp.Utils;


import com.example.movieapp.models.WatchedMovie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WatchedMovieApi {

    // Lấy danh sách phim đã xem theo userId
    @GET("/api/watch-history/{userId}")
    Call<List<WatchedMovie>> getWatchedMovies(@Path("userId") long userId);

    // Gửi 1 phim đã xem mới
    @POST("/api/watch-history/add")
    Call<WatchedMovie> addWatchedMovie(@Body WatchedMovie movie);
}
