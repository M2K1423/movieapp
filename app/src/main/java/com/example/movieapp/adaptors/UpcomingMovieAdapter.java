package com.example.movieapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.models.MovieModel;

import java.util.List;

public class UpcomingMovieAdapter extends RecyclerView.Adapter<UpcomingMovieAdapter.ViewHolder> {

    private Context context;
    private List<MovieModel> list;
    private OnMovieListener onMovieListener;

    public UpcomingMovieAdapter(Context context, OnMovieListener onMovieListener) {
        this.context = context;
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieModel movieModel = list.get(position);

        holder.titleTextView.setText(movieModel.getTitle());
        holder.releaseDateTextView.setText(movieModel.getRelease_date());

        Glide.with(holder.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w500/" + movieModel.getPoster_path())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            if (onMovieListener != null) {
                onMovieListener.onMovieClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    public void setMovieList(List<MovieModel> movieList) {
        this.list = movieList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView titleTextView;
        private TextView releaseDateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movieImage_Upcoming_item);
            titleTextView = itemView.findViewById(R.id.movieTitle_Upcoming);
            releaseDateTextView = itemView.findViewById(R.id.releaseDate_Upcoming);
        }
    }
} 