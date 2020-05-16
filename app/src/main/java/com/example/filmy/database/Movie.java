package com.example.filmy.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import info.movito.themoviedbapi.model.MovieDb;


@Entity (tableName = "movies")
public class Movie {

    @PrimaryKey
    public int movieID;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "movieDB")
    public MovieDb movieDB;

    @ColumnInfo(name = "poster")
    public String poster;

}
