package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.popularmovies.model.PopularMoviesResponse;
import com.example.android.popularmovies.utilities.TheMovieDb;

public class MainActivity extends AppCompatActivity {

    private TextView mHelloWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelloWorld = findViewById(R.id.hello_world);
        new GetPopularMoviesTask().execute();
    }

    private class GetPopularMoviesTask extends AsyncTask<Void, Void, PopularMoviesResponse> {
        @Override
        protected PopularMoviesResponse doInBackground(Void... voids) {
            return TheMovieDb.getPopularMovies();
        }

        @Override
        protected void onPostExecute(PopularMoviesResponse movies) {
            if (null != movies && !"".equals(movies));
            mHelloWorld.setText(movies.toString());
        }
    }
}
