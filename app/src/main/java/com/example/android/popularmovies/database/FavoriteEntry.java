package com.example.android.popularmovies.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.example.android.popularmovies.models.Movie;

@Entity(tableName="favorites")
public class FavoriteEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "movie_id")
    private int movieId;
    private String title;
    @ColumnInfo(name = "poster_path")
    private String posterPath;
    private String description;
    private float rate;
    @ColumnInfo(name = "release_date")
    private String releaseDate;

    @Ignore
    public FavoriteEntry(Movie movie) {
        this.movieId = movie.id;
        this.title = movie.originalTitle;
        this.posterPath = movie.posterPath;
        this.description = movie.overview;
        this.rate = movie.voteAverage;
        this.releaseDate = movie.releaseDate;
    }

    public FavoriteEntry(int id, int movieId, String title, String posterPath, String description,
                         float rate, String releaseDate) {
        this.id = id;
        this.movieId = movieId;
        this.title = title;
        this.posterPath = posterPath;
        this.description = description;
        this.rate = rate;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
