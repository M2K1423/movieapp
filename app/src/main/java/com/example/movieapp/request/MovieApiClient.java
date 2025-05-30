package com.example.movieapp.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.AppExecutors;
import com.example.movieapp.Utils.Credentials;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.response.MovieSearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

// SingleTon Class
public class MovieApiClient {

    // LiveData for Searched Movies
    private MutableLiveData<List<MovieModel>> mMovies;
    // Making Global Search Runnable
    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    // LiveData for Popular Movies
    private MutableLiveData<List<MovieModel>> mMoviesPopular;
    // Making Global Runnable
    private RetrieveMoviesRunnablePopular retrieveMoviesRunnablePopular;

    // LiveData for Upcoming Movies
    private MutableLiveData<List<MovieModel>> mMoviesUpcoming;
    // Making Global Runnable
    private RetrieveMoviesRunnableUpcoming retrieveMoviesRunnableUpcoming;

    // LiveData for Top Rated Movies
    private MutableLiveData<List<MovieModel>> mMoviesTopRated;
    // Making Global Runnable
    private RetrieveMoviesRunnableTopRated retrieveMoviesRunnableTopRated;

    // LiveData for Trending Movies
    private MutableLiveData<List<MovieModel>> mMoviesTrending;
    // Making Global Runnable
    private RetrieveMoviesRunnableTrending retrieveMoviesRunnableTrending;

    // LiveData for Favourite Movies
    private MutableLiveData<List<MovieModel>> mMoviesFavourite;

    private static MovieApiClient instance;

    public static synchronized MovieApiClient getInstance(){
        if(instance==null){
            instance = new MovieApiClient();
        }
        return instance;
    }

    // Constructors
    public MovieApiClient() {
        mMovies = new MutableLiveData<>();
        mMoviesPopular = new MutableLiveData<>();
        mMoviesUpcoming = new MutableLiveData<>();
        mMoviesTopRated = new MutableLiveData<>();
        mMoviesTrending = new MutableLiveData<>();
        mMoviesFavourite = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return mMovies;
    }
    public LiveData<List<MovieModel>> getMoviesPopular(){
        return mMoviesPopular;
    }
    public LiveData<List<MovieModel>> getUpcomingMovies() {
        return mMoviesUpcoming;
    }

    public LiveData<List<MovieModel>> getTopRatedMovies() {
        return mMoviesTopRated;
    }

    public LiveData<List<MovieModel>> getTrendingMovies() {
        return mMoviesTrending;
    }

    public LiveData<List<MovieModel>> getFavouriteMovies() {
        return mMoviesFavourite;
    }

    public void searchMovieApi(String query, int pageNumber){
        if (retrieveMoviesRunnable != null) {
            retrieveMoviesRunnable = null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);

        final Future<?> future = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(() -> {
            future.cancel(true);
        }, 3000, TimeUnit.MILLISECONDS);
    }

    public void searchMoviePopular(int pageNumber){
        if (retrieveMoviesRunnablePopular != null) {
            retrieveMoviesRunnablePopular = null;
        }

        retrieveMoviesRunnablePopular = new RetrieveMoviesRunnablePopular(pageNumber);

        final Future<?> future = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnablePopular);

