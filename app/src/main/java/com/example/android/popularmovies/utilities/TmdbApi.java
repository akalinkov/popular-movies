package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.httpresponses.MoviesResponse;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.httpresponses.ReviewsResponse;
import com.example.android.popularmovies.models.Trailer;
import com.example.android.popularmovies.httpresponses.TrailersResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class TmdbApi {

    private static final String TAG = TmdbApi.class.getSimpleName();

    private static final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String MOVIE_THUMBNAIL_BASE_URL = "https://image.tmdb.org/t/p/w342";
    private static final String YOUTUBE_BASE_URL = "http://www.youtube.com";
    private static final String YOUTUBE_THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/";
    private static final String VIDEO_KEY_PARAM = "v";
    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY = "";

    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String TRAILERS = "videos";
    private static final String WATCH = "watch";
    private static final String REVIEWS = "reviews";
    private static final String DEFAULT_JPG_FILENAME = "0.jpg";

    public static List<Movie> getMovies(String sortOrder) throws IOException {
        URL popularMoviesUrl = buildMoviesUrl(sortOrder);
        String json = Network.sendHttpRequest(popularMoviesUrl);
        return new Gson().fromJson(json, MoviesResponse.class)
                .movies;
    }

    public static List<Trailer> getTrailers(int movieId) throws IOException {
        URL trailersUrl = buildTrailersUrl(movieId);
        String json = Network.sendHttpRequest(trailersUrl);
        Log.d(TAG, "getTrailersData: movieId = " + movieId);
        Log.d(TAG, "getTrailersData: json = " + json);
        return new Gson().fromJson(json, TrailersResponse.class)
                .trailers;
    }

    public static List<Review> getReviews(int movieId) throws IOException {
        URL reveiwsUrl = buildReviewsUrl(movieId);
        String json = Network.sendHttpRequest(reveiwsUrl);
        Log.d(TAG, "getReviews: reviews json" + json);
        return new Gson().fromJson(json, ReviewsResponse.class)
                .reviewsList;
    }

    private static URL buildTrailersUrl(int movieId) {
        Uri uri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(Integer.toString(movieId))
                .appendPath(TRAILERS)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        return buildUrl(uri);
    }

    private static URL buildReviewsUrl(int movieId) {
        Uri uri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(Integer.toString(movieId))
                .appendPath(REVIEWS)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        return buildUrl(uri);
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
        return Uri.parse(MOVIE_THUMBNAIL_BASE_URL).buildUpon()
                .appendEncodedPath(posterPath)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build()
                .toString();
    }

    public static String buildTrailerUrl(String key) {
        Log.d(TAG, "buildTrailerUrl: for key = " + key);
        return Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendPath(WATCH)
                .appendQueryParameter(VIDEO_KEY_PARAM, key)
                .toString();
    }

    public static String buildYoutubeThumbnailUrl(String key) {
        Log.d(TAG, "buildYoutubeThumbnailUrl: for key = " + key);
        return Uri.parse(YOUTUBE_THUMBNAIL_BASE_URL).buildUpon()
                .appendPath(key)
                .appendPath(DEFAULT_JPG_FILENAME)
                .build()
                .toString();

    }
}
