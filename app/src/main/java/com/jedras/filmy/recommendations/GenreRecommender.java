package com.jedras.filmy.recommendations;

import com.jedras.filmy.database.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbDiscover;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

/**
 * Klasa rekomendującą filmy dla najpopularniejszych gatunków
 */
public class GenreRecommender extends MovieRecommender {
    public static final int NUMBER_OF_MOST_POPULAR_GENRE = 3;
    public static final int MIN_GENRE_POPULARITY = 2;
    public static final int NUMBER_OF_RECOMMENDED_MOVIES = 3;

    // function to sort hashmap by values
    public HashMap<Genre, Integer> sortByValue(HashMap<Genre, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<Genre, Integer>> list =
                new LinkedList<>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Genre, Integer>>() {
            public int compare(Map.Entry<Genre, Integer> genre1,
                               Map.Entry<Genre, Integer> genre2) {
                return (genre2.getValue()).compareTo(genre1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Genre, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<Genre, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    private ArrayList<Genre> getPopularGenre(List<Movie> favourites) {
        HashMap<Genre, Integer> genreByCount = new HashMap<>();
        for (Movie movie : favourites) {
            TmdbMovies detail = tmdbApi.getMovies();
            MovieDb resultDetail = detail.getMovie(movie.movieID, null);
            try {
                for (Genre genre : resultDetail.getGenres()) {
                    Integer count = genreByCount.get(genre);
                    if (count == null) count = 0;
                    count++;
                    genreByCount.put(genre, count);
                }
            } catch (NullPointerException e) {
            }
        }

        HashMap<Genre, Integer> sortedGenreByCount = sortByValue(genreByCount);

        // Dodaj 2 najpopularniejsze gatunki do listy
        ArrayList<Genre> popularGenre = new ArrayList<>();
        int i = 0;
        for (Map.Entry<Genre, Integer> perGenre : sortedGenreByCount.entrySet()) {
            i++;
            if (i > NUMBER_OF_MOST_POPULAR_GENRE - 1) break;
            if (perGenre.getValue() >= MIN_GENRE_POPULARITY) {
                popularGenre.add(perGenre.getKey());
            }
        }
        return popularGenre;
    }

    @Override
    public ArrayList<MovieDb> getRecommendations(List<Movie> favourites) {
        ArrayList<Genre> popularGenre = getPopularGenre(favourites);
        ArrayList<MovieDb> list = new ArrayList<>();
        TmdbDiscover discoverGenre = tmdbApi.getDiscover();

        for (Genre genre : popularGenre) {
            String genreStr = String.valueOf(genre.getId());
            Discover discover = new Discover().withGenres(genreStr).sortBy("popularity.desc");
            MovieResultsPage discoverWithGenre = discoverGenre.getDiscover(discover);

            // Tworzę zbiór z ulubionymi, by w czasie O(1) sprawdzać czy film rekomendowany już jest w ulubionych
            Set<MovieDb> favouritesSet = new HashSet<>();
            for (Movie favourite : favourites) {
                favouritesSet.add(favourite.movieDB);
            }

            int j = 0;
            for (MovieDb popularMovie : discoverWithGenre.getResults()) {
                if (favouritesSet.contains(popularMovie)) {
                    // nie dodawaj filmów, które już są ulubione
                    continue;
                }

                j++;
                if (j > NUMBER_OF_RECOMMENDED_MOVIES) {
                    break;
                }
                list.add(popularMovie);
            }
        }
        return list;
    }
}
