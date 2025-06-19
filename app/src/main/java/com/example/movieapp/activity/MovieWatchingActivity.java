package com.example.movieapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.Utils.MovieApi;
import com.example.movieapp.adapters.WatchedMovieAdapter;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.models.MovieVideo;
import com.example.movieapp.models.WatchedItem;
import com.example.movieapp.models.WatchedMovie;
import com.example.movieapp.request.Servicey;
import com.example.movieapp.response.MovieVideoResponse;
import com.example.movieapp.response.SearchDataResponse;
import com.example.movieapp.response.SearchResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieWatchingActivity extends AppCompatActivity implements WatchedMovieAdapter.OnMovieListener {

    private RecyclerView watchedResultsRecyclerView;
    private WatchedMovieAdapter adapter;
    private  MovieVideoResponse movieVideoResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_watched);

        watchedResultsRecyclerView = findViewById(R.id.watchedResultsRecyclerView);
        watchedResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo adapter có xử lý sự kiện click
        adapter = new WatchedMovieAdapter(this,this);


        watchedResultsRecyclerView.setAdapter(adapter);

        // Gọi API
        getWatchedMovie();
    }

    private void getWatchedMovie() {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        long userId = prefs.getLong("userId", -1);

        if (userId != -1) {
            Servicey.getWatchedMovieApi().getWatchedMovies(userId)
                    .enqueue(new Callback<List<WatchedMovie>>() {
                        @Override
                        public void onResponse(Call<List<WatchedMovie>> call, Response<List<WatchedMovie>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<WatchedItem> groupedItems = groupWatchedByDate(response.body());
                                adapter.setItems(groupedItems);
                            } else {
                                try {
                                    Log.e("API_ERROR", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.e("API_FAIL", "Lỗi response: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<WatchedMovie>> call, Throwable t) {
                            Log.e("API_FAIL", "Lỗi kết nối: " + t.getMessage());
                        }
                    });
        } else {
            Log.e("API", "Không tìm thấy userId trong session");
        }
    }

    // Gom nhóm phim theo ngày đã xem
    private List<WatchedItem> groupWatchedByDate(List<WatchedMovie> movies) {
        Map<String, List<WatchedMovie>> grouped = new LinkedHashMap<>();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Ngày hôm nay & hôm qua
        Calendar calendar = Calendar.getInstance();
        String todayStr = dateOnlyFormat.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String yesterdayStr = dateOnlyFormat.format(calendar.getTime());

        for (WatchedMovie movie : movies) {
            try {
                Date date = inputFormat.parse(movie.getWatchedAt());
                String dateKey = dateOnlyFormat.format(date);
                grouped.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(movie);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        List<WatchedItem> result = new ArrayList<>();
        for (String date : grouped.keySet()) {
            String title;
            if (date.equals(todayStr)) {
                title = "Hôm nay";
            } else if (date.equals(yesterdayStr)) {
                title = "Hôm qua";
            } else {
                title = date;
            }

            result.add(new WatchedItem(WatchedItem.TYPE_HEADER, title, null));
            for (WatchedMovie movie : grouped.get(date)) {
                result.add(new WatchedItem(WatchedItem.TYPE_MOVIE, null, movie));
            }
        }

        return result;
    }

    @Override
    public void onMovieClick(WatchedMovie movie) {
        getMovieVideoResponse(movie.getSlug());
        if (movieVideoResponse == null || movieVideoResponse.getEpisodes() == null || movieVideoResponse.getEpisodes().isEmpty()) {
            return;
        }
        Intent intent = new Intent(MovieWatchingActivity.this, WatchMovieActivity.class);

        intent.putExtra("movie_data", movieVideoResponse); // Truyền toàn bộ MovieVideoResponse
        startActivity(intent);
    }
    private void getMovieVideoResponse(String slug) {

        MovieApi movieApi = Servicey.getMovieApi();
        Call<MovieVideoResponse> call = movieApi.getMovie(slug);

        call.enqueue(new Callback<MovieVideoResponse>() {
            @Override
            public void onResponse(Call<MovieVideoResponse> call, Response<MovieVideoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieVideoResponse = response.body();
                    Log.e("Search", movieVideoResponse.toString());
                } else {
                    Toast.makeText(MovieWatchingActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieVideoResponse> call, Throwable t) {
                Toast.makeText(MovieWatchingActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//        return null;
    }

}
