package com.example.movieapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 4000; // 5 giây

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView splashText = findViewById(R.id.splash_text);

        // Animation fade-in
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(2000);
        splashText.startAnimation(fadeIn);

        // Đợi 5 giây rồi chuyển sang LoginActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, SPLASH_DURATION);
    }
}
