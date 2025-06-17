package com.example.movieapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.example.movieapp.R;
import com.example.movieapp.Utils.Credentials;
import com.example.movieapp.Utils.MovieApi;
import com.example.movieapp.adapters.BannerAdapter;
import com.example.movieapp.adapters.MovieRecyclerAdaptor;
import com.example.movieapp.adapters.OnMovieListener;
import com.example.movieapp.adapters.PopularMovieAdaptor;
import com.example.movieapp.adapters.UpcomingMovieAdapter;
import com.example.movieapp.adapters.TrendingMovieAdapter;
import com.example.movieapp.adapters.TopRatedMovieAdapter;
import com.example.movieapp.adapters.FavouritesMovieAdapter;
import com.example.movieapp.models.BannerItem;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.request.Servicey;
import com.example.movieapp.response.MovieSearchResponse;
import com.example.movieapp.response.MovieVideoResponse;
import com.example.movieapp.response.SearchResponse;
import com.example.movieapp.viewmodel.MovieListViewModel;

import androidx.viewpager2.widget.ViewPager2;
import android.os.Handler;
import android.os.Looper;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMovieListener {

    private RecyclerView recyclerView, mainRecyclerView, upcomingRecyclerView;
    private RecyclerView trendingRecyclerView, topRatedRecyclerView, favouritesRecyclerView;
    private MovieRecyclerAdaptor adaptor;
    private PopularMovieAdaptor popularAdaptor;
    private UpcomingMovieAdapter upcomingAdapter;
    private TrendingMovieAdapter trendingMovieAdapter;
    private TopRatedMovieAdapter topRatedMovieAdapter;
    private FavouritesMovieAdapter favouritesMovieAdapter;
    private MovieListViewModel viewModel;
    private LinearLayout mainScreenLL, recyclerLL;
    private TextView pickOfTheDay;
    private ViewPager2 viewPagerSlider;
    private Handler bannerHandler = new Handler(Looper.getMainLooper());
    private Runnable bannerRunnable;
    private int currentBannerPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPagerSlider = findViewById(R.id.viewpagerSlider);

// Tạo danh sách ảnh banner
        List<BannerItem> banners = new ArrayList<>();
        banners.add(new BannerItem(R.drawable.wide));
        banners.add(new BannerItem(R.drawable.wide1));
//        banners.add(new BannerItem(R.drawable.wide3));

// Gắn adapter
        BannerAdapter bannerAdapter = new BannerAdapter(banners);
        viewPagerSlider.setAdapter(bannerAdapter);

// Tự động chuyển banner mỗi 3 giây
        bannerRunnable = new Runnable() {
            @Override
            public void run() {
                if (bannerAdapter.getItemCount() == 0) return;
                currentBannerPosition = (currentBannerPosition + 1) % bannerAdapter.getItemCount();
                viewPagerSlider.setCurrentItem(currentBannerPosition, true);
                bannerHandler.postDelayed(this, 3000);
            }
        };
        bannerHandler.postDelayed(bannerRunnable, 3000);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize RecyclerViews
        recyclerView = findViewById(R.id.recyclerView);
        mainRecyclerView = findViewById(R.id.mainScreenRecyclerView);
        upcomingRecyclerView = findViewById(R.id.upcomingRecyclerView);
        trendingRecyclerView = findViewById(R.id.trendingRecyclerView);
        topRatedRecyclerView = findViewById(R.id.topRatedRecyclerView);
        favouritesRecyclerView = findViewById(R.id.favouritesRecyclerView);

        // Initialize other views
        mainScreenLL = findViewById(R.id.mainScreenLinearLayout);
        recyclerLL = findViewById(R.id.recyclerLinearLayout);
        pickOfTheDay = findViewById(R.id.trendingTextView);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        // Setup all RecyclerViews
//        setupRecyclerViews();
        setupPopularMovies();
        setupTrendingMovies();
        setupUpcomingMovies();
        setupTopRatedMovies();
//        setupFavouritesMovies();

        // Setup search
        setupSearchView();
//        viewModel.getMovies().observe(this, movieModels -> {
//            Log.d("SEARCH_RESULT", "List nhận về: " + (movieModels == null ? "null" : movieModels.size()+" items"));
//            if (movieModels != null) {
//                for (MovieModel m : movieModels) {
//                }
//            }
//            adaptor.setModelList(movieModels);
//        });

        // Observe data changes
        observeAnyChange();
//        viewModel.getMovies().observe(this, movieModels -> {
//            // Khi có dữ liệu search, cập nhật lên adaptor
//            adaptor.setModelList(movieModels);
//        });
        ImageButton profileBtn = findViewById(R.id.btn_profile);
        profileBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        });
    }

    private void setupRecyclerViews() {
        adaptor = new MovieRecyclerAdaptor(this); // this implements OnMovieListener
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setVisibility(View.GONE); // Ẩn ban đầu, chỉ hiện khi search

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.searchNextPage();
                }
            }
        });
    }

    private void loadMovies() {
        // Load popular movies
        viewModel.searchPopularMovieApi(1);

        viewModel.getPopularMoviesList().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if (movieModels != null) {
                    popularAdaptor.setModelList(movieModels);
                }
            }
        });

        // Load upcoming movies
        viewModel.searchUpcomingMovies(1);
        viewModel.getUpcomingMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if (movieModels != null) {
                    upcomingAdapter.setMovieList(movieModels);
                }
            }
        });

        // Observe search results