        AppExecutors.getInstance().networkIO().schedule(() -> {
            future.cancel(true);
        }, 3000, TimeUnit.MILLISECONDS);
    }

    public void searchUpcomingMovies(int pageNumber) {
        if (retrieveMoviesRunnableUpcoming != null) {
            retrieveMoviesRunnableUpcoming = null;
        }

        retrieveMoviesRunnableUpcoming = new RetrieveMoviesRunnableUpcoming(pageNumber);

        final Future<?> future = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnableUpcoming);

        AppExecutors.getInstance().networkIO().schedule(() -> {
            future.cancel(true);
        }, 3000, TimeUnit.MILLISECONDS);
    }

    public void searchTopRatedMovies(int pageNumber) {
        if (retrieveMoviesRunnableTopRated != null) {
            retrieveMoviesRunnableTopRated = null;
        }

        retrieveMoviesRunnableTopRated = new RetrieveMoviesRunnableTopRated(pageNumber);

        final Future<?> future = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnableTopRated);

        AppExecutors.getInstance().networkIO().schedule(() -> {
            future.cancel(true);
        }, 3000, TimeUnit.MILLISECONDS);
    }

    public void searchTrendingMovies(int pageNumber) {
        if (retrieveMoviesRunnableTrending != null) {
            retrieveMoviesRunnableTrending = null;
        }

        retrieveMoviesRunnableTrending = new RetrieveMoviesRunnableTrending(pageNumber);

        final Future<?> future = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnableTrending);

        AppExecutors.getInstance().networkIO().schedule(() -> {
            future.cancel(true);
        }, 3000, TimeUnit.MILLISECONDS);
    }

    // Retrieving Data from RESTAPI by runnable class
    private class RetrieveMoviesRunnable implements Runnable{

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber){
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response<MovieSearchResponse> response = getMovies(query, pageNumber).execute();
                if (cancelRequest) return;

                if (response.code() == 200 && response.body() != null) {
                    List<MovieModel> list = new ArrayList<>(response.body().getMovieModelList());
                    if (pageNumber == 1) {
                        mMovies.postValue(list);
                    } else {
                        List<MovieModel> current = mMovies.getValue();
                        if (current != null) {
                            current.addAll(list);
                            mMovies.postValue(current);
                        }
                    }
                } else {
                    String error = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                    Log.v("Tag", "Error: " + error);
                    mMovies.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }
        }

        private Call<MovieSearchResponse> getMovies(String query, int pageNumber){
            return Servicey.getMovieApi().searchMovies(
                    Credentials.API_KEY,
                    query,
                    String.valueOf(pageNumber)
            );
        }

        private void cancelRequest(){
            Log.v("Tag", "Cancelling Search Request");
            cancelRequest = true;
        }
    }

    private class RetrieveMoviesRunnablePopular implements Runnable{
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnablePopular(int pageNumber){
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Log.d("PopularMovies", "Making API request for popular movies");
                Response<MovieSearchResponse> response = getPopularMovies(pageNumber).execute();
                if (cancelRequest) {
                    Log.d("PopularMovies", "Request was cancelled");
                    return;
                }

                if (response.code() == 200 && response.body() != null) {
                    List<MovieModel> list = new ArrayList<>(response.body().getMovieModelList());
                    Log.d("PopularMovies", "Received " + list.size() + " popular movies");
                    if (pageNumber == 1) {
                        mMoviesPopular.postValue(list);
                    } else {
                        List<MovieModel> current = mMoviesPopular.getValue();
                        if (current != null) {
                            current.addAll(list);
                            mMoviesPopular.postValue(current);
                        }
                    }
                } else {
                    String error = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                    Log.e("PopularMovies", "Error response: " + response.code() + " - " + error);
                    mMoviesPopular.postValue(null);
                }
            } catch (IOException e) {
                Log.e("PopularMovies", "Exception during API call", e);
                e.printStackTrace();
                mMoviesPopular.postValue(null);
            }
        }

        private Call<MovieSearchResponse> getPopularMovies(int pageNumber){
            return Servicey.getMovieApi().getPopularMovies(
                    Credentials.API_KEY,
                    String.valueOf(pageNumber)
            );
        }

        private void cancelRequest(){
            Log.v("Tag", "Cancelling Popular Request");
            cancelRequest = true;
        }
    }

    private class RetrieveMoviesRunnableUpcoming implements Runnable {
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnableUpcoming(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response<MovieSearchResponse> response = getUpcomingMovies(pageNumber).execute();
                if (cancelRequest) return;

                if (response.code() == 200 && response.body() != null) {
                    List<MovieModel> list = new ArrayList<>(response.body().getMovieModelList());
                    if (pageNumber == 1) {
                        mMoviesUpcoming.postValue(list);
                    } else {
                        List<MovieModel> current = mMoviesUpcoming.getValue();
                        if (current != null) {
                            current.addAll(list);
                            mMoviesUpcoming.postValue(current);
                        }
                    }
                } else {
                    String error = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                    Log.v("Tag", "Error: " + error);
                    mMoviesUpcoming.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMoviesUpcoming.postValue(null);
            }
        }

        private Call<MovieSearchResponse> getUpcomingMovies(int pageNumber) {
            return Servicey.getMovieApi().getUpcomingMovies(
                    Credentials.API_KEY,
                    String.valueOf(pageNumber)
            );
        }

        private void cancelRequest() {
            Log.v("Tag", "Cancelling Upcoming Request");
            cancelRequest = true;
        }
    }

    private class RetrieveMoviesRunnableTopRated implements Runnable {
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnableTopRated(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response<MovieSearchResponse> response = getTopRatedMovies(pageNumber).execute();
                if (cancelRequest) return;

                if (response.code() == 200 && response.body() != null) {
                    List<MovieModel> list = new ArrayList<>(response.body().getMovieModelList());
                    if (pageNumber == 1) {
                        mMoviesTopRated.postValue(list);
                    } else {
                        List<MovieModel> current = mMoviesTopRated.getValue();
                        if (current != null) {
                            current.addAll(list);
                            mMoviesTopRated.postValue(current);
                        }
                    }
                } else {
                    String error = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                    Log.v("Tag", "Error: " + error);
                    mMoviesTopRated.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMoviesTopRated.postValue(null);
            }
        }

        private Call<MovieSearchResponse> getTopRatedMovies(int pageNumber) {
            return Servicey.getMovieApi().getTopRatedMovies(
                    Credentials.API_KEY,
                    String.valueOf(pageNumber)
            );
        }

        private void cancelRequest() {
            Log.v("Tag", "Cancelling Top Rated Request");
            cancelRequest = true;
        }
    }

    private class RetrieveMoviesRunnableTrending implements Runnable {
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnableTrending(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response<MovieSearchResponse> response = getTrendingMovies(pageNumber).execute();
                if (cancelRequest) return;

                if (response.code() == 200 && response.body() != null) {
                    List<MovieModel> list = new ArrayList<>(response.body().getMovieModelList());
                    if (pageNumber == 1) {
                        mMoviesTrending.postValue(list);
                    } else {
                        List<MovieModel> current = mMoviesTrending.getValue();
                        if (current != null) {
                            current.addAll(list);
                            mMoviesTrending.postValue(current);
                        }
                    }
                } else {
                    String error = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                    Log.v("Tag", "Error: " + error);
                    mMoviesTrending.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMoviesTrending.postValue(null);
            }
        }

        private Call<MovieSearchResponse> getTrendingMovies(int pageNumber) {
            return Servicey.getMovieApi().getTrendingMovies(
                    Credentials.API_KEY,
                    String.valueOf(pageNumber)
            );
        }

        private void cancelRequest() {
            Log.v("Tag", "Cancelling Trending Request");
            cancelRequest = true;
        }
    }
}
