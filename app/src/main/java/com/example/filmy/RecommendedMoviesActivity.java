package com.example.filmy;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmy.database.Movie;
import com.example.filmy.database.MovieDao;
import com.example.filmy.database.MovieDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbDiscover;
import info.movito.themoviedbapi.TmdbFind;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.FindResults;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCast;

/**
 * Aktywność wyświetlająca rekomendowane filmy.
 */
public class RecommendedMoviesActivity extends AppCompatActivity {

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
                            Intent favourites = new Intent(getApplicationContext(), com.example.filmy.FavouritesMoviesActivity.class);
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
//        public class GetMovieFromDatabase extends AsyncTask<Integer, Void, List<Movie>> {
//
//            @Override
//            protected List<Movie> doInBackground(Integer... movie) {
//                movieDao.getAll();
//                Integer movieID = movie[0];
//                TmdbMovies recommended = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
//                MovieResultsPage resultRecommended = recommended.getRecommendedMovies(movieID, null, null);
//                return resultRecommended;
//            }
//
//            @Override
//            protected void onPostExecute(List<Movie> movies) {
//                super.onPostExecute(movies);
//
//                //clear favourite
//                list.clear();
//
//                for (Movie movie : movies) {
//                    list.add(movie.movieDB);
//                }
//                movieAdapter.notifyDataSetChanged();
//            }
//        }



        /**
         * Pobiera ulubione filmy z bazy danych i wyświetla je jako karty.
         */
//        public void favourite(){
//            GetMovieFromDatabase getMovieFromDatabase = new GetMovieFromDatabase();
//            getMovieFromDatabase.execute();
//        }

        /**
         * Po wznowieniu aktywności odświeża listę ulubionych.
         */
//        @Override
//        protected void onResume() {
//            super.onResume();
//            favourite();
//        }
        public class DownloadMovie extends AsyncTask<Void, Void, ArrayList<MovieDb>>{

            // function to sort hashmap by values
            public  HashMap<PersonCast, Integer> sortByValue(HashMap<PersonCast, Integer> hm)
            {
                // Create a list from elements of HashMap
                List<Map.Entry<PersonCast, Integer> > list =
                        new LinkedList<>(hm.entrySet());

                // Sort the list
                Collections.sort(list, new Comparator<Map.Entry<PersonCast, Integer> >() {
                    public int compare(Map.Entry<PersonCast, Integer> o1,
                                       Map.Entry<PersonCast, Integer> o2)
                    {
                        return (o2.getValue()).compareTo(o1.getValue());
                    }
                });

                // put data from sorted list to hashmap
                HashMap<PersonCast, Integer> temp = new LinkedHashMap<PersonCast, Integer>();
                for (Map.Entry<PersonCast, Integer> aa : list) {
                    temp.put(aa.getKey(), aa.getValue());
                }
                return temp;
            }


            @Override
            protected ArrayList<MovieDb> doInBackground(Void... ignore) {
               List<Movie> favourites = movieDao.getAll();
                ArrayList<MovieDb> list = new ArrayList<>();
                if (favourites.isEmpty()
                ) {
                    //todo getTopRated.
                    return list;
                }
                HashMap<PersonCast, Integer> castByNumber = new HashMap<> ();
                for(Movie favourite : favourites) {
                   int movieID = favourite.movieID;
                   TmdbMovies recommendedMovies = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
                   MovieResultsPage resultRecommended = recommendedMovies.getRecommendedMovies(movieID, null, null);
                   int i = 0;
                   for(MovieDb recommendedMovie : resultRecommended){
                       i++;
                       if(i > 3) break;
                       list.add(recommendedMovie);

                   }
                   try {
                       for (PersonCast personCast : favourite.movieDB.getCast()) {
                           Integer cast = castByNumber.get(personCast);
                           if (cast == null) cast = 0;
                           cast++;
                           castByNumber.put(personCast, cast);
                       }


                   }
                   catch(NullPointerException e){};

               }

                HashMap<PersonCast, Integer> sortedCastByNumber = sortByValue(castByNumber);

                ArrayList<PersonCast> popularCast = new ArrayList<>();
                int i =0;
                for(Map.Entry<PersonCast, Integer> perCast : sortedCastByNumber.entrySet()){
                    i++;
                    if(i > 4) break;
                    if(perCast.getValue() > 1)
                    popularCast.add(perCast.getKey());
                }


                if(popularCast.size() > 0)
                {
                    FindResults resultFind = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getFind().find(popularCast.get(0).getCharacter(), TmdbFind.ExternalSource.imdb_id, "en");
                    int j = 0;
                    for(MovieDb popularMovie : resultFind.getMovieResults()){
                        j++;
                        if(j > 3) break;
                        list.add(popularMovie);

                    }
                }

                Set<MovieDb> set = new HashSet<>(list);

                for(Movie favourite : favourites){
                    set.remove(favourite.movieDB);
                }

                list.clear();
                list.addAll(set);

                //get SImilarMovies()
                Collections.sort(list, new Comparator<MovieDb>() {
                    @Override
                    public int compare(MovieDb o1, MovieDb o2) {
                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                        return o1.getVoteAverage() > o2.getVoteAverage() ? -1 : (o1.getVoteAverage() < o2.getVoteAverage()) ? 1 : 0;
                    }
                });

               return list;

              }


            @Override
            protected void onPostExecute(ArrayList<MovieDb> resultRecommended) {
                super.onPostExecute(resultRecommended);

                for(MovieDb movieDb : resultRecommended){
                    list.add(movieDb);
                }
                movieAdapter.notifyDataSetChanged();
            }
        }

    public void download(){

        DownloadMovie downloadMovieDetail = new DownloadMovie();
        downloadMovieDetail.execute();
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
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

            //recommended();
            download();
        }
    }


