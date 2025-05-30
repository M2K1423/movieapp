package com.example.movieapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.models.MovieModel;
import com.example.movieapp.repository.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    private MovieRepository repository;

    public MovieListViewModel() {
        repository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return repository.getMovies();
    }

    public LiveData<List<MovieModel>> getPopularMoviesList() {
        return repository.getPopularMovies();
    }

    public LiveData<List<MovieModel>> getUpcomingMovies() {
        return repository.getUpcomingMovies();
    }

    public LiveData<List<MovieModel>> getTrendingMovies() {
        return repository.getTrendingMovies();
    }

    public LiveData<List<MovieModel>> getTopRatedMovies() {
        return repository.getTopRatedMovies();
    }

    public LiveData<List<MovieModel>> getFavouriteMovies() {
        return repository.getFavouriteMovies();
    }

    public void searchMovieApi(String query, int pageNumber) {
        repository.searchMovieApi(query, pageNumber);
    }

    public void searchPopularMovieApi(int pageNumber) {
        repository.searchPopularMovieApi(pageNumber);
    }

    public void searchUpcomingMovies(int pageNumber) {
        repository.searchUpcomingMovies(pageNumber);
    }

    public void searchTrendingMovies(int pageNumber) {
        repository.searchTrendingMovies(pageNumber);
    }

    public void searchTopRatedMovies(int pageNumber) {
        repository.searchTopRatedMovies(pageNumber);
    }

    public void searchNextPage() {
        repository.searchNextPage();
    }


}
