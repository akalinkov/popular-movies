package com.example.android.popularmovies.utilities;

import android.util.Log;

import com.example.android.popularmovies.database.FavoriteEntry;
import com.example.android.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MoviesTransformer {
    private static final String TAG = MoviesTransformer.class.getSimpleName();

    public static List<Movie> convertFavoritesToMovies(List<FavoriteEntry> favorites) {
        Log.d(TAG, "convertFavoritesToMovies: ");
        List<Movie> movies = new ArrayList<>();
        for (FavoriteEntry favorite : favorites) {
            movies.add(convertFavoriteToMovie(favorite));
        }
        return movies;
    }

    public static Movie convertFavoriteToMovie(FavoriteEntry favorite) {
        Log.d(TAG, "convertFavoriteToMovie: " + favorite.getTitle());
        Movie movie = new Movie();
        movie.id = favorite.getMovieId();
        movie.originalTitle = favorite.getTitle();
        movie.overview = favorite.getDescription();
        movie.posterPath = favorite.getPosterPath();
        movie.releaseDate = favorite.getReleaseDate();
        movie.voteAverage = favorite.getRate();
        return movie;
    }
}
