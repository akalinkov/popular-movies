package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Network {

    private static final String TAG = Network.class.getSimpleName();

    public static String sendHttpRequest(URL url) throws IOException {
        Log.d(TAG, "sendHttpRequest: open http connection");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in).useDelimiter("\\A");
            if (scanner.hasNext()) return scanner.next();
            else throw new IOException("Empty response from http request");
        } finally {
            Log.d(TAG, "sendHttpRequest: http connection closed");
            urlConnection.disconnect();
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
}
