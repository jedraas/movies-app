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
import com.jedras.filmy.database.Movie;
import com.jedras.filmy.database.MovieDao;
import com.jedras.filmy.database.MovieDatabase;
import com.jedras.filmy.recommendations.CollaborativeRecommender;
import com.jedras.filmy.recommendations.DirectorRecommender;
import com.jedras.filmy.recommendations.GenreRecommender;
import com.jedras.filmy.recommendations.LanguageRecommender;
import com.jedras.filmy.recommendations.PopularCastRecommender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;


/**
 * Aktywność wyświetlająca rekomendowane filmy.
 */
public class RecommendedMoviesActivity extends AppCompatActivity {

    private TmdbApi tmdbApi;
    MovieDao movieDao;
    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    ArrayList<MovieDb> list = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.recommended);
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
     * Pobiera ulubione filmy z bazy danych, dodaje rekomendowane filmy i zapisuje je w obiekcie o nazwie list
     */
    public class DownloadMovieRecommended extends AsyncTask<Void, Void, ArrayList<MovieDb>> {
        @Override
        protected ArrayList<MovieDb> doInBackground(Void... ignore) {

            tmdbApi = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc");

            return getRecommendedMovies();
        }

        @Override
        protected void onPostExecute(ArrayList<MovieDb> resultRecommended) {
            super.onPostExecute(resultRecommended);

            for (MovieDb movieDb : resultRecommended) {
                list.add(movieDb);
            }
            movieAdapter.notifyDataSetChanged();
        }
    }

    private ArrayList<MovieDb> getRecommendedMovies() {
        List<Movie> favourites = movieDao.getAll();
        ArrayList<MovieDb> list = new ArrayList<>();

        /**
         * Pobiera filmy najwyżej oceniane i wyświetla je jako karty, jeżeli użytkownik nie posiada filmów ulubionych.
         */
        if (favourites.isEmpty()) {
            TmdbMovies topRated = tmdbApi.getMovies();
            MovieResultsPage resultTopRated = topRated.getTopRatedMovies(null, null);
            for (MovieDb movieDb : resultTopRated) {
                list.add(movieDb);
            }
        }

        list.addAll(new CollaborativeRecommender().getRecommendations(favourites));
        list.addAll(new PopularCastRecommender().getRecommendations(favourites));
        list.addAll(new DirectorRecommender().getRecommendations(favourites));
        list.addAll(new GenreRecommender().getRecommendations(favourites));
        list.addAll(new LanguageRecommender().getRecommendations(favourites));

        Set<MovieDb> set = new HashSet<>(list);
        for (Movie favourite : favourites) {
            set.remove(favourite.movieDB);
        }

        list.clear();
        list.addAll(set);

        // Sortowanie po ocenie filmu
        Collections.sort(list, new Comparator<MovieDb>() {
            @Override
            public int compare(MovieDb movie1, MovieDb movie2) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return movie1.getVoteAverage() > movie2.getVoteAverage() ? -1 : (movie1.getVoteAverage() < movie2.getVoteAverage()) ? 1 : 0;
            }
        });
        return list;
    }

    /**
     * Pobiera ulubione filmy
     */
    public void downloadRecommended() {
        DownloadMovieRecommended downloadMovieRecommended = new DownloadMovieRecommended();
        downloadMovieRecommended.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended);

        setTitle("Recommended Movies");

        bottomNavigation();

        movieDao = MovieDatabase.getMovieDatabase(this).movieDao();

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        movieAdapter = new MovieAdapter(this, list);
        recyclerView.setAdapter(movieAdapter);

        downloadRecommended();
    }
}


