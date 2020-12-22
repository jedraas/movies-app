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

import info.movito.themoviedbapi.TmdbDiscoverAdditions;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.PersonCast;

public class PopularCastRecommender extends MovieRecommender {
    public static final int NUMBER_OF_MOST_POPULAR_CAST = 3;
    public static final int MIN_CAST_POPULARITY = 2;
    public static final int NUMBER_OF_RECOMMENDED_MOVIES = 3;

    // function to sort hashmap by values
    public HashMap<PersonCast, Integer> sortByValue(HashMap<PersonCast, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<PersonCast, Integer>> list =
                new LinkedList<>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<PersonCast, Integer>>() {
            public int compare(Map.Entry<PersonCast, Integer> cast1,
                               Map.Entry<PersonCast, Integer> cast2) {
                return (cast2.getValue()).compareTo(cast1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<PersonCast, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<PersonCast, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }


    private ArrayList<PersonCast> getPopularCast(List<Movie> favourites) {
        // Znajdź aktorów i ich popularność w ulubionych
        HashMap<PersonCast, Integer> castByNumber = new HashMap<>();
        for (Movie favourite : favourites) {
            try {
                for (PersonCast personCast : favourite.movieDB.getCast()) {
                    Integer cast = castByNumber.get(personCast);
                    if (cast == null) cast = 0;
                    cast++;
                    castByNumber.put(personCast, cast);
                }
            } catch (NullPointerException e) {
            }
        }
        HashMap<PersonCast, Integer> sortedCastByNumber = sortByValue(castByNumber);

        // Dodaj 4 najpopularniejszych aktorów do listy
        ArrayList<PersonCast> popularCast = new ArrayList<>();
        int i = 0;
        for (Map.Entry<PersonCast, Integer> perCast : sortedCastByNumber.entrySet()) {
            i++;
            if (i > NUMBER_OF_MOST_POPULAR_CAST - 1) break;
            if (perCast.getValue() >= MIN_CAST_POPULARITY) {
                popularCast.add(perCast.getKey());
            }
        }

        return popularCast;
    }

    @Override
    public ArrayList<MovieDb> getRecommendations(List<Movie> favourites) {

        ArrayList<PersonCast> popularCast = getPopularCast(favourites);
        ArrayList<MovieDb> list = new ArrayList<>();
        TmdbDiscoverAdditions discoverPeople = new TmdbDiscoverAdditions(tmdbApi);

        for (PersonCast cast : popularCast) {
            String personId = String.valueOf(cast.getId());
            MovieResultsPage discoverWithPeople = discoverPeople.getDiscoverWithPeople(personId);

            Set<MovieDb> favouritesSet = new HashSet<MovieDb>();
            for (Movie favourite : favourites) {
                favouritesSet.add(favourite.movieDB);
            }

            int j = 0;
            for (MovieDb popularMovie : discoverWithPeople.getResults()) {
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

            // TODO: Discover, nie wszedzie discover zwraca aktorow. Czy to jest problem jak aktor straje sie rezyserem?
        }

        return list;
    }
}
