package com.example.movieapp.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movieapp.R;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);

        // Setup RecyclerView
        recyclerView = findViewById(R.id.favorite_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Không cần thêm adapter vì danh sách rỗng
    }
}
