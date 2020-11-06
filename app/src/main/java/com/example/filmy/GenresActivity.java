package com.example.filmy;

import android.os.AsyncTask;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbGenre;

import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;


public class GenresActivity extends AppCompatActivity {

    GenresAdapter genreAdapter;
    RecyclerView recyclerView;
    List<Genre> genres;


    public class DownloadGenre extends AsyncTask<String, Void, List<Genre>> {

        @Override
        protected List<Genre> doInBackground(String... strings) {
            TmdbGenre popular = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getGenre();
            List<Genre> genres = popular.getGenreList(null);
            return genres;
        }

        @Override
        protected void onPostExecute(List<Genre> genres) {
            super.onPostExecute(genres);

            for(Genre genre : genres){

                GenresActivity.this.genres.add(genre);
            }

            genreAdapter.notifyDataSetChanged();
        }

    }

    public void genre(){
        DownloadGenre downloadGenre = new DownloadGenre();
        downloadGenre.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres);

        setTitle("Genres");

        genres = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        genreAdapter = new GenresAdapter(this, genres);
        recyclerView.setAdapter(genreAdapter);

        genre();
    }

}
