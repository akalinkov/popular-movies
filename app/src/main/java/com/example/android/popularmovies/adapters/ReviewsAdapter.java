package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.viewholders.ReviewViewHolder;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewViewHolder>{

    private LayoutInflater mInflater;
    private List<Review> mReviewList;

    public ReviewsAdapter(Context context, @NonNull List<Review> reviewList ) {
        mReviewList = reviewList;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.review_view, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bind(mReviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public void replaceReviewsList(List<Review> reviewsList) {
        mReviewList = reviewsList;
        notifyDataSetChanged();
    }
}
