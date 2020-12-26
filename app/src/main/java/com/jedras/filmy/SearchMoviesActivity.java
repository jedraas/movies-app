package com.jedras.filmy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

/**
 * Aktywność pozwalająca na wyszukiwanie filmów.
 */
public class SearchMoviesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView searchView;
    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    String query;
    ArrayList<MovieDb> list = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
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

    @Override
    public boolean onQueryTextSubmit(String query_) {
        query = query_;
        list.clear();
        searchMovies(1);
        searchView.clearFocus();
        return true;
    }

    private void searchMovies(Integer page) {
        DownloadMovie downloadMovie = new DownloadMovie();
        downloadMovie.execute(query, String.valueOf(page));
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() == 0){
            list.clear();
            movieAdapter.notifyDataSetChanged();
        }
        return true;
    }

    /**
     * Pobiera filmy z bazy danych i zapisuje je w obiekcie o nazwie list.
     */
    public class DownloadMovie extends AsyncTask<String, Void, MovieResultsPage> {

        @Override
        protected MovieResultsPage doInBackground(String... strings) {
            String text = (strings.length > 0) ? strings[0] : null;
            Integer page =  Integer.valueOf(strings[1]);
            TmdbSearch search = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getSearch();
            MovieResultsPage resultSearch = search.searchMovie(text, null, null, true, page);
            return resultSearch;
        }

        @Override
        protected void onPostExecute(MovieResultsPage resultSearch) {
            super.onPostExecute(resultSearch);

            for (MovieDb movieDb : resultSearch) {
                list.add(movieDb);
            }
            movieAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setTitle("Find Movies");

        bottomNavigation();

        searchView = (SearchView) findViewById(R.id.search);

        searchView.setOnQueryTextListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        GridLayoutManager gridLayoutManager =  new GridLayoutManager(this, 2);
        RecyclerView.LayoutManager mLayoutManager = gridLayoutManager;
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        movieAdapter = new MovieAdapter(this, list);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
               searchMovies(page + 1);
            }

        });

    }
}