//        viewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
//            @Override
//            public void onChanged(List<MovieModel> movieModels) {
//                if (movieModels != null) {
//                    adaptor.setModelList(movieModels);
//                }
//            }
//        });
    }

    private void setupSearchView() {
        final SearchView searchView = findViewById(R.id.search_View);
        EditText searchEditText = (EditText) searchView.findViewById(
                androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.WHITE);         // Màu chữ nhập
        searchEditText.setHintTextColor(Color.GRAY);      // Màu hint

        searchView.setOnSearchClickListener(v -> {
            mainScreenLL.setVisibility(View.GONE);
            recyclerLL.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
//            adaptor.setModelList(new ArrayList<>()); // clear dữ liệu cũ nếu cần
        });

        searchView.setOnCloseListener(() -> {
            mainScreenLL.setVisibility(View.VISIBLE);
            recyclerLL.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.trim().isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                    intent.putExtra("query", query.trim());
                    startActivity(intent);
                    searchView.clearFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }


    private void getRetrofitResponseToId() {
        MovieApi movieApi = Servicey.getMovieApi();

        Call<MovieVideoResponse> responseCall = movieApi.getMovie("500");

//        responseCall.enqueue(new Callback<MovieModel>() {
//            @Override
//            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
//                if (response.code() == 200) {
//                    Toast.makeText(MainActivity.this, "Success by ID", Toast.LENGTH_SHORT).show();
//
//                    MovieModel movie = response.body();
//                    assert movie != null;
//                } else {
//                    try {
//                        Toast.makeText(MainActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
//                        assert response.errorBody() != null;
//                        Log.d("TAG", "onResponse: " + response.errorBody().string());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieModel> call, Throwable t) {
//            }
//        });
    }



    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movie", adaptor.getSelectedMovie(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        // Handle category click if needed
    }

    @Override
    public void onBackPressed() {
        if (recyclerLL.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);
            recyclerLL.setVisibility(View.GONE);
        } else if (mainScreenLL.getVisibility() == View.GONE) {
            pickOfTheDay.setVisibility(View.VISIBLE);
            mainScreenLL.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            recyclerLL.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    private void setupTrendingMovies() {
        trendingMovieAdapter = new TrendingMovieAdapter(this);
        trendingRecyclerView.setAdapter(trendingMovieAdapter);
        trendingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        // Getting trending movies
        viewModel.searchTrendingMovies(1);
    }

    private void setupTopRatedMovies() {
        topRatedMovieAdapter = new TopRatedMovieAdapter(this);
        topRatedRecyclerView.setAdapter(topRatedMovieAdapter);
        topRatedRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Getting top rated movies
        viewModel.searchTopRatedMovies(1);
    }

    private void setupFavouritesMovies() {
        favouritesMovieAdapter = new FavouritesMovieAdapter(this);
        favouritesRecyclerView.setAdapter(favouritesMovieAdapter);
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Load favourites from local storage or database
        loadFavouriteMovies();
    }

    private void loadFavouriteMovies() {
        // TODO: Implement loading favourites from local storage
        // This will be implemented when we add local storage functionality
    }

    private void observeAnyChange() {
        // Observing trending movies

        // Observing popular movies
        viewModel.getPopularMoviesList().observe(this, movieModels -> {
            if (movieModels != null) {
                Log.d("Movies", "Popular movies loaded: " + movieModels.size());
                popularAdaptor.setModelList(movieModels);
            } else {
                Log.e("Movies", "Popular movies response was null");
            }
        });
        viewModel.getTrendingMovies().observe(this, movieModels -> {
            if (movieModels != null) {
                Log.d("Movies", "Trending movies loaded: " + movieModels.size());
                trendingMovieAdapter.setModelList(movieModels);
            }
        });

        // Observing top rated movies

        // Observing upcoming movies
        viewModel.getUpcomingMovies().observe(this, movieModels -> {
            if (movieModels != null) {
                upcomingAdapter.setMovieList(movieModels);
            }
        });
        viewModel.getTopRatedMovies().observe(this, movieModels -> {
            if (movieModels != null) {
                Log.d("Movies", "Top rated movies loaded: " + movieModels.size());
                topRatedMovieAdapter.setModelList(movieModels);
            }
        });

        // Observing favourite movies
        viewModel.getFavouriteMovies().observe(this, movieModels -> {
            if (movieModels != null) {
                favouritesMovieAdapter.setModelList(movieModels);
            }
        });
    }

    private void setupPopularMovies() {
        // Setup Popular Movies RecyclerView
        Log.d("PopularMovies", "Setting up popular movies adapter");
        popularAdaptor = new PopularMovieAdaptor(this);
        mainRecyclerView.setAdapter(popularAdaptor);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Load popular movies
        Log.d("PopularMovies", "Requesting popular movies data");
        viewModel.searchPopularMovieApi(1);
    }

    private void setupUpcomingMovies() {
        // Setup Upcoming Movies RecyclerView
        upcomingAdapter = new UpcomingMovieAdapter(this, this);
        upcomingRecyclerView.setAdapter(upcomingAdapter);
        upcomingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Load upcoming movies
        viewModel.searchUpcomingMovies(1);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bannerHandler.removeCallbacks(bannerRunnable);
    }
}