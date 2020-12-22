package com.jedras.filmy.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Singleton bazy danych Movie wykorzystująca Room.
 */
@Database(entities = {Movie.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class MovieDatabase extends RoomDatabase {

    /**
     * Statyczna instancja bazy danych.
     */
    private static MovieDatabase INSTANCE;

    /**
     * Zwraca interfejs do bazy danych.
     *
     * @return interfejs do bazy danych.
     */
    public abstract MovieDao movieDao();

    /**
     * Statyczna metoda zwracająca Singleton instancji bazy danych lub tworząca go, gdy jeszcze nie istnieje.
     *
     * @param context kontekst aplikacji.
     * @return Singleton instancji bazy danych.
     */
    public static MovieDatabase getMovieDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), MovieDatabase.class, "movies").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
