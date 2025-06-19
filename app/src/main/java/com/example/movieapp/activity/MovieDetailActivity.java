package com.example.movieapp.activity;

import com.example.movieapp.models.Episode;
import com.example.movieapp.models.User;
import com.example.movieapp.models.WatchedMovie;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.Utils.Credentials;
import com.example.movieapp.Utils.MovieApi;
import com.example.movieapp.adapters.ReviewAdapter;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.models.MovieVideo;
import com.example.movieapp.models.Review;
import com.example.movieapp.request.Servicey;
import com.example.movieapp.response.MovieVideoResponse;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity implements ReviewAdapter.OnReviewInteractionListener {

    private static final String TAG = "MovieDetailActivity";

    private ImageView movieBackdrop;
    private ImageView movieThumbnail;
    private TextView movieTitle;
    private TextView movieOverview;
    private TextView movieReleaseDate;
    private RatingBar movieRating;
    private ImageButton playTrailerButton;
    private View videoPlayerContainer;
    private PlayerView playerView;
    private ImageButton closePlayerButton;
    private MovieModel movie;
    private MovieVideo movieVideo;
    private String trailerUrl;
    private ExoPlayer player;
    private List<Episode> episode;

    // Review views
    private RatingBar userRatingBar;
    private EditText commentInput;
    private Button submitReviewButton;
    private MovieVideoResponse movieVideoResponse;
    private Button watchNowBtn;
    private FrameLayout youtubeContainer;

    private RecyclerView reviewsRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviews;

    YouTubePlayerView youtubePlayerView; // Thêm field này vào class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        youtubeContainer = findViewById(R.id.youtube_container);

        // Khởi tạo views
        initializeExistingViews();
        initializeReviewViews();

        // RatingBar
        RatingBar ratingBar = findViewById(R.id.user_rating_bar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#FFA500"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.parseColor("#FFCC80"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.parseColor("#EEEEEE"), PorterDuff.Mode.SRC_ATOP);

        // Khởi tạo YouTubePlayerView
        youtubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youtubePlayerView); // bắt buộc phải thêm dòng này

        // Lấy movie từ Intent
        movie = getIntent().getParcelableExtra("movie");
        if (movie != null) {
            displayMovieDetails();
            checkForTrailer(); // Gọi để load trailer

            loadReviews();
        } else {
            Toast.makeText(this, "Error loading movie details", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupClickListeners();
        watchNowBtn = findViewById(R.id.watch_now_button);
        watchNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieVideoResponse == null || movieVideoResponse.getEpisodes() == null || movieVideoResponse.getEpisodes().isEmpty()) {
                    return;
                }

                Intent intent = new Intent(MovieDetailActivity.this, WatchMovieActivity.class);

                saveWatchedMovie();

                intent.putExtra("movie_data", movieVideoResponse); // Truyền toàn bộ MovieVideoResponse
                startActivity(intent);
            }
        });

    }

    private void saveWatchedMovie() {
        Log.e("movie detail",movie+"");
        WatchedMovie watchedMovie = new WatchedMovie();
        watchedMovie.setTitle(movie.getTitle());
        watchedMovie.setThumbnailUrl(movie.getBackdropPath());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        watchedMovie.setWatchedAt(currentTime);
        watchedMovie.setContent(movieVideo.getContent());

        User user = new User();
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        long userId = prefs.getLong("userId", -1);
        if(userId != -1){
            user.setId(userId);
        }
        watchedMovie.setUser(user);

        Servicey.getWatchedMovieApi().addWatchedMovie(watchedMovie).enqueue(new Callback<WatchedMovie>() {
            @Override
            public void onResponse(Call<WatchedMovie> call, Response<WatchedMovie> response) {
                if (response.isSuccessful()) {
                    Log.d("API", "Đã lưu phim đã xem thành công");
                } else {
                    Log.e("API", "Lỗi khi lưu: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<WatchedMovie> call, Throwable t) {
                Log.e("API", "Lỗi kết nối: " + t.getMessage());
            }
        });

    }

    private void displayOverview(String overview){
        movieOverview = findViewById(R.id.movie_overview);
        TextView showMoreButton = findViewById(R.id.show_more_button);
        final int MAX_LINES = 4;

        movieOverview.setText(overview);

        movieOverview.post(() -> {
            if (movieOverview.getLineCount() > MAX_LINES) {
                movieOverview.setMaxLines(MAX_LINES);
                showMoreButton.setVisibility(View.VISIBLE);
            } else {
                showMoreButton.setVisibility(View.GONE);
            }
        });

        showMoreButton.setOnClickListener(v -> {
            if (movieOverview.getMaxLines() == Integer.MAX_VALUE) {
                movieOverview.setMaxLines(MAX_LINES);
                showMoreButton.setText("Show more");
            } else {
                movieOverview.setMaxLines(Integer.MAX_VALUE);
                showMoreButton.setText("Show less");
            }
        });

    }
    private void initializeExistingViews() {
        movieBackdrop = findViewById(R.id.movie_backdrop);
        movieTitle = findViewById(R.id.movie_title);
        movieOverview = findViewById(R.id.movie_overview);
        movieReleaseDate = findViewById(R.id.movie_release_date);
        movieRating = findViewById(R.id.movie_rating);
        playTrailerButton = findViewById(R.id.play_trailer_button);
        movieThumbnail = findViewById(R.id.movie_thumbnail);
//        closePlayerButton = findViewById(R.id.close_player_button);
    }

    private void initializeReviewViews() {
        userRatingBar = findViewById(R.id.user_rating_bar);
        commentInput = findViewById(R.id.comment_input);
        submitReviewButton = findViewById(R.id.submit_review_button);
        reviewsRecyclerView = findViewById(R.id.reviews_recycler_view);

        reviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsRecyclerView.setAdapter(reviewAdapter);
    }

    private void setupClickListeners() {
        playTrailerButton.setOnClickListener(v -> showYouTubePlayer());
//            // Hiện vùng chứa và player
//            FrameLayout youtubeContainer = findViewById(R.id.youtube_container);
//
//            youtubeContainer.setVisibility(View.VISIBLE);
//            youtubePlayerView.setVisibility(View.VISIBLE);
//
//            // Lấy videoId từ URL trailer
//            String videoId = Uri.parse(trailerUrl).getQueryParameter("v");
//            if (videoId != null) {
//                youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//                    @Override
//                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                        youTubePlayer.loadVideo(videoId, 0);
//                    }
//                });
//            }
//        });

    }

    private void submitReview() {
        float rating = userRatingBar.getRating();
        String comment = commentInput.getText().toString().trim();

        if (rating == 0) {
            Toast.makeText(this, "Please provide a rating", Toast.LENGTH_SHORT).show();
            return;
        }
        if (comment.isEmpty()) {
            Toast.makeText(this, "Please write a review", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        Review newReview = new Review(UUID.randomUUID().toString(), "User", comment, currentDate, rating);

        reviewAdapter.addReview(newReview);
        reviewsRecyclerView.smoothScrollToPosition(0);

        userRatingBar.setRating(0);
        commentInput.setText("");
        commentInput.clearFocus();

        Toast.makeText(this, "Review submitted successfully", Toast.LENGTH_SHORT).show();
    }

    private void loadReviews() {
        // Thêm review mẫu, thay bằng gọi API khi có backend
        List<Review> dummyReviews = new ArrayList<>();
        dummyReviews.add(new Review("1", "John Doe", "Great movie!", "2024-03-20 14:30", 4.5f));
        dummyReviews.add(new Review("2", "Jane Smith", "Interesting plot.", "2024-03-19 09:15", 3.5f));
        reviewAdapter.setReviews(dummyReviews);
    }

    private void displayMovieDetails() {
        Log.d(TAG, "Displaying details for movie: " + movie.getTitle());

//        String backdropPath = movie.getBackdropPath();
        String posterPath = movie.getPosterPath();
        String backdropPath = movie.getBackdropPath();
        if (backdropPath != null && !backdropPath.isEmpty()) {
            Glide.with(this)
                    .load(Credentials.IMAGE_URL+ "/" + backdropPath)
                    .placeholder(R.drawable.movie_placeholder)
                    .error(R.drawable.movie_placeholder)
                    .into(movieBackdrop);
        } else {
            Glide.with(this).load(R.drawable.movie_placeholder).into(movieBackdrop);
        }

        if (posterPath != null && !posterPath.isEmpty()) {
            Glide.with(this)
                    .load(Credentials.IMAGE_URL+ "/" + posterPath)
                    .placeholder(R.drawable.movie_placeholder)
                    .error(R.drawable.movie_placeholder)
                    .into(movieThumbnail);
        } else {
            Glide.with(this).load(R.drawable.movie_placeholder).into(movieThumbnail);
        }
        movieTitle.setText(movie.getTitle());
        movieRating.setRating(movie.getRatings().getVote_average() / 2f);
    }


    private void checkForTrailer() {
        String slug = movie.getSlug();

        MovieApi movieApi = Servicey.getMovieApi();
        Call<MovieVideoResponse> responseCall = movieApi.getMovie(slug);
        Log.e("movieVideo",responseCall+"");

        responseCall.enqueue(new Callback<MovieVideoResponse>() {
            @Override
            public void onResponse(Call<MovieVideoResponse> call, Response<MovieVideoResponse> response) {
                if (!response.isSuccessful()) {
//                    Toast.makeText(MovieDetailActivity.this, "Không lấy được trailer", Toast.LENGTH_SHORT).show();
                    playTrailerButton.setVisibility(View.GONE);
                    return;
                }

                movieVideoResponse = response.body();
                movieVideo = movieVideoResponse.getVideo();
                episode = movieVideoResponse.getEpisodes();
                Log.e("movieVideo",movieVideoResponse.getEpisodes()+"");
                movieReleaseDate.setText("năm xuất bản " + movie.getYear());
                displayOverview(movieVideo.getContent());

                trailerUrl = response.body().getVideo().getTrailerUrl(); // cập nhật biến toàn cục
                if (trailerUrl != null && trailerUrl.contains("youtube.com")) {
                    playTrailerButton.setVisibility(View.VISIBLE); // ✅ hiện nút nếu có trailer
                    loadTrailer(trailerUrl);
                } else {
                    playTrailerButton.setVisibility(View.GONE);
                    Toast.makeText(MovieDetailActivity.this, "Không có trailer", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieVideoResponse> call, Throwable t) {
                Toast.makeText(MovieDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                playTrailerButton.setVisibility(View.GONE);
            }
        });
    }

    private void loadTrailer(String trailerUrl) {
        String videoId = extractYouTubeVideoId(trailerUrl);
        if (videoId != null) {
            youtubePlayerView.setVisibility(View.VISIBLE);
            youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0);
                }
            });
        }
    }
    private String extractYouTubeVideoId(String url) {
        Uri uri = Uri.parse(url);
        return uri.getQueryParameter("v"); // Trích videoId từ https://www.youtube.com/watch?v=Iru7Q2qmijQ
    }
    private void showYouTubePlayer() {
        movieBackdrop.setVisibility(View.GONE);
        playTrailerButton.setVisibility(View.GONE);

        // Bổ sung dòng này để hiện container
        youtubeContainer.setVisibility(View.VISIBLE);
        youtubePlayerView.setVisibility(View.VISIBLE);

        getLifecycle().addObserver(youtubePlayerView);

        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = extractVideoIdFromUrl(trailerUrl);
                youTubePlayer.loadVideo(videoId, 0);
            }
        });
    }


    private String extractVideoIdFromUrl(String url) {
        Uri uri = Uri.parse(url);
        String videoId = uri.getQueryParameter("v");
        if (videoId == null && url.contains("youtu.be/")) {
            videoId = url.substring(url.lastIndexOf("/") + 1);
        }
        return videoId;
    }

    private void hideVideoPlayer() {
        videoPlayerContainer.setVisibility(View.GONE);
        movieBackdrop.setVisibility(View.VISIBLE);
        playTrailerButton.setVisibility(View.VISIBLE);
        player.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) player.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) player.pause();
    }

    @Override
    public void onLikeClicked(Review review, int position) {
        review.toggleLike();
        reviewAdapter.notifyItemChanged(position);
    }

    @Override
    public void onReplyClicked(Review review, int position) {
        Toast.makeText(this, "Reply feature coming soon!", Toast.LENGTH_SHORT).show();
    }
}