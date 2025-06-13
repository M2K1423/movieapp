package com.example.movieapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class MovieUrl implements Parcelable {
    @SerializedName("name")
    private String name;
    @SerializedName("slug")
    private String slug;
    @SerializedName("filename")
    private String fileName;
    @SerializedName("link_embed")
    private String link_embed;
    @SerializedName("link_m3u8")
    private String link_m3u8;

    protected MovieUrl(Parcel in) {
        name = in.readString();
        slug = in.readString();
        fileName = in.readString();
        link_embed = in.readString();
        link_m3u8 = in.readString();
    }

    public static final Creator<MovieUrl> CREATOR = new Creator<MovieUrl>() {
        @Override
        public MovieUrl createFromParcel(Parcel in) {
            return new MovieUrl(in);
        }

        @Override
        public MovieUrl[] newArray(int size) {
            return new MovieUrl[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(slug);
        parcel.writeString(fileName);
        parcel.writeString(link_embed);
        parcel.writeString(link_m3u8);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLink_embed() {
        return link_embed;
    }

    public void setLink_embed(String link_embed) {
        this.link_embed = link_embed;
    }

    public String getLink_m3u8() {
        return link_m3u8;
    }

    public void setLink_m3u8(String link_m3u8) {
        this.link_m3u8 = link_m3u8;
    }

    @Override
    public String toString() {
        return "MovieUrl{" +
                "name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", fileName='" + fileName + '\'' +
                ", link_embed='" + link_embed + '\'' +
                ", link_m3u8='" + link_m3u8 + '\'' +
                '}';
    }
}

