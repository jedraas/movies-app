package com.example.filmy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Video;


public class MovieDetail extends AppCompatActivity {

    Video trailer;
    ImageView imageBackground;
    ImageView imagePoster;
    MovieDb movieDB;
    Button trailerButton;
    Button favouritesButton;
    Credits credits;

    RecyclerView rvCast;
    CreditsAdapter creditsAdapter;


    public void watchTrailer(View view){

        String url = "https://www.youtube.com/watch?v=" + trailer.getKey();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void addToFavourites(View view){
        Intent intent = new Intent(this, Favourites.class);
        startActivity(intent);
    }


    public class DownloadMovie extends AsyncTask<Integer, Void, List<Video>> {

        @Override
        protected List<Video> doInBackground(Integer... strings) {
            Integer movieID = strings[0];
            TmdbMovies video = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
            List<Video> resultVideo =  video.getVideos(movieID, null);
            return resultVideo;

        }
        @Override
        protected void onPostExecute(List<Video> resultVideo) {
            super.onPostExecute(resultVideo);

            for (Video video: resultVideo) {

                if("Trailer".equals(video.getType())){
                    MovieDetail.this.trailer = video;
                    MovieDetail.this.trailerButton.setVisibility(View.VISIBLE);
                    break;
                }

            }

        }

    }


    //CAST
    public class DownloadCast extends AsyncTask<Integer, Void, Credits> {

        @Override
        protected Credits doInBackground(Integer... strings) {
            Integer movieID = strings[0];
            TmdbMovies credit = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
            Credits resultCredits =  credit.getCredits(movieID);
            return resultCredits;


        }

        @Override
        protected void onPostExecute(Credits resultCredits) {
            super.onPostExecute(resultCredits);


           // creditsAdapter.notifyDataSetChanged();





        }
    }


    void detail() {
        DownloadMovie downloadMovie = new DownloadMovie();
        downloadMovie.execute(movieDB.getId());
    }


    void credits() {
        DownloadCast downloadCast = new DownloadCast();
        downloadCast.execute(movieDB.getId());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);




        movieDB = (MovieDb) getIntent().getSerializableExtra("movieDB");

        setTitle(movieDB.getTitle());

        imagePoster = (ImageView) findViewById(R.id.poster);
        imageBackground = (ImageView) findViewById(R.id.backdrop);
        TextView textTitle = (TextView) findViewById(R.id.text_title);
        TextView textOverview = (TextView) findViewById(R.id.textOverview);
        TextView textLanguage = (TextView) findViewById(R.id.textLanguage);
        TextView textVote = (TextView) findViewById(R.id.textVotes);
        TextView textTotalVote = (TextView) findViewById(R.id.textTotalVotes);
        TextView textDate = (TextView) findViewById(R.id.textDate);
        trailerButton = (Button) findViewById(R.id.trailers);
        trailerButton.setVisibility(View.INVISIBLE);
        favouritesButton = (Button) findViewById(R.id.favouritesButton);




        //backdrop
        String backdropPath = "https://image.tmdb.org/t/p/w200" + movieDB.getBackdropPath();
        Picasso.get().load(backdropPath).into(MovieDetail.this.imageBackground);

        //poster
        String posterPath = "https://image.tmdb.org/t/p/w200" + movieDB.getPosterPath();
        Picasso.get().load(posterPath).into(MovieDetail.this.imagePoster);

        //przekazanie tytulu
        textTitle.setText(movieDB.getTitle());


        //przekazanie overview
        textOverview.setText(movieDB.getOverview());

        //przekazanie language
        textLanguage.setText(movieDB.getOriginalLanguage());

//        //przekazanie vote
        textVote.setText(Float.toString(movieDB.getVoteAverage()) + "/10");

        //przekazanie totalvote
        textTotalVote.setText(Integer.toString(movieDB.getVoteCount()));

        //przekazanie daty
        textDate.setText(movieDB.getReleaseDate());

       credits();
        detail();

        rvCast = (RecyclerView) findViewById(R.id.rvCast);
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rvCast.setLayoutManager(mLayoutManager);
        rvCast.setItemAnimator(new DefaultItemAnimator());
        //creditsAdapter = new CreditsAdapter(this, list);
        rvCast.setAdapter(creditsAdapter);
        rvCast.addItemDecoration(new DividerItemDecoration(rvCast.getContext(), DividerItemDecoration.VERTICAL));


    }
}
