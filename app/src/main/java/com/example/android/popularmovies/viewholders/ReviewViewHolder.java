package com.example.android.popularmovies.viewholders;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Review;

public class ReviewViewHolder extends RecyclerView.ViewHolder{

//    private static final String AUTHOR_FORMAT = "by %s";
    private TextView mAuthor;
    private TextView mContent;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        mAuthor = itemView.findViewById(R.id.tv_review_author);
        mContent = itemView.findViewById(R.id.tv_review_content);
    }

    public void bind(Review review) {
        mAuthor.setText(review.author);
//        mAuthor.setText(String.format(AUTHOR_FORMAT, review.author));
        mContent.setText(review.content);
    }
}
