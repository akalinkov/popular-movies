package com.example.android.popularmovies.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Trailer;
import com.example.android.popularmovies.utilities.TmdbApi;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailersAdapter
        extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private static final String TAG = TrailersAdapter.class.getSimpleName();
    private final TrailerClickListener mClickListener;
    private List<Trailer> trailers;

    public TrailersAdapter(List<Trailer> trailers, TrailerClickListener clickListener) {
        this.trailers = trailers;
        this.mClickListener = clickListener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View trailerView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_view, parent, false);
        return new TrailerViewHolder(trailerView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mTrailerThumbnail;
        TextView mTrailerTitle;

        private TrailerViewHolder(View view) {
            super(view);
            mTrailerThumbnail = view.findViewById(R.id.iv_trailer_thumbnail);
            mTrailerTitle = view.findViewById(R.id.tv_trailer_title);
            view.setOnClickListener(this);
        }

        void bind(int position) {
            Picasso.with(itemView.getContext())
                    .load(TmdbApi.buildYoutubeThumbnailUrl(trailers.get(position).key))
                    .into(mTrailerThumbnail);
            mTrailerThumbnail.setContentDescription(trailers.get(position).name);
            mTrailerTitle.setText(trailers.get(position).name);
        }

        @Override
        public void onClick(View v) {
            mClickListener.onTrailerClick(trailers.get(getAdapterPosition()));
        }
    }

    public void replaceTrailersList(List<Trailer> trailers) {
        Log.d(TAG, "replaceTrailersList: ");
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    public interface TrailerClickListener {
        void onTrailerClick(Trailer trailer);
    }
}
