package com.example.movieapp.adapters;

import android.content.Context;
import android.credentials.Credential;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.Utils.AppCredentials;
import com.example.movieapp.Utils.Credentials;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.response.SearchDataResponse;

import java.util.List;

public class MovieRecyclerAdaptor extends RecyclerView.Adapter<MovieViewHolder> {

    private OnMovieListener onMovieListener;
    private SearchDataResponse modelList;

    private Context context;

    public MovieRecyclerAdaptor(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }



    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.movie_list_item,parent,false);
        return new MovieViewHolder(view,onMovieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        MovieModel movieModel= modelList.getItems().get(position);

        holder.title.setText(movieModel.getTitle());

        Glide.with(holder.itemView.getContext())
                .load(Credentials.IMAGE_URL+"/" +movieModel.getPosterPath())
                .into(holder.movieImage);


    }

    @Override
    public int getItemCount() {

        if(modelList!=null)
            return modelList.getItems().size();

        return 0;

    }

    public void setModelList(SearchDataResponse modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    //  Getting the id's of the selected movie
    public MovieModel getSelectedMovie(int position){

        if( modelList != null ){
            if( modelList.getItems().size() > 0 ){
                return modelList.getItems().get(position);
            }
        }


        return null;
    }

}