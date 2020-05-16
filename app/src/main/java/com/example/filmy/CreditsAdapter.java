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

import info.movito.themoviedbapi.model.MovieDb;

public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.MyViewHolder>{
    Context context;
    ArrayList<MovieDb> list;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView textCast;
        public ImageView imageCast;


        public MyViewHolder(View view) {
            super(view);
            textCast = (TextView) view.findViewById(R.id.textCast);
            imageCast = (ImageView) view.findViewById(R.id.imageCast);


        }

    }
    public CreditsAdapter(Context context, ArrayList<MovieDb> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_card, viewGroup, false);
        return new CreditsAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.textCast.setText(list.get(i).getTitle());
        String path = "https://image.tmdb.org/t/p/w200" + list.get(i).getPosterPath();
        Picasso.get().load(path).into(myViewHolder.imageCast);


    }
    @Override
    public int getItemCount() {
        return list.size();
    }

}
