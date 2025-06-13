package com.example.movieapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieVideo implements Parcelable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("slug")
    @Expose
    private String slug;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("lang")
    @Expose
    private String site;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("trailer_url")
    @Expose
    private String trailerUrl;
    @SerializedName("year")
    private int year;


    protected MovieVideo(Parcel in) {
        id = in.readString();
        slug = in.readString();
        name = in.readString();
        site = in.readString();
        type = in.readString();
        trailerUrl = in.readString();
        content = in.readString();
        year = in.readInt();
    }

    public static final Creator<MovieVideo> CREATOR = new Creator<MovieVideo>() {
        @Override
        public MovieVideo createFromParcel(Parcel in) {
            return new MovieVideo(in);
        }

        @Override
        public MovieVideo[] newArray(int size) {
            return new MovieVideo[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public boolean isYoutubeVideo() {
        return "YouTube".equalsIgnoreCase(site);
    }

    public boolean isTrailer() {
        // TMDB API can return "Trailer" or "Teaser" as valid trailer types
        return "Trailer".equalsIgnoreCase(type) || "Teaser".equalsIgnoreCase(type);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }



    @Override
    public String toString() {
        return "MovieVideo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", content='" + content + '\'' +
                ", site='" + site + '\'' +
                ", type='" + type + '\'' +
                ", trailerUrl='" + trailerUrl + '\'' +
                ", year=" + year +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(slug);
        parcel.writeString(name);
        parcel.writeString(site);
        parcel.writeString(type);
        parcel.writeString(trailerUrl);
        parcel.writeString(content);
        parcel.writeInt(year);
    }

}