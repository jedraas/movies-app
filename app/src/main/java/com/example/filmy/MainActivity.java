package com.example.filmy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.core.MovieResultsPage;


//tu ma byc logowanie


public class MainActivity extends AppCompatActivity {



public void signUpClicked(View view){
    Button signUpButton = (Button) findViewById(R.id.signUpButton);
    Intent intent = new Intent(getApplicationContext(), PopularMovies.class);
    startActivity(intent);
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("digUpMovie");



    }




   // public MovieResultsPage getRecommendedMovies(int movieId, String language, Integer page)
    //filmy rekomendowane
    /*void recommended(){
        TmdbMovies movies = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
        //MovieDb movie = movies.getMovie(5353, "en");
        //Log.i("Halo",  "" + movie );
        MovieResultsPage result = movies.getRecommendedMovies(5353, "en", 0);
        Log.i("Halo",  "" + result.getResults());
    }*/
/*
    //film id:5353
    void find(){
        TmdbMovies movies = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
        MovieDb movie = movies.getMovie(5353, "en");
        Log.i("Halo",  "" + movie );
    }
*/

//filmy akcji wedlug popularnosci
  /*  void genres(){
        TmdbGenre genre = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getGenre();
        MovieResultsPage filmy = genre.getGenreMovies(28, "en", 0, true);
        Log.i("Halko", "" + filmy.getResults());

    }*/

   //topratedmovies
   /* void topRated(){
        TmdbMovies movies = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
        MovieResultsPage result = movies.getTopRatedMovies("en", 0);
        Log.i("Halko", "" + result.getResults());

    }*/

 /*   void find(){
        TmdbMovies popular = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
        MovieResultsPage resultPopular = popular.getPopularMovies("", 0);
        Log.i("Popular movies: ", "" + resultPopular);
    }*/
   //nadchodzace - cos tu nie halko
   /*void find(){
       TmdbMovies movies = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
       MovieResultsPage result = movies.getUpcoming("",0,"");
       Log.i("Halko", "" + result.getResults());
   }*/

   /*void find(){
       TmdbTvSeasons tvSeasons = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getTvSeasons();
       TvSeason result = tvSeasons.getSeason(60735,3,"");
       Log.i("Halko", "" + result);
   }*/

     /*void find() {
         TmdbTvSeasons tvSeasons = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getTvSeasons();
         TvSeason result = tvSeasons.getSeason(1396, 3, "");
         Log.i("Halko", "" + result);


     }*/

    /* void find(){
         TmdbSearch search = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getSearch();
         MovieResultsPage result = search.searchMovie("", 2012, "", true, 0);
         Log.i("bum", "" + result);
     }*/
}
