package com.jedras.filmy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import info.movito.themoviedbapi.model.Genre;

/**
 * Adapter służący do wyświetlenia listy gatunków filmów w formie listy RecyclerView.
 */
public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.MyViewHolder> {

    Context context;
    List<Genre> genres;
    private final HashMap<String, Integer> gat = new HashMap<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView genreName;
        public ImageView imageGenre;

        public MyViewHolder(View view) {
            super(view);
            genreName = (TextView) view.findViewById(R.id.genreName);
            imageGenre = (ImageView) view.findViewById(R.id.imageGenre);
        }
    }

    public GenresAdapter(Context context, List<Genre> genre) {
        this.context = context;
        this.genres = genre;
        gat.put("Action", R.drawable.action);
        gat.put("Adventure", R.drawable.adventure);
        gat.put("Animation", R.drawable.animation);
        gat.put("Comedy", R.drawable.comedy);
        gat.put("Crime", R.drawable.crime);
        gat.put("Documentary", R.drawable.document);
        gat.put("Drama", R.drawable.drama);
        gat.put("Family", R.drawable.family);
        gat.put("Fantasy", R.drawable.fantasy);
        gat.put("History", R.drawable.history);
        gat.put("Horror", R.drawable.horror);
        gat.put("Music", R.drawable.music);
        gat.put("Mystery", R.drawable.mystery);
        gat.put("Romance", R.drawable.romance);
        gat.put("Science Fiction", R.drawable.sci_fi);
        gat.put("Thriller", R.drawable.thriller);
        gat.put("TV Movie", R.drawable.tv_movie);
        gat.put("War", R.drawable.war);
        gat.put("Western", R.drawable.western);

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

        Integer resource = gat.get(genre.getName());
        if (resource != null) {
            myViewHolder.imageGenre.setImageResource(resource);
        } else {
            //domyslny obrazek
            myViewHolder.imageGenre.setImageResource(R.drawable.logo);
        }
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
