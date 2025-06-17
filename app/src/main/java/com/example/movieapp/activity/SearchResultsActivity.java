package com.example.movieapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.Utils.Credentials;
import com.example.movieapp.Utils.MovieApi;
import com.example.movieapp.adapters.MovieRecyclerAdaptor;
import com.example.movieapp.adapters.OnMovieListener;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.request.Servicey;
import com.example.movieapp.response.MovieSearchResponse;
import com.example.movieapp.response.SearchDataResponse;
import com.example.movieapp.response.SearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultsActivity extends AppCompatActivity implements OnMovieListener {

    private RecyclerView searchResultsRecyclerView;
    private MovieRecyclerAdaptor adaptor;
    private TextView searchResultsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        searchResultsTitle = findViewById(R.id.searchResultsTitle);

        searchResultsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Hiển thị 2 cột
        adaptor = new MovieRecyclerAdaptor(this);
        searchResultsRecyclerView.setAdapter(adaptor);

        // Lấy query truyền từ MainActivity
        String query = getIntent().getStringExtra("query");
        searchResultsTitle.setText("Search Results for: \"" + query + "\"");

        // Gọi API tìm kiếm phim với query
        searchMoviesFromApi(query);
    }

    private void searchMoviesFromApi(String query) {
        MovieApi movieApi = Servicey.getMovieApi();
        Call<SearchResponse> call = movieApi.searchMovies(
                query,
                "10" // page = 1
        );

        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SearchDataResponse movies = response.body().getData();
                    Log.e("Search",movies+"");
                    adaptor.setModelList(movies);
                } else {
                    Toast.makeText(SearchResultsActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(SearchResultsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        // Xử lý sự kiện khi người dùng click phim trong danh sách
        MovieModel selectedMovie = adaptor.getSelectedMovie(position);
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movie", selectedMovie);
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        // Nếu có xử lý sự kiện click category, xử lý ở đây
    }
}