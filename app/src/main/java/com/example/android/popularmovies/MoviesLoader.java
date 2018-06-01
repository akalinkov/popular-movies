package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utilities.Network;
import com.example.android.popularmovies.utilities.TmdbApi;

import java.io.IOException;
import java.util.List;

public class MoviesLoader extends AsyncTaskLoader<List<Movie>> {

    private static final String TAG = MoviesLoader.class.getSimpleName();
    private String mSortOrder;

    public MoviesLoader(@NonNull Context context, String mSortOrder) {
        super(context);
        this.mSortOrder = mSortOrder;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d(TAG, "onStartLoading");
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        try {
            List<Movie> m = TmdbApi.getMovies(mSortOrder);
            return TmdbApi.getMovies(mSortOrder);
        } catch (IOException e) {
            Log.d(TAG, "loadInBackground: falied to get movies for 'TmdbApi' - " + mSortOrder);
            e.printStackTrace();
            return null;
        }
    }
}
