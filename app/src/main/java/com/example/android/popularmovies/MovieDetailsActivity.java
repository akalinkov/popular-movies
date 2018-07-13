package com.example.android.popularmovies;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.adapters.ReviewsAdapter;
import com.example.android.popularmovies.adapters.TrailersAdapter;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.FavoriteEntry;
import com.example.android.popularmovies.databinding.ActivityMovieDetailsBinding;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;
import com.example.android.popularmovies.repositories.ReviewsRepository;
import com.example.android.popularmovies.repositories.TrailersRepository;
import com.example.android.popularmovies.utilities.Formatting;
import com.example.android.popularmovies.utilities.Network;
import com.example.android.popularmovies.utilities.TmdbApi;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity
        extends AppCompatActivity implements TrailersAdapter.TrailerClickListener {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private static final String REVIEWS_HEADER_FORMAT = "Reviews (%d)";
    private static final String MOVIE = "movie";
    private static final String TRAILERS = "trailers";
    private static final String REVIEWS = "reviews";
    private static final String IS_FAVORITE = "is_favorite";


    ActivityMovieDetailsBinding mBinding;
    RecyclerView mReviewsRecyclerView;
    RecyclerView mTrailersListRecyclerView;
    private TrailersAdapter mTrailerAdapter;
    private ReviewsAdapter mReviewsAdapter;
    private ExpandableLayout mReviewsGroup;
    private ImageView mHeaderArrow;
    private TextView mReviewsHeader;
    private ImageView mFavoriteIcon;

    private Movie movie;
    private List<Trailer> trailersList = new ArrayList<>();
    private List<Review> reviewsList = new ArrayList<>();
    private boolean isFavorite = false;

    private AppDatabase mDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        mDb = AppDatabase.getsInstance(getApplicationContext());

        setupViews();

        if (savedInstanceState == null) {
            movie = extractMovie(getIntent());
        } else {
            restoreInstanceState(savedInstanceState);
        }
        bindDetailsData(movie);
        updateFavoriteStatusFromDb();

        mReviewsAdapter = new ReviewsAdapter(this, reviewsList);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);

        mTrailerAdapter = new TrailersAdapter(trailersList, this);
        mTrailersListRecyclerView.setAdapter(mTrailerAdapter);

        if (Network.isOnline(this)) {
            loadTrailers();
            loadReviews();
        } else {
            Network.noInternetConnectionToast(this);
        }
    }

    private void setupViews() {
        mHeaderArrow = findViewById(R.id.iv_header_arrow);
        mFavoriteIcon = findViewById(R.id.iv_favorite_icon);
        mFavoriteIcon.setOnClickListener(mFavoriteIconClickListener);
        mReviewsHeader = findViewById(R.id.tv_reviews_header);
        mReviewsHeader.setOnClickListener(mReviewsHeaderClickListener);
        mReviewsGroup = findViewById(R.id.expandable_reviews_layout);
        mReviewsGroup.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
            }
        });
        mReviewsRecyclerView = findViewById(R.id.rv_reviews);
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTrailersListRecyclerView = findViewById(R.id.rv_trailers);
        mTrailersListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE, movie);
        outState.putParcelableArrayList(TRAILERS, (ArrayList<Trailer>) trailersList);
        outState.putParcelableArrayList(REVIEWS, (ArrayList<Review>) reviewsList);
        outState.putBoolean(IS_FAVORITE, isFavorite);
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        movie = savedInstanceState.getParcelable(MOVIE);
        trailersList = savedInstanceState.getParcelableArrayList(TRAILERS);
        reviewsList = savedInstanceState.getParcelableArrayList(REVIEWS);
        isFavorite = savedInstanceState.getBoolean(IS_FAVORITE);
    }

    private Movie extractMovie(Intent intent) {
        Log.d(TAG, "extractMovie: receive Movie details from parent activity");
        if (intent.hasExtra(Movie.CURRENT)) {
            return intent.getParcelableExtra(Movie.CURRENT);
        }
        return new Movie();
    }

    private void loadReviews() {
        ReviewsRepository.clearReviewsData();
        ReviewsRepository.getReviewsData().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(@Nullable List<Review> reviews) {
                Log.d(TAG, "onChanged: reviews observer notified about data change " + reviews);
                reviewsList = reviews;
                bindHeaderText();
                mReviewsAdapter.replaceReviewsList(reviewsList);
            }
        });
        Log.d(TAG, "loadReviews: ");
        ReviewsRepository.startLoadingReviews(movie.id);
    }

    private void loadTrailers() {
        TrailersRepository.clearTrailersData();
        TrailersRepository.getTrailersData().observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(@Nullable List<Trailer> trailers) {
                TrailersRepository.getTrailersData().removeObservers(MovieDetailsActivity.this);
                Log.d(TAG, "onChanged: trailers observer notified about data change " + trailers);
                trailersList = trailers;
                mTrailerAdapter.replaceTrailersList(trailers);
            }
        });
        Log.d(TAG, "loadTrailers: ");
        TrailersRepository.startLoadingTrailers(movie.id);
    }

    private void bindDetailsData(Movie movie) {
        Log.d(TAG, "bindDetailsData: ");
        mBinding.tvTitle.setText(movie.originalTitle);
        mBinding.tvReleaseDate.setText(Formatting.formatReleaseDate(this, movie.releaseDate));
        mBinding.tvRating.setText(Formatting.formatRating(this, movie.voteAverage));
        mBinding.tvDescription.setText(movie.overview);
        mBinding.ivThumbnail.setContentDescription(movie.originalTitle + " poster_view");
        Picasso.with(this)
                .load(TmdbApi.buildThumbnailPath(movie.posterPath))
                .into(mBinding.ivThumbnail);
        bindHeaderText();
    }

    private void bindHeaderText() {
        Log.d(TAG, "bindHeaderText: Reviews");
        mReviewsHeader.setText(String.format(REVIEWS_HEADER_FORMAT, reviewsList.size()));
    }

    private void updateFavoriteIconImage() {
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                if (isFavorite) {
                    Log.d(TAG, "updateFavoriteIconImage: enabled");
                    mFavoriteIcon.setImageResource(R.drawable.ic_favorite_enabled_40dp);
                } else {
                    Log.d(TAG, "updateFavoriteIconImage: disabled");
                    mFavoriteIcon.setImageResource(R.drawable.ic_favorite_disabled_40dp);
                }
            }
        });
    }

    private void updateFavoriteStatusFromDb() {
        AppExecutors.getInstance().diskIo().execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "updateFavoriteStatusFromDb: Check if movie is favorite in database");
                isFavorite = mDb.favoriteDao().findMovieInFavorites(movie.id).size() > 0;
                updateFavoriteIconImage();
            }
        });
    }

    private View.OnClickListener mReviewsHeaderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(@NonNull View view) {
            if (reviewsList.size() == 0) {
                return;
            }
            if (mReviewsGroup.isExpanded()) {
                Log.d(TAG, "onClick: collapse reviews");
                mReviewsGroup.collapse();
                mHeaderArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
            } else {
                mReviewsGroup.expand();
                Log.d(TAG, "onClick: expand reviews");
                mHeaderArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
        }
    };

    private View.OnClickListener mFavoriteIconClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isFavorite = !isFavorite;
            updateFavoriteIconImage();
            if (isFavorite) {
                AppExecutors.getInstance().diskIo().execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "onClick: " + movie.originalTitle + " - save to favorites");
                        mDb.favoriteDao().insertMovie(new FavoriteEntry(movie));
                    }
                });
            } else {
                AppExecutors.getInstance().diskIo().execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "onClick: " + movie.originalTitle + " - remove from favorites");
                        mDb.favoriteDao().deleteMovie(movie.id);
                    }
                });
            }
        }
    };

    @Override
    public void onTrailerClick(Trailer trailer) {
        Log.d(TAG, "onTrailerClick: trailer" + trailer.name);
        Intent watchTrailerIntent = new Intent()
                .setAction(Intent.ACTION_VIEW)
                .setData(Uri.parse(TmdbApi.buildTrailerUrl(trailer.key)));
        if (null != watchTrailerIntent.resolveActivity(getPackageManager())) {
            startActivity(watchTrailerIntent);
        } else {
            Toast.makeText(this, R.string.cannot_resolve_intent, Toast.LENGTH_LONG).show();
        }
    }
}
