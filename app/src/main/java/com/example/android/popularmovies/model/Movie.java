package com.example.android.popularmovies.model;

import com.google.gson.annotations.SerializedName;

public class Movie {

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
}
