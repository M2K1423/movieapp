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
import com.example.movieapp.Utils.Credentials;
import com.example.movieapp.models.WatchedItem;
import com.example.movieapp.models.WatchedMovie;

import java.util.ArrayList;
import java.util.List;

public class WatchedMovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = WatchedItem.TYPE_HEADER;
    private static final int TYPE_MOVIE = WatchedItem.TYPE_MOVIE;

    private final Context context;
    private final OnMovieListener onMovieListener;
    private List<WatchedItem> items = new ArrayList<>();

    // Interface callback để xử lý khi click vào movie
    public interface OnMovieListener {
        void onMovieClick(WatchedMovie movie);
    }

    public WatchedMovieAdapter(Context context, OnMovieListener onMovieListener) {
        this.context = context;
        this.onMovieListener = onMovieListener;
    }

    public void setItems(List<WatchedItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_date_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_watch_video, parent, false);
            return new MovieViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WatchedItem item = items.get(position);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).textHeader.setText(item.getHeaderTitle());
        } else if (holder instanceof MovieViewHolder) {
            WatchedMovie movie = item.getMovie();
            MovieViewHolder movieHolder = (MovieViewHolder) holder;

            movieHolder.textTitle.setText(movie.getTitle());
            movieHolder.textContent.setText(movie.getContent());

            Glide.with(context)
                    .load(Credentials.IMAGE_URL + "/" + movie.getThumbnailUrl())
                    .placeholder(R.drawable.movie_placeholder)
                    .into(movieHolder.imagePoster);

            holder.itemView.setOnClickListener(v -> {
                if (onMovieListener != null) {
                    onMovieListener.onMovieClick(movie);
                }
            });
        }
    }

    // ViewHolder cho header (ngày)
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView textHeader;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            textHeader = itemView.findViewById(R.id.textView);
        }
    }

    // ViewHolder cho movie
    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePoster;
        TextView textTitle, textContent;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePoster = itemView.findViewById(R.id.videoThumbnail);
            textTitle = itemView.findViewById(R.id.videoTitle);
            textContent = itemView.findViewById(R.id.textContent);
        }
    }
}
