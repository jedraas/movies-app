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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder>  {



        Context context;
        ArrayList<MovieDb> list;

        public class MyViewHolder extends RecyclerView.ViewHolder{

            public TextView title;
            public ImageView imageView;


            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                imageView = (ImageView) view.findViewById(R.id.image);


            }

        }
        public MovieAdapter(Context context, ArrayList<MovieDb> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_card, viewGroup, false);
            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
            myViewHolder.title.setText(list.get(i).getTitle());
            String path = "https://image.tmdb.org/t/p/w200" + list.get(i).getPosterPath();
            Picasso.get().load(path).into(myViewHolder.imageView);
            final int position = myViewHolder.getAdapterPosition();
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra("movieDB", list.get(position));
                    context.startActivity(intent);
                }
            });



        }
        @Override
        public int getItemCount() {
            return list.size();
        }


    }


