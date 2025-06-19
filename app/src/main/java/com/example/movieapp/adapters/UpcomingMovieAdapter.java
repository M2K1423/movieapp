package com.example.movieapp.adapters;

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
        MovieModel model = list.get(position);

        holder.titleTextView.setText(model.getTitle());
//        holder.releaseDateTextView.setText(model.getYear());
//        float rating = model.getRatings().getVote_average() / 2;
//        holder.rating.setText(String.format("%.1f", rating));
//


        Glide.with(context)
                .load("http://phimimg.com/" + model.getPosterPath())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            if (onMovieListener != null) {
                onMovieListener.onMovieClick(position);
            }
        });
    }
    public String converCountRating(int countRating){
        String res = "" + countRating;
        if(res.equals("0"))
            return "Chưa có";
        if(countRating > 1000){
            int thousandths =  countRating / 1000;
            res = thousandths + "." + (countRating % 1000) / 100 + "k";
        }
        return res;
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
        TextView  rating,ratingCount ;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movieImage_Upcoming_item);
            titleTextView = itemView.findViewById(R.id.movieTitle_Upcoming);
            releaseDateTextView = itemView.findViewById(R.id.releaseDate_Upcoming);
            rating = itemView.findViewById(R.id.rating);
            ratingCount = itemView.findViewById(R.id.rating_count);
        }
    }
} 