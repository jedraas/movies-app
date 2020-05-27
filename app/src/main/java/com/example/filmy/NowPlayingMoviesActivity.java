package com.example.filmy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

/**
 * aktywnosc wyswietlajaca obecnie odtwarzane filmy
 */
public class NowPlayingMoviesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    ArrayList<MovieDb> list = new ArrayList<>();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.nowPlaying);
        item.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.topRated:
                Intent topRated = new Intent(getApplicationContext(), TopRatedMoviesActivity.class);
                startActivity(topRated);
                return true;
            case R.id.nowPlaying:
                Intent nowPlaying = new Intent(getApplicationContext(), NowPlayingMoviesActivity.class);
                startActivity(nowPlaying);
                return true;
            case R.id.upComing:
                Intent upComing = new Intent(getApplicationContext(), UpComingMoviesActivity.class);
                startActivity(upComing);
                return true;
            default:
                return false;
        }
    }

    /**
     * wyswietlanie dolnego paska menu zawierajacego 3 zakladki (home, search, favourites)
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
     * pobieranie obecnie granych filmow z bazy danych i wyswietlanie ich jako karty
     */
    public void nowPlaying(){
       DownloadMovie downloadMovie = new DownloadMovie();
        downloadMovie.execute();
    }

    /**
     * pobieranie filmow z internetu i dodanie ich do listy
     */

    public class DownloadMovie extends AsyncTask<String, Void, MovieResultsPage> {

        @Override
        protected MovieResultsPage doInBackground(String... strings) {
            TmdbMovies nowPlaying = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
            MovieResultsPage resultNowPlaying = nowPlaying.getNowPlayingMovies(null, null, null);
            return resultNowPlaying;
        }

        @Override
        protected void onPostExecute(MovieResultsPage resultNowPlaying) {
            super.onPostExecute(resultNowPlaying);

            for(MovieDb movieDb : resultNowPlaying){

                list.add(movieDb);
            }

            movieAdapter.notifyDataSetChanged();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing_movies);

        setTitle("Now Playing Movies");

        bottomNavigation();

        nowPlaying();

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        movieAdapter = new MovieAdapter(this, list);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

    }
}
