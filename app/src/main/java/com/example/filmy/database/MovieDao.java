package com.example.filmy.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Interfejs do bazy danych filmów ulubionych.
 */
@Dao
public interface MovieDao {

    /**
     * Pobierz wszystkie ulubione filmy z bazy.
     * @return lista obiektów typu Movie.
     */
    @Query("SELECT * FROM movies")
    List<Movie> getAll();

    /**
     * Pobierz ulubione filmy z bazy danych po ID.
     * @param movieID identyfikator filmu.
     * @return lista obiektów typu Movie.
     */
    @Query("SELECT * FROM movies WHERE movieID = :movieID")
    List<Movie> getMoviesByID(int movieID);

    /**
     * Usuń ulubione filmy z bazy danych.
     * @param movieID identyfikator filmu.
     */
    @Query("DELETE FROM movies WHERE movieID = :movieID")
    void deleteByID(int movieID);

    /**
     * Dodaj film do ulubionych.
     * @param movies tablica filmów do dodania do bazy.
     */
    @Insert
    void insertAll(Movie... movies);
}
