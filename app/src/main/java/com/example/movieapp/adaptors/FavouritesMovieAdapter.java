package com.example.movieapp.adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.activity.MovieDetailActivity;
import com.example.movieapp.R;
import com.example.movieapp.Utils.Credentials;
import com.example.movieapp.models.MovieModel;

import java.util.List;

public class FavouritesMovieAdapter extends RecyclerView.Adapter<FavouritesMovieAdapter.MovieViewHolder> {
    private Context context;
    private List<MovieModel> modelList;

    public FavouritesMovieAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieModel model = modelList.get(position);
        
        // Set movie title
        holder.title.setText(model.getTitle());
        
        // Set movie rating
        float rating = model.getVote_average() / 2;
        holder.rating.setText(String.format("%.1f", rating));

        // Load movie poster using Glide
        Glide.with(context)
                .load(Credentials.IMAGE_URL + model.getPoster_path())
                .into(holder.imageView);

        // Set click listener for movie details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra("movie", model);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (modelList != null) {
            return modelList.size();
        }
        return 0;
    }

    public void setModelList(List<MovieModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, rating;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movie_img);
            title = itemView.findViewById(R.id.movie_title);
            rating = itemView.findViewById(R.id.rating);
        }
    }
} 