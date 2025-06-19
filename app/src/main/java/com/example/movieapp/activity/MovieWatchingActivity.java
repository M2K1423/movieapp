package com.example.movieapp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.Utils.AppCredentials;
import com.example.movieapp.adapters.MovieRecyclerAdaptor;
import com.example.movieapp.adapters.OnMovieListener;
import com.example.movieapp.adapters.WatchedMovieAdapter;
import com.example.movieapp.models.WatchedItem;
import com.example.movieapp.models.WatchedMovie;
import com.example.movieapp.request.Servicey;

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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieWatchingActivity extends AppCompatActivity implements OnMovieListener {

    private RecyclerView watchedResultsRecyclerView;
    private WatchedMovieAdapter  adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_watched);

        // Ánh xạ và cấu hình RecyclerView
        watchedResultsRecyclerView = findViewById(R.id.watchedResultsRecyclerView);
        watchedResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adaptor = new WatchedMovieAdapter(this); // OnMovieListener là this
        watchedResultsRecyclerView.setAdapter(adaptor);

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
                            Log.e("grouped",response.body().toString());
                            adaptor.setItems(groupedItems);
                        } else {
                            try {
                                Log.e("API_ERROR", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.e("API hihi", "Lỗi response: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<WatchedMovie>> call, Throwable t) {
                        Log.e("API", "Lỗi kết nối: " + t.getMessage());
                    }
                });
        } else {
            Log.e("API", "Không tìm thấy userId trong session");
        }
    }

    private List<WatchedItem> groupWatchedByDate(List<WatchedMovie> movies) {
        Map<String, List<WatchedMovie>> grouped = new LinkedHashMap<>();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Lấy ngày hiện tại và hôm qua
        Calendar calendar = Calendar.getInstance();
        String todayStr = dateOnlyFormat.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String yesterdayStr = dateOnlyFormat.format(calendar.getTime());

        for (WatchedMovie movie : movies) {
            try {
                Date date = inputFormat.parse(movie.getWatchedAt());
                String dateKey = dateOnlyFormat.format(date); // "dd/MM/yyyy"
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
    public void onMovieClick(int position) {
        // Xử lý khi người dùng click vào 1 bộ phim đã xem (tuỳ bạn)
    }

    @Override
    public void onCategoryClick(String category) {
        // Không dùng trong danh sách phim đã xem (có thể để trống)
    }
}
