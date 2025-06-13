package com.example.movieapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.models.MovieUrl;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {

    public interface OnEpisodeClickListener {
        void onEpisodeClick(MovieUrl movieUrl);
    }

    private final List<MovieUrl> movieUrlList;
    private final OnEpisodeClickListener listener;

    public EpisodeAdapter(List<MovieUrl> movieUrlList, OnEpisodeClickListener listener) {
        this.movieUrlList = movieUrlList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_episode, parent, false);
        return new EpisodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        MovieUrl movieUrl = movieUrlList.get(position);
        holder.episodeTitle.setText(movieUrl.getName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEpisodeClick(movieUrl);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieUrlList != null ? movieUrlList.size() : 0;
    }

    public static class EpisodeViewHolder extends RecyclerView.ViewHolder {
        TextView episodeTitle;

        public EpisodeViewHolder(@NonNull View itemView) {
            super(itemView);
            episodeTitle = itemView.findViewById(R.id.episode_title);
        }
    }
}
