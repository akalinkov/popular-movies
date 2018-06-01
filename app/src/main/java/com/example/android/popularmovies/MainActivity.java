package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utilities.Network;
import com.example.android.popularmovies.utilities.TmdbApi;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MovieRecyclerViewAdapter.PosterClickListener,
        LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String MOVIES_KEY = "movies";
    public static final String SORT_ORDER_KEY = "sort_order";
    public static final int GET_MOVIES_LOADER_ID = 11;
    public static final int POSTER_WIDTH = 500;

    private MovieRecyclerViewAdapter mMovieAdapter;
    private RecyclerView mPostersList;

    private List<Movie> mMovies;
    private String mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPostersList = findViewById(R.id.rv_posters);

        setInstanceState(savedInstanceState);

        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount());
        mPostersList.setLayoutManager(layoutManager);
        mPostersList.setHasFixedSize(true);
        mMovieAdapter = new MovieRecyclerViewAdapter(mMovies, MainActivity.this);
        mPostersList.setAdapter(mMovieAdapter);

        if (null == mMovies || 0 == mMovies.size()) {
            updateMoviesList();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES_KEY, (ArrayList<Movie>) mMovies);
        outState.putString(SORT_ORDER_KEY, mSortOrder);
    }

    private void setInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Log.d(TAG, "setInstanceState: get saved state");
            mMovies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
            mSortOrder = savedInstanceState.getString(SORT_ORDER_KEY);
        } else {
            Log.d(TAG, "setInstanceState: initialize mMovies and mSortOrder, restart loader");
            mMovies = new ArrayList<>();
            mSortOrder = TmdbApi.POPULAR;
        }
    }

    private void updateMoviesList() {
        if (Network.isOnline(this)) {
            getSupportLoaderManager().restartLoader(GET_MOVIES_LOADER_ID, null, this);
        } else {
            Toast.makeText(this, R.string.internet_connection_failed, Toast.LENGTH_LONG).show();
        }
    }

    private int spanCount() {
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        return width / POSTER_WIDTH;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.popular:
                mSortOrder = TmdbApi.POPULAR;
                updateMoviesList();
                return true;
            case R.id.top_rated:
                mSortOrder = TmdbApi.TOP_RATED;
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

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, final Bundle args) {
        return new MoviesLoader(this, mSortOrder);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        Log.d(TAG, "onLoadFinished: data = " + data);
        if (null == data && 0 == data.size()) {
            return;
        }
        mMovies = data;
        mMovieAdapter.replaceMoviesData(mMovies);
        mPostersList.scrollToPosition(0);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
    }


}
