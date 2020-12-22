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

public class LanguageRecommender extends MovieRecommender {

    public static final int NUMBER_OF_MOST_POPULAR_LANGUAGE = 3;
    public static final int MIN_LANGUAGE_POPULARITY = 2;
    public static final int NUMBER_OF_RECOMMENDED_MOVIES = 3;

    // function to sort hashmap by values
    public HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer>> list =
                new LinkedList<>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> cast1,
                               Map.Entry<String, Integer> cast2) {
                return (cast2.getValue()).compareTo(cast1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    private ArrayList<String> getPopularLanguage(List<Movie> favourites) {
        HashMap<String, Integer> languageByCount = new HashMap<>();
        for (Movie movie : favourites) {
            try {
                String language = movie.movieDB.getOriginalLanguage();
                Integer count = languageByCount.get(language);
                if (count == null) count = 0;
                count++;
                languageByCount.put(language, count);

            } catch (NullPointerException e) {
            }
        }

        HashMap<String, Integer> sortedLanguageByCount = sortByValue(languageByCount);

        // Dodaj 4 najpopularniejszych gatunki do listy
        ArrayList<String> popularLanguage = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Integer> perLanguage : sortedLanguageByCount.entrySet()) {
            i++;
            if (i > NUMBER_OF_MOST_POPULAR_LANGUAGE - 1) break;
            if (perLanguage.getValue() >= MIN_LANGUAGE_POPULARITY) {
                popularLanguage.add(perLanguage.getKey());
            }
        }
        return popularLanguage;
    }


    @Override
    public ArrayList<MovieDb> getRecommendations(List<Movie> favourites) {
        ArrayList<String> popularLanguage = getPopularLanguage(favourites);
        ArrayList<MovieDb> list = new ArrayList<>();
        TmdbDiscoverAdditions discoverer = new TmdbDiscoverAdditions(tmdbApi);

        for (String language : popularLanguage) {
            MovieResultsPage discoverWithLanguage = discoverer.getDiscoverWithOriginalLanguage(language);

            // Tworzę zbiór z ulubionymi, by w czasie O(1) sprawdzać czy film rekomendowany już jest w ulubionych
            Set<MovieDb> favouritesSet = new HashSet<>();
            for (Movie favourite : favourites) {
                favouritesSet.add(favourite.movieDB);
            }

            int j = 0;
            for (MovieDb popularMovie : discoverWithLanguage.getResults()) {
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

