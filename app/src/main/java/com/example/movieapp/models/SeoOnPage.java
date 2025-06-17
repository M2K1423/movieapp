package com.example.movieapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SeoOnPage implements Parcelable {
    @SerializedName("og_type")
    private String og_type;
    @SerializedName("titleHead")
    private String titleHead;
    @SerializedName("descriptionHead")
    private String descriptionHead;
    @SerializedName("og_image")
    private List<String> og_image;

    protected SeoOnPage(Parcel in) {
        og_type = in.readString();
        titleHead = in.readString();
        descriptionHead = in.readString();
        og_image = in.createStringArrayList();
    }

    public static final Creator<SeoOnPage> CREATOR = new Creator<SeoOnPage>() {
        @Override
        public SeoOnPage createFromParcel(Parcel in) {
            return new SeoOnPage(in);
        }

        @Override
        public SeoOnPage[] newArray(int size) {
            return new SeoOnPage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(og_type);
        parcel.writeString(titleHead);
        parcel.writeString(descriptionHead);
        parcel.writeStringList(og_image);
    }
}
