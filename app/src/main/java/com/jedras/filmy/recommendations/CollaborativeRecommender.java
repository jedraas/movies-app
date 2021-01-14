package com.jedras.filmy.recommendations;

import com.jedras.filmy.database.Movie;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

/**
 * Klasa rekomendującą filmy przez themoviedbapi
 */
public class CollaborativeRecommender extends MovieRecommender {
    public static final int NUMBER_OF_RECOMMENDED_MOVIES = 3;
    public static final int NUMBER_OF_SIMILAR_MOVIES = 3;

    /**
     * Dla każdego ulubionego filmu, pobiera 2 filmy rekomendowane oraz 2 filmy podobne.
     *
     * @return
     */
    @Override
    public ArrayList<MovieDb> getRecommendations(List<Movie> favourites) {
        ArrayList<MovieDb> list = new ArrayList<>();

        for (Movie favourite : favourites) {
            int movieID = favourite.movieID;
            TmdbMovies recommendedMovies = tmdbApi.getMovies();
            MovieResultsPage resultRecommended = recommendedMovies.getRecommendedMovies(movieID, null, null);
            TmdbMovies similarMovies = tmdbApi.getMovies();
            MovieResultsPage resultSimilarMovie = similarMovies.getSimilarMovies(movieID, null, null);

            int i = 0;
            for (MovieDb recommendedMovie : resultRecommended) {
                i++;
                if (i >= NUMBER_OF_RECOMMENDED_MOVIES) break;
                list.add(recommendedMovie);
            }

            int j = 0;
            for (MovieDb similarMovie : resultSimilarMovie) {
                j++;
                if (j >= NUMBER_OF_SIMILAR_MOVIES) break;
                list.add(similarMovie);
            }
        }
        return list;
    }
}
