package com.example.movieapp.Utils;

import com.example.movieapp.models.MovieModel;
import com.example.movieapp.response.MovieSearchResponse;
import com.example.movieapp.response.MovieVideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("/3/search/movie")
    Call<MovieSearchResponse> searchMovies(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") String page
    );

    @GET("/3/movie/popular")
    Call<MovieSearchResponse> getPopularMovies(
            @Query("api_key") String key,
            @Query("page") String page
    );

    @GET("/3/movie/upcoming")
    Call<MovieSearchResponse> getUpcomingMovies(
            @Query("api_key") String key,
            @Query("page") String page
    );

    @GET("/3/movie/{movie_id}")
    Call<MovieModel> getMovie(
            @Path("movie_id") int movieId,
            @Query("api_key") String key
    );

    @GET("/3/trending/movie/day")
    Call<MovieSearchResponse> getTrendingMovies(
            @Query("api_key") String key,
            @Query("page") String page
    );

    @GET("/3/movie/top_rated")
    Call<MovieSearchResponse> getTopRatedMovies(
            @Query("api_key") String key,
            @Query("page") String page
    );

    @GET("/3/movie/{movie_id}/videos")
    Call<MovieVideoResponse> getMovieVideos(
            @Path("movie_id") int movieId,
            @Query("api_key") String key,
            @Query("language") String language
    );
}
