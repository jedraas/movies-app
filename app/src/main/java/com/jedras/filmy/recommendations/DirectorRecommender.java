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

import info.movito.themoviedbapi.TmdbDiscoverPeople;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.PersonCrew;
import info.movito.themoviedbapi.model.people.PersonCrew;

public class DirectorRecommender extends MovieRecommender {
        // pobieramy rezysera filmu z get movie credits
        // discover z tym id
        // 1 usuwamy duplikaty
        // 2 sortujemy
        // 3 pierwsze 2

    public static final int NUMBER_OF_MOST_POPULAR_DIRECTORS = 3;
    public static final int MIN_DIRECTOR_POPULARITY = 2;
    public static final int NUMBER_OF_RECOMMENDED_MOVIES = 3;

    // function to sort hashmap by values
    public HashMap<PersonCrew, Integer> sortByValue(HashMap<PersonCrew, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<PersonCrew, Integer> > list =
                new LinkedList<>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<PersonCrew, Integer> >() {
            public int compare(Map.Entry<PersonCrew, Integer> cast1,
                               Map.Entry<PersonCrew, Integer> cast2)
            {
                return (cast2.getValue()).compareTo(cast1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<PersonCrew, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<PersonCrew, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }


    private ArrayList<PersonCrew> getPopularDirectors(List<Movie> favourites) {
        // Znajdź rezyserow i ich popularność w ulubionych
        HashMap<PersonCrew, Integer> directorsByCount = new HashMap<>();
        for (Movie favourite : favourites) {
            try {
                for (PersonCrew personCrew : favourite.movieDB.getCrew()) {
                    if (!personCrew.getJob().equals("Director")) {
                        continue;
                    }
                    Integer crew = directorsByCount.get(personCrew);
                    if (crew == null) crew = 0;
                    crew++;
                    directorsByCount.put(personCrew, crew);
                }

            } catch (NullPointerException e) { }
        }
        HashMap<PersonCrew, Integer> sortedDirectorsByCount = sortByValue(directorsByCount);

        // Dodaj 4 najpopularniejszych aktorów do listy
        ArrayList<PersonCrew> popularDirectors = new ArrayList<>();
        int i = 0;
        for (Map.Entry<PersonCrew, Integer> perDirector : sortedDirectorsByCount.entrySet()) {
            i++;
            if (i > NUMBER_OF_MOST_POPULAR_DIRECTORS - 1) break;
            if (perDirector.getValue() >= MIN_DIRECTOR_POPULARITY) {
                popularDirectors.add(perDirector.getKey());
            }
        }

        return popularDirectors;
    }

    @Override
    public ArrayList<MovieDb> getRecommendations(List<Movie> favourites) {
        ArrayList<PersonCrew> popularDirectors = getPopularDirectors(favourites);

        ArrayList<MovieDb> list = new ArrayList<>();
        TmdbDiscoverPeople discoverPeople = new TmdbDiscoverPeople(tmdbApi);

        for (PersonCrew director : popularDirectors) {
            String personId = String.valueOf(director.getId());
            MovieResultsPage discoverWithPeople = discoverPeople.getDiscoverWithPeople(personId);

            // Tworzę zbiór z ulubionymi, by w czasie O(1) sprawdzać czy film rekomendowany już jest w ulubionych
            Set<MovieDb> favouritesSet = new HashSet<>();
            for(Movie favourite : favourites){
                favouritesSet.add(favourite.movieDB);
            }

            int j = 0;
            for(MovieDb popularMovie : discoverWithPeople.getResults()) {
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
