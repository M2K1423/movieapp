package com.example.movieapp.request;

import com.example.movieapp.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {

    // Search for movies
    @GET("/3/search/movie")
    Call<MovieSearchResponse> searchMovies(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") String page
    );

    // Get Popular Movies
    @GET("/3/movie/popular")
    Call<MovieSearchResponse> getPopularMovies(
            @Query("api_key") String key,
            @Query("page") String page
    );

    // Get Upcoming Movies
    @GET("/3/movie/upcoming")
    Call<MovieSearchResponse> getUpcomingMovies(
            @Query("api_key") String key,
            @Query("page") String page
    );

    // Get Top Rated Movies
    @GET("/3/movie/top_rated")
    Call<MovieSearchResponse> getTopRatedMovies(
            @Query("api_key") String key,
            @Query("page") String page
    );

    // Get Trending Movies
    @GET("/3/trending/movie/day")
    Call<MovieSearchResponse> getTrendingMovies(
            @Query("api_key") String key,
            @Query("page") String page
    );
} 