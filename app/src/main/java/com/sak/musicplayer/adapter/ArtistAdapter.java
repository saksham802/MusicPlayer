package com.sak.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sak.musicplayer.R;
import com.sak.musicplayer.listner.SelectListnerArtrist;
import com.sak.musicplayer.data.Artist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.MyViewHolder> {
    public ArtistAdapter(Context context, ArrayList<Artist> list,SelectListnerArtrist listnerArtrist) {
        this.context = context;
        this.list = list;
        this.listnerArtrist=listnerArtrist;
    }

    Context context;
    ArrayList<Artist> list;
    SelectListnerArtrist listnerArtrist;
    @NonNull
    @Override
    public ArtistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.artist_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistAdapter.MyViewHolder holder, int position) {

        Artist artist= list.get(position);


        holder.textView.setText(artist.getName());

        // Use Picasso to load images efficiently with placeholder and error handling
        Picasso.get()
                .load(artist.getCoverUrl())
                .into(holder.imageView);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listnerArtrist.onItemClicked(list.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        LinearLayout layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.artist_name);
            imageView=itemView.findViewById(R.id.artist_cover_image);
            layout=itemView.findViewById(R.id.artist);
        }
    }
}
