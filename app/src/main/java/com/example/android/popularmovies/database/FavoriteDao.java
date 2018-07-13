package com.example.android.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorites")
    List<FavoriteEntry> loadAllFavorites();

    @Insert
    void insertMovie(FavoriteEntry favoriteEntry);

    @Query("DELETE FROM favorites WHERE movie_id = :movieId")
    void deleteMovie(int movieId);

    @Query("SELECT id FROM favorites where movie_id = :movieId")
    List<Integer> findMovieInFavorites(int movieId);
}
