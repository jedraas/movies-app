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



   /* void test(){
        TmdbMovies video = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
        List<Video> resultVideo =  video.getVideos(2, null);
        Log.i("Now playing movies: ", "" + resultVideo);
    }
    */
/*   void test(){

       TmdbMovies similar = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
       MovieResultsPage resultSimilarMovie = similar.getSimilarMovies(475557, null, null);
       Log.i("Similar movies: ", "" + resultSimilarMovie.getResults());

   }

    void test2(){

        TmdbMovies rekom = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
        MovieResultsPage resultReko = rekom.getRecommendedMovies(475557, null, null);
        Log.i("Rekom  movies: ", "" + resultReko.getResults());

    }*/

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

 /*   void test(){
        TmdbGenre video = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getGenre();
        MovieResultsPage resultVideo =  video.getGenreMovies(12, null, null, true);
        Log.i("Now playing movies: ", "" + resultVideo.getResults());
    }*/
  /*  void test(){
        TmdbGenre genre = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getGenre();
        List<Genre> resultVideo =  genre.getGenreList(null);
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
            case R.id.genres:
                Intent action = new Intent(getApplicationContext(), GenresActivity.class);
                startActivity(action);
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


    /**
     * Pobiera popularne filmy z bazy danych i wyświetla je jako karty.
     */
    public void popular(){
        DownloadMovie downloadMovie = new DownloadMovie();
        downloadMovie.execute();
    }



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

        thread.start();*/


        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        movieAdapter = new MovieAdapter(this, list);
        recyclerView.setAdapter(movieAdapter);

        popular();

    }
}
