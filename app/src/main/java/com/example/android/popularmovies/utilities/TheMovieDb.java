package com.example.android.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class TheMovieDb {

    private static final String MOVIES_BASE_URL = "https://api.tmdb.org/3/movie/popular";
    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY = "";

    public static String getPopularMovies() {
        URL popularMoviesUrl = buildMoviesUrl();
        try {
            return sendHttpRequest(popularMoviesUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static URL buildMoviesUrl() {
        Uri uri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String sendHttpRequest(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in).useDelimiter("\\A");
            if (scanner.hasNext()) return scanner.next();
            else return null;
        } finally {
            urlConnection.disconnect();
        }
    }
}
