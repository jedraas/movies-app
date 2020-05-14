package com.example.filmy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MovieDetail extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        setTitle(getTitle());
    }

}
