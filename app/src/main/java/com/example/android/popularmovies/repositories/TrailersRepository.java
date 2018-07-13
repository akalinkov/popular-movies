package com.example.android.popularmovies.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.android.popularmovies.AppExecutors;
import com.example.android.popularmovies.models.Trailer;
import com.example.android.popularmovies.utilities.TmdbApi;

import java.io.IOException;
import java.util.List;

public class TrailersRepository {

    private static final String TAG = TrailersRepository.class.getSimpleName();

    private static MutableLiveData<List<Trailer>> trailersList = new MutableLiveData<>();

    public static LiveData<List<Trailer>> getTrailersData() {
        return trailersList;
    }

    public static void clearTrailersData() {
        trailersList = new MutableLiveData<>();
     }

    public static void startLoadingTrailers(final int movieId) {
        Log.d(TAG, "startLoadingTrailers: in a background thread for movieId = " + movieId);
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    trailersList.postValue(TmdbApi.getTrailers(movieId));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
