package com.example.android.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PopularMoviesResponse {

    @SerializedName("id")
    public int id;
    @SerializedName("page")
    public int page;
    @SerializedName("totalPages")
    public short totalPages;
    @SerializedName("results")
    public List<Movie> movies;
}
