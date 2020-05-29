package com.example.filmy.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import info.movito.themoviedbapi.model.MovieDb;

/**
 * Encja reprezentująca film, który jest zapisany w tabeli movies.
 */
@Entity (tableName = "movies")
public class Movie {

    /**
     * Identyfikator filmu.
     */
    @PrimaryKey
    public int movieID;

    /**
     * Obiekt reprezentujący szczegóły filmu.
     */
    @ColumnInfo(name = "movieDB")
    public MovieDb movieDB;
}
