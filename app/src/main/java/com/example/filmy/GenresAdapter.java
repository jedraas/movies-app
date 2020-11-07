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

import java.util.List;

import info.movito.themoviedbapi.model.Genre;

/**
 * Adapter służący do wyświetlenia listy gatunków filmów w formie listy RecyclerView.
 */
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
        //String path = "@drawable/ic_search_black_24dp";
        //Picasso.get().load(path).into(myViewHolder.imageView);
        final int position = myViewHolder.getAdapterPosition();
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(context, GenresDetailActivity.class);
                intent.putExtra("movieDBGenre", genres.get(position));
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return genres.size();
    }
}
