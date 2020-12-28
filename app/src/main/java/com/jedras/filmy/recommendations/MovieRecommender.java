package com.jedras.filmy.recommendations;

import com.jedras.filmy.database.Movie;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;

/** Klasa udostępniająca funkcjonalności wykorzystywane przez wszystkie klasy dziedziczące
 *
 */
public abstract class MovieRecommender {
    TmdbApi tmdbApi;

    MovieRecommender() {
        this.tmdbApi = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc");
    }

    public abstract ArrayList<MovieDb> getRecommendations(List<Movie> favourites);
}
