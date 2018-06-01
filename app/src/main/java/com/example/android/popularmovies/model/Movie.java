package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    private static final String TAG = Movie.class.getSimpleName();

    public static final String CURRENT = "current_moview";

    @SerializedName("original_title")
    public String originalTitle;
    @SerializedName("poster_path")
    public String posterPath;
    @SerializedName("overview")
    public String overview;
    @SerializedName("vote_average")
    public float voteAverage;
    @SerializedName("release_date")
    public String releaseDate;

    private Movie(Parcel parcel) {
        Log.d(TAG, "Movie: new object created");
        originalTitle = parcel.readString();
        posterPath = parcel.readString();
        overview = parcel.readString();
        voteAverage = parcel.readFloat();
        releaseDate = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        Log.d(TAG, "writeToParcel: create new parcel");
        parcel.writeString(originalTitle);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeFloat(voteAverage);
        parcel.writeString(releaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
