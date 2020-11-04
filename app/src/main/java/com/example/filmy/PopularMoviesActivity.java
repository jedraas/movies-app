package com.example.filmy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbGenre;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Video;
import info.movito.themoviedbapi.model.core.MovieResultsPage;


/**
 * Aktywność wyświetlająca popularne filmy.
 */
public class PopularMoviesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    ArrayList<MovieDb> list = new ArrayList<>();
    MovieDb movieDB;
    ChipGroup genres;


   /* void test(){
        TmdbMovies video = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
        List<Video> resultVideo =  video.getVideos(2, null);
        Log.i("Now playing movies: ", "" + resultVideo);
    }
    */

   /* void test(){
        TmdbMovies nowPlaying = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
        MovieResultsPage resultNowPlaying = nowPlaying.getNowPlayingMovies(null, null, null);
        Log.i("Now playing movies: ", "" + resultNowPlaying.getResults());
    }
    */

      /*  void test(){
        TmdbMovies recommended = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
        MovieResultsPage resultRecommended = recommended.getRecommendedMovies(724989, null, null);
        Log.i("Recommended movies: ", "" + resultRecommended.getResults());
    }
*/

  /*  void test(){
        TmdbGenre video = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getGenre();
        MovieResultsPage resultVideo =  video.getGenreMovies(12, null, null, true);
        Log.i("Now playing movies: ", "" + resultVideo);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.popular);
        item.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.popular:
                Intent popular = new Intent(getApplicationContext(), PopularMoviesActivity.class);
                startActivity(popular);
                return true;
            case R.id.topRated:
                Intent topRated = new Intent(getApplicationContext(), TopRatedMoviesActivity.class);
                startActivity(topRated);
                return true;
            case R.id.nowPlaying:
                Intent nowPlaying = new Intent(getApplicationContext(), NowPlayingMoviesActivity.class);
                startActivity(nowPlaying);
                return true;
            case R.id.upcoming:
                Intent upcoming = new Intent(getApplicationContext(), UpcomingMoviesActivity.class);
                startActivity(upcoming);
                return true;
            case R.id.recommended:
                Intent recommended = new Intent(getApplicationContext(), RecommendedMoviesActivity.class);
                startActivity(recommended);
                return true;
            default:
                return false;
        }
    }

    /**
     * Wyświetla menu znajdujące się w dolnej części ekranu, które zawiera odnośniki do innych aktywności.
     */
    public void bottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent popular = new Intent(getApplicationContext(), PopularMoviesActivity.class);
                        startActivity(popular);
                        return true;
                    case R.id.favourites:
                        Intent favourites = new Intent(getApplicationContext(), FavouritesMoviesActivity.class);
                        startActivity(favourites);
                        return true;
                    case R.id.search:
                        Intent search = new Intent(getApplicationContext(), SearchMoviesActivity.class);
                        startActivity(search);
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Pobiera popularne filmy z bazy danych i wyświetla je jako karty.
     */
    public void popular(){
        DownloadMovie downloadMovie = new DownloadMovie();
        downloadMovie.execute();

    }
/*

    public void download(){

        DownloadMovieDetail downloadMovieDetail = new DownloadMovieDetail();
        downloadMovieDetail.execute();
    }
*/




    /**
     * Pobiera filmy z bazy danych i zapisuje je w obiekcie o nazwie list.
     */
   public class DownloadMovie extends AsyncTask<String, Void, MovieResultsPage>{

       @Override
       protected MovieResultsPage doInBackground(String... strings) {
           TmdbMovies popular = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
           MovieResultsPage resultPopular = popular.getPopularMovies(null, null);
           return resultPopular;
       }

       @Override
       protected void onPostExecute(MovieResultsPage resultPopular) {
           super.onPostExecute(resultPopular);

           for(MovieDb movieDb : resultPopular){
               list.add(movieDb);
           }
           movieAdapter.notifyDataSetChanged();
       }
   }

   /* public class DownloadMovieDetail extends AsyncTask<String, Void, MovieResultsPage> {

        @Override
        protected MovieResultsPage doInBackground(String... strings) {
            TmdbGenre video = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getGenre();
            MovieResultsPage resultDetail =  video.getGenreMovies(12, null, null, true);
            return resultDetail;
        }
        @Override
        protected void onPostExecute(MovieResultsPage resultDetail) {
            super.onPostExecute(resultDetail);

            for(MovieDb movieDb : resultDetail){
                Chip chip = (Chip) LayoutInflater.from(PopularMoviesActivity.this).inflate(R.layout.genres_chip, PopularMoviesActivity.this.genres, false);

                PopularMoviesActivity.this.genres.addView(chip);

            }
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);



        setTitle("Popular Movies");

        bottomNavigation();

     /*  Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {

                    test();



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
*/


        genres = (ChipGroup) findViewById(R.id.genres);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        movieAdapter = new MovieAdapter(this, list);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        popular();
       //download();
    }
}
