package com.example.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class Review implements Parcelable{

    private static final String TAG = Review.class.getSimpleName();

    @SerializedName("author")
    public String author;
    @SerializedName("content")
    public String content;

    private Review(Parcel in) {
        Log.d(TAG, "Review: new object creation");
        author = in.readString();
        content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.d(TAG, "writeToParcel: create new parcel");
        dest.writeString(author);
        dest.writeString(content);
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };


}
