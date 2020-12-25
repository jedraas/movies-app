package com.jedras.filmy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import info.movito.themoviedbapi.model.people.PersonCast;

/**
 * Adapter służący do wyświetlenia listy aktorów w formie listy RecyclerView.
 */
public class CastAdapter extends RecyclerView.Adapter<CastAdapter.MyViewHolder> {

    Context context;
    ArrayList<PersonCast> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textCast;
        public ImageView imageCast;

        public MyViewHolder(View view) {
            super(view);
            textCast = (TextView) view.findViewById(R.id.textCast);
            imageCast = (ImageView) view.findViewById(R.id.imageCast);
        }
    }

    public CastAdapter(Context context, ArrayList<PersonCast> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cast_card, viewGroup, false);
        return new CastAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.textCast.setText(list.get(i).getName());
        String path = "https://image.tmdb.org/t/p/w200" + list.get(i).getProfilePath();
        Picasso.get().load(path).error(R.drawable.placeholder).into(myViewHolder.imageCast);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
