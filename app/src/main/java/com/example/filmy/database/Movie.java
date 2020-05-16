package com.example.filmy.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "movies")
public class Movie {

    @PrimaryKey
    public int movieID;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "poster")
    public String poster;

}
