package com.example.movieapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieModel implements Parcelable {

    private String title;
    private String poster_path;
    private String backdrop_path;
    
    @SerializedName("id")
    private int movie_id;
    
    private String release_date;
    private float vote_average;

    @SerializedName("overview")
    @Expose
    private String movie_overview;

    public MovieModel() {

    }


    // Setter
    public void setTitle(String title) {
        this.title = title;
    }
    private int runtime;

    public MovieModel(Parcel in) {
        title = in.readString();
        poster_path = in.readString();
        backdrop_path = in.readString();
        movie_id = in.readInt();
        release_date = in.readString();
        vote_average = in.readFloat();
        movie_overview = in.readString();
        runtime = in.readInt();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public int getId() {
        return movie_id;
    }

    public String getRelease_date() {
        return release_date;
    }

    public float getVote_average() {
        return vote_average;
    }

    public String getOverview() {
        return movie_overview;
    }

    public int getRuntime() {
        return runtime;
    }

    @Override
    public String toString() {
        return "MovieModel{" +
                "title='" + title + '\'' +
                ", id=" + movie_id +
                ", overview='" + movie_overview + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeString(backdrop_path);
        dest.writeInt(movie_id);
        dest.writeString(release_date);
        dest.writeFloat(vote_average);
        dest.writeString(movie_overview);
        dest.writeInt(runtime);
    }
}
