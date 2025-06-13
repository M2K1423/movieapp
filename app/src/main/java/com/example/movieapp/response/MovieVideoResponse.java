package com.example.movieapp.response;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.movieapp.models.Episode;
import com.example.movieapp.models.MovieVideo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieVideoResponse implements Parcelable {
    @SerializedName("movie")
    @Expose
    private MovieVideo video;


    @SerializedName("episodes")
    private List<Episode> episodes;

    protected MovieVideoResponse(Parcel in) {
        video = in.readParcelable(MovieVideo.class.getClassLoader());
        episodes = in.createTypedArrayList(Episode.CREATOR);
    }

    public static final Creator<MovieVideoResponse> CREATOR = new Creator<MovieVideoResponse>() {
        @Override
        public MovieVideoResponse createFromParcel(Parcel in) {
            return new MovieVideoResponse(in);
        }

        @Override
        public MovieVideoResponse[] newArray(int size) {
            return new MovieVideoResponse[size];
        }
    };

    public MovieVideo getVideo() {
        return video;
    }

    public void setVideo(MovieVideo video) {
        this.video = video;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable(video, i);
        parcel.writeTypedList(episodes);
    }
}