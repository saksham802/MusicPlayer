package com.sak.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sak.musicplayer.R;

import com.sak.musicplayer.data.Podcast;
import com.sak.musicplayer.listner.SelectListnerPodcast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PodCastAdapter extends RecyclerView.Adapter<PodCastAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Podcast> list;
    private SelectListnerPodcast listenerPodcast;

    // Constructor to initialize context, list, and listener
    public PodCastAdapter(Context context, ArrayList<Podcast> list, SelectListnerPodcast listenerPodcast) {
        this.context = context;
        this.list = list;
        this.listenerPodcast = listenerPodcast;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.podcast, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Get the podcast at the current position
        Podcast podcast = list.get(position);

        // Set the podcast name and image
        holder.name.setText(podcast.getName());

        Picasso.get().load(podcast.getCoverUrl()).into(holder.cover);

        // Set click listener on the layout
        holder.layout.setOnClickListener(view -> listenerPodcast.onItemClicked(podcast));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // ViewHolder class to hold the views for each item
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView cover;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views
            name = itemView.findViewById(R.id.podcast_name);
            cover = itemView.findViewById(R.id.podcast_cover_image); // Use the correct ImageView ID
            layout = itemView.findViewById(R.id.podcastclick); // Use the correct LinearLayout ID
        }
    }
}
