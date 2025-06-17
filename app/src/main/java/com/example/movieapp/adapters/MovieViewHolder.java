package com.example.movieapp.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    public TextView title, label;
    public ImageView movieImage;

    OnMovieListener onMovieListener;

    public MovieViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);

        this.onMovieListener = onMovieListener;

        title = itemView.findViewById(R.id.movie_title);
        label = itemView.findViewById(R.id.movie_label);
        movieImage = itemView.findViewById(R.id.movie_img);

        itemView.setOnClickListener(v -> {
            if (onMovieListener != null) {
                onMovieListener.onMovieClick(getAdapterPosition());
            }
        });
    }
}
