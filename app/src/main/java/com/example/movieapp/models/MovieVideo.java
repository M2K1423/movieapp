package com.example.movieapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieVideo {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("site")
    @Expose
    private String site;

    @SerializedName("type")
    @Expose
    private String type;

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
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

    public boolean isYoutubeVideo() {
        return "YouTube".equalsIgnoreCase(site);
    }

    public boolean isTrailer() {
        // TMDB API can return "Trailer" or "Teaser" as valid trailer types
        return "Trailer".equalsIgnoreCase(type) || "Teaser".equalsIgnoreCase(type);
    }

    @Override
    public String toString() {
        return "MovieVideo{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", site='" + site + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
} 