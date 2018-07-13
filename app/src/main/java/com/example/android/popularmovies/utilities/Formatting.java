package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.util.Log;

import com.example.android.popularmovies.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Formatting {
    private static final String TAG = Formatting.class.getSimpleName();

    public static String formatReleaseDate(Context context, String releaseDate) {
        try {
            Date date = new SimpleDateFormat(context.getString(R.string.date_input_format), Locale.US)
                    .parse(releaseDate);
            return new SimpleDateFormat(context.getString(R.string.release_date_format), Locale.US)
                    .format(date);
        } catch (ParseException e) {
            Log.d(TAG, "formatReleaseDate: failed to parse release date - " + releaseDate);
            e.printStackTrace();
        }
        return releaseDate;
    }

    public static String formatRating(Context context, Float rating) {
        return String.format(context.getString(R.string.rating_format), rating);
    }
}
