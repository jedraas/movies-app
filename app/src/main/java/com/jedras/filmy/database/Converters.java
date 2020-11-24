package com.jedras.filmy.database;

import android.util.Base64;

import androidx.room.TypeConverter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import info.movito.themoviedbapi.model.MovieDb;

/**
 * Klasa serializująca obiekt typu MovieDb z typu String i do typu String.
 */
public class Converters {

    /**
     * Zdeserializuj obiekt value do typu MovieDb.
     * @param value obiekt, który zostaje zdeserializowany.
     * @return zdeserializowany obiekt.
     */
    @TypeConverter
    public static MovieDb fromString(String value) {

        MovieDb movieDb = null;
        try {
            byte b[] = Base64.decode(value.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            movieDb = (MovieDb)si.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return movieDb;
    }

    /**
     * Zserializuj obiekt movieDb do typu String.
     * @param movieDb obiekt, który zostaje zserializowany.
     * @return zserializowany obiekt.
     */
    @TypeConverter
    public static String fromMovieDb(MovieDb movieDb) {

        String value = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(movieDb);
            so.flush();
            value = new String(Base64.encode(bo.toByteArray(), Base64.DEFAULT));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}

