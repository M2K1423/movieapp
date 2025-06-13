package com.example.movieapp.activity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.media3.common.MediaItem;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.ui.PlayerView;

import com.example.movieapp.R;
import com.example.movieapp.adapters.EpisodeAdapter;
import com.example.movieapp.models.Episode;
import com.example.movieapp.models.MovieUrl;
import com.example.movieapp.response.MovieVideoResponse;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.HttpsURLConnection;

public class WatchMovieActivity extends AppCompatActivity {

    private int isVietsub = 0;
    private int currentEpisodeIndex = 0;

    private PlayerView playerView;
    private ExoPlayer exoPlayer;
    private RecyclerView episodeRecyclerView;
    private MovieVideoResponse movieData;
    private List<MovieUrl> allMovieUrls = new ArrayList<>();
    private Button btn_vietsub;
    private Button btn_dub;

    @OptIn(markerClass = UnstableApi.class)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_movie);

        playerView = findViewById(R.id.player_view);
        episodeRecyclerView = findViewById(R.id.episode_recycler_view);
        ImageButton backButton = findViewById(R.id.back_button);
        TextView titleText = findViewById(R.id.movie_title_text);
        btn_vietsub = findViewById(R.id.btn_vietsub);
        btn_dub = findViewById(R.id.btn_dub);

        backButton.setOnClickListener(v -> finish());

        movieData = getIntent().getParcelableExtra("movie_data");

        if (movieData == null || movieData.getEpisodes() == null || movieData.getEpisodes().isEmpty()) {
            Toast.makeText(this, "Không có tập phim để xem", Toast.LENGTH_SHORT).show();
            return;
        }

        // HTTP & SSL Bypass setup
        DefaultHttpDataSource.Factory httpDataSourceFactory = createInsecureHttpDataSourceFactory();
        DefaultDataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(this, httpDataSourceFactory);
        DefaultMediaSourceFactory mediaSourceFactory = new DefaultMediaSourceFactory(dataSourceFactory);

        exoPlayer = new ExoPlayer.Builder(this)
                .setMediaSourceFactory(mediaSourceFactory)
                .build();
        exoPlayer.setVolume(1f);
        playerView.setPlayer(exoPlayer);

        titleText.setText(movieData.getVideo().getName());

        btn_vietsub.setSelected(true);
        btn_dub.setSelected(false);

        if (isVietsub == 0) {
            btn_vietsub.setBackgroundResource(R.drawable.bg_btn_sub_selected);
            btn_dub.setBackgroundResource(R.drawable.bg_btn_sub);
        }

        // Default: Vietsub
        getEpisode(isVietsub);

        if (allMovieUrls.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu video", Toast.LENGTH_SHORT).show();
            return;
        }

        playEpisode(allMovieUrls.get(0), 0);
        Log.e("WatchMovie", "Phát video: " + allMovieUrls.get(0).getLink_m3u8());

        setupEpisodeRecycler();

        btn_vietsub.setOnClickListener(v -> {
            isVietsub = 0;
            switchEpisodeLanguage(isVietsub);
        });

        btn_dub.setOnClickListener(v -> {
            isVietsub = 1;
            switchEpisodeLanguage(isVietsub);
        });
    }

    public void getEpisode(int flag) {
        allMovieUrls.clear();
        Episode episode = movieData.getEpisodes().get(flag);
        allMovieUrls.addAll(episode.getMovieUrl());
    }

    private void switchEpisodeLanguage(int flag) {
        long playbackPosition = exoPlayer.getCurrentPosition();

        getEpisode(flag);
        btn_vietsub.setSelected(true);
        btn_dub.setSelected(false);


        // Update UI trạng thái nút được chọn
        if (flag == 0) {
            btn_vietsub.setBackgroundResource(R.drawable.bg_btn_sub_selected);
            btn_dub.setBackgroundResource(R.drawable.bg_btn_sub);
        } else {
            btn_dub.setBackgroundResource(R.drawable.bg_btn_sub_selected);
            btn_vietsub.setBackgroundResource(R.drawable.bg_btn_sub);
        }

        if (allMovieUrls.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu video", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentEpisodeIndex >= 0 && currentEpisodeIndex < allMovieUrls.size()) {
            playEpisode(allMovieUrls.get(currentEpisodeIndex), playbackPosition);
        } else {
            currentEpisodeIndex = 0;
            playEpisode(allMovieUrls.get(0), 0);
        }

        setupEpisodeRecycler();
    }


    private void setupEpisodeRecycler() {
        EpisodeAdapter adapter = new EpisodeAdapter(allMovieUrls, movieUrl -> playEpisode(movieUrl, 0));
        episodeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        episodeRecyclerView.setAdapter(adapter);
    }

    private void playEpisode(MovieUrl movieUrl, long startPositionMs) {
        if (movieUrl == null || movieUrl.getLink_m3u8() == null) {
            Toast.makeText(this, "Không thể phát video", Toast.LENGTH_SHORT).show();
            return;
        }

        currentEpisodeIndex = allMovieUrls.indexOf(movieUrl);

        Uri videoUri = Uri.parse(movieUrl.getLink_m3u8());
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();

        // Dùng listener đúng với androidx.media3
        androidx.media3.common.Player.Listener[] listenerHolder = new androidx.media3.common.Player.Listener[1];

        listenerHolder[0] = new androidx.media3.common.Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == ExoPlayer.STATE_READY) {
                    exoPlayer.seekTo(startPositionMs);
                    exoPlayer.play();
                    exoPlayer.removeListener(listenerHolder[0]);
                }
            }
        };

        exoPlayer.addListener(listenerHolder[0]);
    }


    private DefaultHttpDataSource.Factory createInsecureHttpDataSourceFactory() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            return new DefaultHttpDataSource.Factory()
                    .setAllowCrossProtocolRedirects(true)
                    .setConnectTimeoutMs(10000)
                    .setReadTimeoutMs(10000);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT <= 23 && exoPlayer != null) {
            exoPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > 23 && exoPlayer != null) {
            exoPlayer.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
