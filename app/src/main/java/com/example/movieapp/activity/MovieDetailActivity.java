package com.example.movieapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private TextView movieTitle;
    private TextView movieOverview;
    private TextView movieReleaseDate;
    private RatingBar movieRating;
    private ImageButton playTrailerButton;
    private View videoPlayerContainer;
    private PlayerView playerView;
    private ImageButton closePlayerButton;
    private MovieModel movie;
    private String trailerUrl;
    private ExoPlayer player;

    // Review views
    private RatingBar userRatingBar;
    private EditText commentInput;
    private Button submitReviewButton;
    private RecyclerView reviewsRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Khởi tạo views
        initializeExistingViews();
        initializeReviewViews();

        // ⚠️ Thêm đoạn xử lý màu RatingBar ở đây
        RatingBar ratingBar = findViewById(R.id.user_rating_bar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#FFA500"), PorterDuff.Mode.SRC_ATOP); // đã chọn
        stars.getDrawable(1).setColorFilter(Color.parseColor("#FFCC80"), PorterDuff.Mode.SRC_ATOP); // nửa
        stars.getDrawable(0).setColorFilter(Color.parseColor("#EEEEEE"), PorterDuff.Mode.SRC_ATOP); // chưa chọn

        // Khởi tạo ExoPlayer và gán cho PlayerView
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        // Lấy movie từ Intent
        movie = getIntent().getParcelableExtra("movie");
        if (movie != null) {
            displayMovieDetails();

            playTrailerButton.setVisibility(View.GONE); // Ẩn nút play trailer lúc đầu
            checkForTrailer(); // Tìm trailer và hiện nút nếu có
            loadReviews(); // Load review mẫu hoặc API
        } else {
            Toast.makeText(this, "Error loading movie details", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupClickListeners();
    }


    private void initializeExistingViews() {
        movieBackdrop = findViewById(R.id.movie_backdrop);
        movieTitle = findViewById(R.id.movie_title);
        movieOverview = findViewById(R.id.movie_overview);
        movieReleaseDate = findViewById(R.id.movie_release_date);
        movieRating = findViewById(R.id.movie_rating);
        playTrailerButton = findViewById(R.id.play_trailer_button);
        videoPlayerContainer = findViewById(R.id.video_player_container);
        playerView = findViewById(R.id.player_view);
        closePlayerButton = findViewById(R.id.close_player_button);
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
        playTrailerButton.setOnClickListener(v -> {
            if (trailerUrl != null && !trailerUrl.isEmpty()) {
                showVideoPlayer();
            } else {
                Toast.makeText(this, "No trailer available", Toast.LENGTH_SHORT).show();
            }
        });

        closePlayerButton.setOnClickListener(v -> hideVideoPlayer());

        submitReviewButton.setOnClickListener(v -> submitReview());
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

        String backdropPath = movie.getBackdrop_path();
        if (backdropPath != null && !backdropPath.isEmpty()) {
            Glide.with(this)
                    .load(Credentials.IMAGE_URL + backdropPath)
                    .placeholder(R.drawable.movie_placeholder)
                    .error(R.drawable.movie_placeholder)
                    .into(movieBackdrop);
        } else {
            Glide.with(this).load(R.drawable.movie_placeholder).into(movieBackdrop);
        }

        movieTitle.setText(movie.getTitle());
        movieOverview.setText(movie.getOverview());
        movieReleaseDate.setText(movie.getRelease_date());
        movieRating.setRating(movie.getVote_average() / 2f);
    }

    private void checkForTrailer() {
        int movieId = movie.getId();
        Log.d(TAG, "Checking for trailer availability for movie ID: " + movieId);

        MovieApi movieApi = Servicey.getMovieApi();
        Call<MovieVideoResponse> responseCall = movieApi.getMovieVideos(movieId, Credentials.API_KEY, "en-US");

        responseCall.enqueue(new Callback<MovieVideoResponse>() {
            @Override
            public void onResponse(Call<MovieVideoResponse> call, Response<MovieVideoResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Error checking for trailer: " + response.code());
                    Toast.makeText(MovieDetailActivity.this, "Failed to load trailer", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.body() == null || response.body().getVideos() == null) {
                    Log.e(TAG, "Response body or videos is null");
                    return;
                }

                List<MovieVideo> videos = response.body().getVideos();
                Log.d(TAG, "Number of videos received: " + videos.size());

                for (MovieVideo video : videos) {
                    if ("Trailer".equalsIgnoreCase(video.getType())) {
                        String videoKey = video.getKey();
                        trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
                        // Hoặc trailerUrl = "https://www.youtube.com/watch?v=" + videoKey; -> Ko chơi trực tiếp với ExoPlayer
                        playTrailerButton.setVisibility(View.VISIBLE);
                        return;
                    }
                }

                Log.d(TAG, "No suitable trailer found");
                Toast.makeText(MovieDetailActivity.this, "No trailer available", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MovieVideoResponse> call, Throwable t) {
                Log.e(TAG, "Network error when checking for trailer", t);
                Toast.makeText(MovieDetailActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showVideoPlayer() {
        movieBackdrop.setVisibility(View.GONE);
        playTrailerButton.setVisibility(View.GONE);
        videoPlayerContainer.setVisibility(View.VISIBLE);

        player.stop();
        player.clearMediaItems();

        MediaItem mediaItem = MediaItem.fromUri(trailerUrl);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
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
