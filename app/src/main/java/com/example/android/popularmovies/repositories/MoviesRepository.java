package com.example.android.popularmovies.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.example.android.popularmovies.AppExecutors;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.FavoriteEntry;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utilities.MoviesTransformer;
import com.example.android.popularmovies.utilities.Network;
import com.example.android.popularmovies.utilities.TmdbApi;

import java.io.IOException;
import java.util.List;

public class MoviesRepository {
    private static final String TAG = MoviesRepository.class.getSimpleName();
    private static MutableLiveData<List<Movie>> moviesList = new MutableLiveData<>();

    private static void loadFavoritesFromDb(Context context) {
        Log.d(TAG, "loadFavoritesFromDb: ");
        final List<FavoriteEntry> favoriteEntries = AppDatabase
                .getsInstance(context.getApplicationContext())
                .favoriteDao()
                .loadAllFavorites();
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                moviesList.setValue(MoviesTransformer.convertFavoritesToMovies(favoriteEntries));
            }
        });
    }

    private static void loadMoviesUsingHttpRequest(Context context, String displayType) {
        Log.d(TAG, "loadMoviesUsingHttpRequest: ");
        if (!Network.isOnline(context)) {
            Network.noInternetConnectionToast(context);
            return;
        }
        try {
            final List<Movie> movies = TmdbApi.getMovies(displayType);
            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    moviesList.setValue(movies);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LiveData<List<Movie>> getMoviesData() {
        return moviesList;
    }

    public static void clearMoviesData() { moviesList = new MutableLiveData<>(); }

    public static void startLoadingMovies(final Context context, final String displayType) {
        Log.d(TAG, "startLoadingMovies: ");
        if (displayType.equals(Movie.FAVORITE)) {
            AppExecutors.getInstance().diskIo().execute(new Runnable() {
                @Override
                public void run() {
                    loadFavoritesFromDb(context);
                }
            });
        } else {
            AppExecutors.getInstance().networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    loadMoviesUsingHttpRequest(context, displayType);
                }
            });
        }
    }
}
