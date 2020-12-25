package com.jedras.filmy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;


/**
 * Aktywność wyswietlająca najwyżej oceniane filmy.
 */
public class TopRatedMoviesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    ArrayList<MovieDb> list = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.topRated);
        item.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
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
                        Intent favourites = new Intent(getApplicationContext(), FavouriteMoviesActivity.class);
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
    public class DownloadMovie extends AsyncTask<Integer, Void, MovieResultsPage> {

        @Override
        protected MovieResultsPage doInBackground(Integer... page) {
            TmdbMovies topRated = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
            MovieResultsPage resultTopRated = topRated.getTopRatedMovies(null, page[0]);
            return resultTopRated;
        }

        @Override
        protected void onPostExecute(MovieResultsPage resultTopRated) {
            super.onPostExecute(resultTopRated);

            for (MovieDb movieDb : resultTopRated) {
                list.add(movieDb);
            }
            movieAdapter.notifyDataSetChanged();
        }

    }

    /**
     * Pobiera najwyżej oceniane filmy z bazy danych i wyświetla je jako karty.
     */
    public void topRated(Integer page) {
        DownloadMovie downloadMovie = new DownloadMovie();
        downloadMovie.execute(page);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_rated_movies);

        setTitle("Top Rated Movies");

        bottomNavigation();

        topRated(1);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        GridLayoutManager gridLayoutManager =  new GridLayoutManager(this, 2);
        RecyclerView.LayoutManager mLayoutManager = gridLayoutManager;        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        movieAdapter = new MovieAdapter(this, list);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                topRated(page + 1);
            }

        });
    }
}
