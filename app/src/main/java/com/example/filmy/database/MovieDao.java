package com.example.filmy.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies")
    List<Movie> getAll();

    @Query("SELECT * FROM movies WHERE movieID = :movieID")
    List<Movie> getMoviesByID(int movieID);

    @Query("DELETE FROM movies WHERE movieID = :movieID")
    void deleteByID(int movieID);

    @Insert
    void insertAll(Movie... movies);

    @Delete
    void delete(Movie movie);
}
