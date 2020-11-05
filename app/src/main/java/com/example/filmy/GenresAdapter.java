package com.example.filmy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.MyViewHolder> {

    Context context;
    List<Genre> genres;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView genreName;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            genreName = (TextView) view.findViewById(R.id.genreName);
            imageView = (ImageView) view.findViewById(R.id.imageGenre);
        }
    }

    public GenresAdapter(Context context, List<Genre> genre) {
        this.context = context;
        this.genres = genre;
    }

    @Override
    public GenresAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.genre_card, viewGroup, false);
        return new GenresAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GenresAdapter.MyViewHolder myViewHolder, int i) {
        Genre genre = genres.get(i);
        myViewHolder.genreName.setText(genre.getName());
   /*     //String path = "https://image.tmdb.org/t/p/w200" + list.get(i).getPosterPath();
       // Picasso.get().load(path).into(myViewHolder.imageView);
        final int position = myViewHolder.getAdapterPosition();
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("movieDB", list.get(position));
                context.startActivity(intent);
            }
        });*/
    }
    @Override
    public int getItemCount() {
        return genres.size();
    }
}
