package com.example.movieapp.Utils;

import com.example.movieapp.models.MovieModel;
import com.example.movieapp.response.MovieListResponse;
import com.example.movieapp.response.MovieSearchResponse;
import com.example.movieapp.response.MovieVideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("/v1/api/tim-kiem")
    Call<MovieSearchResponse> searchMovies(
            @Query("keyword") String query,
            @Query("limit") String page
    );

    @GET("/danh-sach/phim-moi-cap-nhat")
    Call<MovieSearchResponse> getPopularMovies(
            @Query("page") String page
    );

    @GET("/v1/api/danh-sach/phim-le")
    Call<MovieListResponse> getUpcomingMovies(

            @Query("page") String page
    );

    @GET("/phim/{slug}")
    Call<MovieVideoResponse> getMovie(@Path("slug") String slug);

    @GET("/v1/api/danh-sach/hoat-hinh")
    Call<MovieListResponse> getTrendingMovies(
            @Query("page") String page
    );

    @GET("/v1/api/danh-sach/tv-shows")
    Call<MovieListResponse> getTopRatedMovies(
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