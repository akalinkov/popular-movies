package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MoviesResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class TmdbApi {

    private static final String TAG = TmdbApi.class.getSimpleName();

    private static final String MOVIES_BASE_URL = "https://api.tmdb.org/3/movie";
    private static final String MOVIE_THUMBNAIL_BASE_URL = "https://image.tmdb.org/t/p/w342";
    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY = "";

    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";

    public static List<Movie> getMovies(String sortOrder) throws IOException {
        URL popularMoviesUrl = buildMoviesUrl(sortOrder);
        String json = Network.sendHttpRequest(popularMoviesUrl);
        return new Gson().fromJson(json, MoviesResponse.class)
                .movies;
    }

    private static URL buildMoviesUrl(String sortOrder) {
        Uri uri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        return buildUrl(uri);
    }


    private static URL buildUrl(Uri uri) {
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.d(TAG, "buildUrl: failed to convert Uri to URL " + uri);
            e.printStackTrace();
        }
        return null;
    }

    public static String buildThumbnailPath(String posterPath) {
        Log.d(TAG, "buildThumbnailPath: posterPath = " + posterPath);
        String path =  Uri.parse(MOVIE_THUMBNAIL_BASE_URL).buildUpon()
                .appendEncodedPath(posterPath)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build()
                .toString();
        Log.d(TAG, "buildThumbnailPath: path = " + path);
        return path;
    }
}
