package com.example.movieapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.models.MovieModel;
import com.example.movieapp.request.MovieApiClient;

import java.util.List;

public class MovieRepository {

    private static MovieRepository instance;
    private MovieApiClient movieApiClient;

    private String mQuery;
    private int mPageNumber;

    public MovieRepository() {
        movieApiClient = new MovieApiClient();
    }

    public static synchronized MovieRepository getInstance(){
        if(instance == null){
            instance = new MovieRepository();
        }
        return instance;
    }



    public LiveData<List<MovieModel>> getMovies(){
        return movieApiClient.getMovies();
    }

    public LiveData<List<MovieModel>> getPopularMovies(){
        return movieApiClient.getMoviesPopular();
    }

    public LiveData<List<MovieModel>> getUpcomingMovies() {
        return movieApiClient.getUpcomingMovies();
    }

    public LiveData<List<MovieModel>> getTrendingMovies() {
        return movieApiClient.getTrendingMovies();
    }

    public LiveData<List<MovieModel>> getTopRatedMovies() {
        return movieApiClient.getTopRatedMovies();
    }

    public LiveData<List<MovieModel>> getFavouriteMovies() {
        return movieApiClient.getFavouriteMovies();
    }

    public void searchMovieApi(String query, int pageNumber){
        mQuery = query;
        mPageNumber = pageNumber;
        movieApiClient.searchMovieApi(query, pageNumber);
    }

    public void searchPopularMovieApi(int pageNumber){
        mPageNumber = pageNumber;
        movieApiClient.searchMoviePopular(pageNumber);
    }

    public void searchUpcomingMovies(int pageNumber) {
        mPageNumber = pageNumber;
        movieApiClient.searchUpcomingMovies(pageNumber);
    }

    public void searchTrendingMovies(int pageNumber) {
        mPageNumber = pageNumber;
        movieApiClient.searchTrendingMovies(pageNumber);
    }

    public void searchTopRatedMovies(int pageNumber) {
        mPageNumber = pageNumber;
        movieApiClient.searchTopRatedMovies(pageNumber);
    }

    public void searchNextPage() {
        searchMovieApi(mQuery,mPageNumber+1);
    }
}
