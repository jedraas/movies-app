package com.jedras.filmy.recommendations;

import com.jedras.filmy.database.Movie;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class DirectorRecommender extends MovieRecommender {
    @Override
    public ArrayList<MovieDb> getRecommendations(List<Movie> favorites) {
        return new ArrayList<>();
    }
}
