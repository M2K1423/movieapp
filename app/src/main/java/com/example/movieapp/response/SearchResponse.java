package com.example.movieapp.response;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.movieapp.models.MovieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse implements Parcelable {
    @SerializedName("data")
    @Expose
    private SearchDataResponse data;

    @SerializedName("status")
    private boolean status;


    protected SearchResponse(Parcel in) {
        data = in.readParcelable(SearchDataResponse.class.getClassLoader());
        status = in.readByte() != 0;
    }

    public static final Creator<SearchResponse> CREATOR = new Creator<SearchResponse>() {
        @Override
        public SearchResponse createFromParcel(Parcel in) {
            return new SearchResponse(in);
        }

        @Override
        public SearchResponse[] newArray(int size) {
            return new SearchResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable(data, i);
        parcel.writeByte((byte) (status ? 1 : 0));
    }

    public SearchDataResponse getData() {
        return data;
    }

    public void setData(SearchDataResponse data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
