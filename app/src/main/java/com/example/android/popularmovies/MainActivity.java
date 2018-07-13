package com.example.android.popularmovies;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popularmovies.adapters.MovieAdapter;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.repositories.MoviesRepository;
import com.example.android.popularmovies.utilities.TmdbApi;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.PosterClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    protected static final String FAVORITE = "favorite";
    private static final String MOVIES_KEY = "movies";
    public static final String DISPLAY_TYPE_KEY = "movies_display_type";
    public static final int POSTER_WIDTH = 500;

    private AppDatabase mDb;
    private MovieAdapter mMovieAdapter;
    private RecyclerView mPostersList;

    private List<Movie> mMovies = new ArrayList<>();
    private String mDisplayType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPostersList = findViewById(R.id.rv_posters);

        setInstanceState(savedInstanceState);

        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount());
        mPostersList.setLayoutManager(layoutManager);
        mPostersList.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(mMovies, MainActivity.this);
        mPostersList.setAdapter(mMovieAdapter);

        mDb = AppDatabase.getsInstance(this.getApplicationContext());
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
        updateMoviesList();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES_KEY, (ArrayList<Movie>) mMovies);
        outState.putString(DISPLAY_TYPE_KEY, mDisplayType);
    }

    private void setInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Log.d(TAG, "setInstanceState: get saved state");
            mMovies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
            mDisplayType = savedInstanceState.getString(DISPLAY_TYPE_KEY);
        } else {
            Log.d(TAG, "setInstanceState: initialize mDisplayType");
            mDisplayType = TmdbApi.POPULAR;
        }
    }

    private void updateMoviesList() {
        Log.d(TAG, "updateMoviesList: Actively retrieving movies");
        MoviesRepository.clearMoviesData();
        MoviesRepository.getMoviesData().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "onChanged: update movies list");
                mMovies = movies;
                mMovieAdapter.replaceMoviesData(mMovies);
            }
        });
        MoviesRepository.startLoadingMovies(this, mDisplayType);
    }


    private int spanCount() {
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        return width / POSTER_WIDTH;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_display_type, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.popular:
                mDisplayType = TmdbApi.POPULAR;
                updateMoviesList();
                return true;
            case R.id.top_rated:
                mDisplayType = TmdbApi.TOP_RATED;
                updateMoviesList();
                return true;
            case R.id.favorite:
                mDisplayType = FAVORITE;
                updateMoviesList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPosterClick(Movie movie) {
        Log.d(TAG, "onClick: clicked " + movie.originalTitle);
        Intent MovieDetails = new Intent(this, MovieDetailsActivity.class);
        MovieDetails.putExtra(Movie.CURRENT, movie);
        startActivity(MovieDetails);
    }
}
