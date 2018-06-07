package com.example.android.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.databinding.ActivityMovieDetailsBinding;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utilities.TmdbApi;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    ActivityMovieDetailsBinding mBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Log.d(TAG, "onCreate: receive Movie details");
        Intent intent = getIntent();
        if (intent.hasExtra(Movie.CURRENT)) {
            Movie movie = intent.getParcelableExtra(Movie.CURRENT);
            bindData(movie);
        }
    }

    private void bindData(Movie movie){
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        mBinding.tvTitle.setText(movie.originalTitle);
        mBinding.tvReleaseDate.setText(formatReleaseDate(movie.releaseDate));
        mBinding.tvRating.setText(formatRating(movie.voteAverage));
        mBinding.tvDescription.setText(movie.overview);
        mBinding.ivThumbnail.setContentDescription(movie.originalTitle + " poster");
        Picasso.with(this)
                .load(TmdbApi.buildThumbnailPath(movie.posterPath))
                .into(mBinding.ivThumbnail);
    }

    private String formatReleaseDate(String releaseDate) {
        try {
            Date date = new SimpleDateFormat(getString(R.string.date_input_format), Locale.US)
                    .parse(releaseDate);
            return new SimpleDateFormat(getString(R.string.release_date_format), Locale.US)
                    .format(date);
        } catch (ParseException e) {
            Log.d(TAG, "formatReleaseDate: filed to parse release date - " + releaseDate);
            e.printStackTrace();
        }
            return releaseDate;
    }

    private String formatRating(Float rating) {
        return String.format(getString(R.string.rating_format), rating);
    }
}
