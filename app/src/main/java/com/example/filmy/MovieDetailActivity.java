package com.example.filmy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filmy.database.Movie;
import com.example.filmy.database.MovieDao;
import com.example.filmy.database.MovieDatabase;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Video;
import info.movito.themoviedbapi.model.people.PersonCast;


/**
 * Aktywność wyświetlająca szczegóły filmu.
 */
public class MovieDetailActivity extends AppCompatActivity {

    Video trailer;
    ImageView imageBackdrop;
    ImageView imagePoster;
    TextView textRuntime;
    MovieDb movieDB;
    Button trailerButton;
    Button favouritesButton;
    RecyclerView rvCast;
    CastAdapter castAdapter;
    ChipGroup genres;
    MovieDao movieDao;
    ArrayList<PersonCast> cast = new ArrayList();
    Credits credits;

    // https://stackoverflow.com/questions/2317428/how-to-refresh-app-upon-shaking-the-device
    //shake device to add/remove favourite
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    /**
     * Listener, który po wykryciu wstrząśniecia dodaje lub usuwa film z ulubionych.
     */
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            if (mAccel > 5) {
                Toast toast = Toast.makeText(getApplicationContext(), MovieDetailActivity.this.isFavourite()?"Remove from favourites" : "Added to favourites.", Toast.LENGTH_LONG);
                toast.show();
                MovieDetailActivity.this.favouriteButtonClicked(null);
            }
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    /**
     * Uruchamia trailer filmu w YouTube.
     * @param view widok który wywołał metodę.
     */
    public void watchTrailer(View view){
        String url = "https://www.youtube.com/watch?v=" + trailer.getKey();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    /**
     * Dodaje lub usuwa film z ulubionych.
     * @param view widok który wywołał metodę.
     */
    public void favouriteButtonClicked(View view){
        if(isFavourite()){
            removeFromFavourites();
        } else{
            addToFavourites();
        }
    }

    /**
     * Dodaje aktualny film do bazy danych ulubionych filmów oraz aktualizuje działanie przycisku, który teraz służy do usuwania z ulubionych.
     */
    public void addToFavourites(){
        Movie movie = new Movie();
        movieDB.setCredits(credits);
        movie.movieDB = movieDB;
        movie.movieID = movieDB.getId();
        movieDao.insertAll(movie);
        updateFavouriteButton(true);
    }

    /**
     * Sprawdza czy aktualny film jest na liście ulubionych.
     * @return true jeżeli film jest ulubiony, w przeciwnym przypadku false.
     */
    public boolean isFavourite() {
        List<Movie> favourites = movieDao.getMoviesByID(movieDB.getId());

        if(favourites.isEmpty()){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Usuwa bieżący film z bazy ulubionych filmów.
     */
    public void removeFromFavourites(){
        movieDao.deleteByID(movieDB.getId());
        updateFavouriteButton(false);
    }

    /**
     * Aktualizuje tekst przycisku dodawania do/usuwania z ulubionych.
     * @param favourite czy film jest aktualnie ulubiony.
     */
    public void updateFavouriteButton(boolean favourite){
        if(favourite){
            favouritesButton.setText("Remove from favourites");
        } else {
            favouritesButton.setText("Add to favourites");
        }
    }

    /**
     * Pobiera szczegóły dotyczące filmu z themoviedb API i uaktualnia interfejs.
     */
    public class DownloadMovieDetail extends AsyncTask<Integer, Void, MovieDb> {

        @Override
        protected MovieDb doInBackground(Integer... strings) {
            Integer movieID = strings[0];
            TmdbMovies detail = new TmdbApi("e8f32d6fe548e75c59021f2b82a91edc").getMovies();
            MovieDb resultDetail =  detail.getMovie(movieID, null);
            return resultDetail;
        }
        @Override
        protected void onPostExecute(MovieDb resultDetail) {
            super.onPostExecute(resultDetail);

            for(Genre genre : resultDetail.getGenres()){
                Chip chip = (Chip) LayoutInflater.from(MovieDetailActivity.this).inflate(R.layout.genres_chip, MovieDetailActivity.this.genres, false);
                chip.setText(genre.getName());
                MovieDetailActivity.this.genres.addView(chip);
                String runtime = "<b>Time: </b>" + Integer.toString(resultDetail.getRuntime()) + " min";
                textRuntime.setText(HtmlCompat.fromHtml(runtime, HtmlCompat.FROM_HTML_MODE_LEGACY));
                }
        }
    }

    /**
     * Pobiera link do trailera z themoviedb API i konfiguruje przycisk do oglądania.
     */
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
                //Zabezpieczenie przed przypadkiem gdy video.getType == null
                if("Trailer".equals(video.getType())){
                    MovieDetailActivity.this.trailer = video;
                    MovieDetailActivity.this.trailerButton.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
    }

    /**
     * Pobiera listę aktorów z themoviedb API i zapisuje je w obiekcie o nazwie cast.
     */
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

            credits = resultCredits;
            for(PersonCast personCast : resultCredits.getCast()){
                cast.add(personCast);
            }
           castAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Pobiera szczegóły dotyczące filmu z bazy danych.
     */
    public void download() {
        DownloadMovie downloadMovie = new DownloadMovie();
        downloadMovie.execute(movieDB.getId());

        DownloadCast downloadCast = new DownloadCast();
        downloadCast.execute(movieDB.getId());

        DownloadMovieDetail downloadMovieDetail = new DownloadMovieDetail();
        downloadMovieDetail.execute(movieDB.getId());
    }

    /**
     * Włącza działanie użycia akcelerometra podczas podtrząsania urządzeniem, gdy obecna aktywność jest widoczna.
     */
    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Wyłącza działanie użycia akcelerometra podczas podtrząsania urządzeniem, gdy obecna aktywność jest niewidoczna.
     */
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieDB = (MovieDb) getIntent().getSerializableExtra("movieDB");

        setTitle(movieDB.getTitle());

        imagePoster = (ImageView) findViewById(R.id.poster);
        imageBackdrop = (ImageView) findViewById(R.id.backdrop);
        TextView textTitle = (TextView) findViewById(R.id.text_title);
        TextView textOverview = (TextView) findViewById(R.id.textOverview);
        TextView textLanguage = (TextView) findViewById(R.id.textLanguage);
        TextView textVote = (TextView) findViewById(R.id.textVotes);
        TextView textTotalVote = (TextView) findViewById(R.id.textTotalVotes);
        TextView textDate = (TextView) findViewById(R.id.textDate);
        textRuntime = (TextView) findViewById(R.id.textRuntime);
        trailerButton = (Button) findViewById(R.id.trailers);
        trailerButton.setVisibility(View.INVISIBLE);
        favouritesButton = (Button) findViewById(R.id.favouritesButton);
        genres = (ChipGroup) findViewById(R.id.genres);

        movieDao = MovieDatabase.getMovieDatabase(this).movieDao();

        updateFavouriteButton(isFavourite());

        download();

        //backdrop
        String backdropPath = "https://image.tmdb.org/t/p/w200" + movieDB.getBackdropPath();
        Picasso.get().load(backdropPath).into(MovieDetailActivity.this.imageBackdrop);

        //poster
        String posterPath = "https://image.tmdb.org/t/p/w200" + movieDB.getPosterPath();
        Picasso.get().load(posterPath).into(MovieDetailActivity.this.imagePoster);

        //title
        textTitle.setText(movieDB.getTitle());

        //overview
        textOverview.setText(movieDB.getOverview());

        //language
        textLanguage.setText(movieDB.getOriginalLanguage());

        //vote
        textVote.setText(Float.toString(movieDB.getVoteAverage()) + "/10");

        //totalvote
        textTotalVote.setText(Integer.toString(movieDB.getVoteCount()));

        //date
        textDate.setText(movieDB.getReleaseDate());

        //rv cast
        rvCast = (RecyclerView) findViewById(R.id.rvCast);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        rvCast.setLayoutManager(mLayoutManager);
        rvCast.setItemAnimator(new DefaultItemAnimator());
        castAdapter = new CastAdapter(this, cast);
        rvCast.setAdapter(castAdapter);

        //shake device sensor
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }
}
