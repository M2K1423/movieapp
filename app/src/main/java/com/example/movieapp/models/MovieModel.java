package com.example.movieapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieModel implements Parcelable {
    @SerializedName("name")
    private String title;

    @SerializedName("poster_url")
    private String posterPath;

    @SerializedName("thumb_url")
    private String backdropPath;

    @SerializedName("slug")
    private String slug;

    @SerializedName("content")
    private String content;

    @SerializedName("year")
    private int year;

    @SerializedName("time")
    private String runtimeText;

    @SerializedName("episode_total")
    private int episodeTotal;

    @SerializedName("episode_current")
    private String episodeCurrent;

    @SerializedName("quality")
    private String quality;

    @SerializedName("lang")
    private String language;

    @SerializedName("actor")
    private List<String> actors;

    @SerializedName("director")
    private List<String> directors;

    @SerializedName("tmdb")
    private Rating rating;

    @SerializedName("view")
    private int view;

    protected MovieModel(Parcel in) {
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        slug = in.readString();
        content = in.readString();
        year = in.readInt();
        runtimeText = in.readString();
        episodeTotal = in.readInt();
        episodeCurrent = in.readString();
        quality = in.readString();
        language = in.readString();
        actors = in.createStringArrayList();
        directors = in.createStringArrayList();
        rating = in.readParcelable(Rating.class.getClassLoader());
        view = in.readInt();
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(slug);
        dest.writeString(content);
        dest.writeInt(year);
        dest.writeString(runtimeText);
        dest.writeInt(episodeTotal);
        dest.writeString(episodeCurrent);
        dest.writeString(quality);
        dest.writeString(language);
        dest.writeStringList(actors);
        dest.writeStringList(directors);
        dest.writeParcelable(rating, flags);
        dest.writeInt(view);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and setters
    public String getTitle() {
        Log.e("title", this.title);
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        Log.d("poster", this.posterPath);
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getRuntimeText() {
        return runtimeText;
    }

    public void setRuntimeText(String runtimeText) {
        this.runtimeText = runtimeText;
    }

    public int getEpisodeTotal() {
        return episodeTotal;
    }

    public void setEpisodeTotal(int episodeTotal) {
        this.episodeTotal = episodeTotal;
    }

    public String getEpisodeCurrent() {
        return episodeCurrent;
    }

    public void setEpisodeCurrent(String episodeCurrent) {
        this.episodeCurrent = episodeCurrent;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public Rating getRatings() {
        return rating;
    }

    public void setRatings(Rating rating) {
        this.rating = rating;
    }

    public String getOverview() {
        Log.e("overview", toString());
        return content;
    }

    public void setOverview(String overview) {
        this.content = overview;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    @NonNull
    @Override
    public String toString() {
        return "MovieModel{" +
                "title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", slug='" + slug + '\'' +
                ", year=" + year +
                ", overview='" + content + '\'' +
                ", runtimeText='" + runtimeText + '\'' +
                ", episodeTotal=" + episodeTotal +
                ", episodeCurrent='" + episodeCurrent + '\'' +
                ", quality='" + quality + '\'' +
                ", language='" + language + '\'' +
                ", actors=" + actors +
                ", directors=" + directors +
                ", view=" + view +
                '}';
    }
}