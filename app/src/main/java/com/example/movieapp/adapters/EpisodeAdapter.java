package com.example.movieapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
    private int selectedIndex = 0; // mặc định chọn tập đầu tiên

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
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MovieUrl movieUrl = movieUrlList.get(position);
        Context context = holder.itemView.getContext();
        holder.episodeTitle.setText(movieUrl.getName());
//         Đổi màu nền và chữ nếu được chọn
        if (position == selectedIndex) {
            holder.itemView.setBackgroundResource(R.drawable.bg_episode_selected);
            holder.episodeTitle.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_episode_normal);
            holder.episodeTitle.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEpisodeClick(movieUrl);
            }
            selectedIndex = position;
            notifyDataSetChanged(); // cập nhật lại danh sách
        });
    }

    @Override
    public int getItemCount() {
        return movieUrlList != null ? movieUrlList.size() : 0;
    }

    public void setSelectedIndex(int index) {
        Log.e("position",index + " and " + selectedIndex);

        selectedIndex = index;
        notifyDataSetChanged();
    }

    public static class EpisodeViewHolder extends RecyclerView.ViewHolder {
        TextView episodeTitle;

        public EpisodeViewHolder(@NonNull View itemView) {
            super(itemView);
            episodeTitle = itemView.findViewById(R.id.episode_title);
        }
    }
}
