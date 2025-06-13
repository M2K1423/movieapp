package com.example.movieapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Episode implements Parcelable {
    @SerializedName("server_name")
    private String name;
    @SerializedName("server_data")
    private List<MovieUrl> movieUrl;


    protected Episode(Parcel in) {
        name = in.readString();
        movieUrl = in.createTypedArrayList(MovieUrl.CREATOR);
    }

    public static final Creator<Episode> CREATOR = new Creator<Episode>() {
        @Override
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeTypedList(movieUrl);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MovieUrl> getMovieUrl() {
        return movieUrl;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "name='" + name + '\'' +
                ", movieUrl=" + movieUrl +
                '}';
    }
}
