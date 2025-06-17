package com.example.movieapp.response;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.movieapp.models.MovieModel;
import com.example.movieapp.models.SeoOnPage;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchDataResponse implements Parcelable {
    @SerializedName("seoOnPage")
    private SeoOnPage seoOnPage;
    @SerializedName("items")
    private List<MovieModel> items;

    protected SearchDataResponse(Parcel in) {
        seoOnPage = in.readParcelable(SeoOnPage.class.getClassLoader());
        items = in.createTypedArrayList(MovieModel.CREATOR);
    }

    public static final Creator<SearchDataResponse> CREATOR = new Creator<SearchDataResponse>() {
        @Override
        public SearchDataResponse createFromParcel(Parcel in) {
            return new SearchDataResponse(in);
        }

        @Override
        public SearchDataResponse[] newArray(int size) {
            return new SearchDataResponse[size];
        }
    };

    public SeoOnPage getSeoOnPage() {
        return seoOnPage;
    }

    public void setSeoOnPage(SeoOnPage seoOnPage) {
        this.seoOnPage = seoOnPage;
    }

    public List<MovieModel> getItems() {
        return items;
    }

    public void setItems(List<MovieModel> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable(seoOnPage, i);
        parcel.writeTypedList(items);
    }
}
