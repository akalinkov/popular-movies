package com.example.android.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utilities.TmdbApi;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieRecyclerViewAdapter
        extends RecyclerView.Adapter<MovieRecyclerViewAdapter.PosterViewHolder> {

    private final String TAG = MovieRecyclerViewAdapter.class.getSimpleName();

    private final PosterClickListener mOnClickListener;
    private List<Movie> movies;

    public interface PosterClickListener {
        void onPosterClick(Movie currentMovie);
    }

    MovieRecyclerViewAdapter(List<Movie> movies, PosterClickListener listener) {
        this.movies = movies;
        this.mOnClickListener = listener;
    }

    @NonNull
    @Override
    public PosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View posterView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poster, parent, false);
        return new PosterViewHolder(posterView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PosterViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: #" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mPoster;

        private PosterViewHolder(View view) {
            super(view);
            mPoster = view.findViewById(R.id.iv_poster);
            view.setOnClickListener(this);
        }

        void bind(int position) {
            Picasso.with(itemView.getContext())
                    .load(TmdbApi.buildThumbnailPath(movies.get(position).posterPath))
                    .into(mPoster);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnClickListener.onPosterClick(movies.get(position));
        }
    }

    public void replaceMoviesData(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}
