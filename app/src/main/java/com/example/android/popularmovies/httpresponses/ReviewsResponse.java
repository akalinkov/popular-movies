package com.example.android.popularmovies.httpresponses;

import com.example.android.popularmovies.models.Review;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResponse {

    @SerializedName("page")
    public short page;
    @SerializedName("results")
    public List<Review> reviewsList;
}
