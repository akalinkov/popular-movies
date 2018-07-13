package com.example.android.popularmovies.httpresponses;

import com.example.android.popularmovies.models.Trailer;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersResponse {

    @SerializedName("id")
    public int id;
    @SerializedName("results")
    public List<Trailer> trailers;
}
