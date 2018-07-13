package com.example.android.popularmovies.httpresponses;

import com.example.android.popularmovies.models.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MoviesResponse {

    @SerializedName("page")
    public int page = 0;
    @SerializedName("totalPages")
    public short totalPages = 0;
    @SerializedName("results")
    public ArrayList<Movie> movies = new ArrayList<>();
}
