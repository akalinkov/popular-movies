package com.example.android.popularmovies.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.android.popularmovies.AppExecutors;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.utilities.TmdbApi;

import java.io.IOException;
import java.util.List;

public class ReviewsRepository {
    private static final String TAG = ReviewsRepository.class.getSimpleName();

    private static MutableLiveData<List<Review>> reviewsList = new MutableLiveData<>();

    public static LiveData<List<Review>> getReviewsData() {
        return reviewsList;
    }

    public static void clearReviewsData() { reviewsList = new MutableLiveData<>(); }

    public static void startLoadingReviews(final int movieId) {
        Log.d(TAG, "startLoadingReviews: in a background thread for movieId = " + movieId);
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    reviewsList.postValue(TmdbApi.getReviews(movieId));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
