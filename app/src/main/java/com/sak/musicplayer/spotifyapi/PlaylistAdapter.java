package com.sak.musicplayer.spotifyapi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sak.musicplayer.R;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private List<SpotifyPlaylistResponse.Item> items;
    SelectListnerSpotify selectListnerSpotify;

    public PlaylistAdapter(List<SpotifyPlaylistResponse.Item> items, SelectListnerSpotify spotify) {
        this.items = items;
        this.selectListnerSpotify = spotify;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectListnerSpotify.onItemClicked(items.get(position));
            }
        });
        holder.layout.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Playing Preview", Toast.LENGTH_SHORT).show();
            selectListnerSpotify.onItemClicked(items.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView albumImage;
        private TextView trackName;
        private TextView artistName;
        private LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
             // ImageView ID in layout
            trackName = itemView.findViewById(R.id.song_title);
            layout = itemView.findViewById(R.id.songtoplay);
            artistName = itemView.findViewById(R.id.song_artist);
        }

        public void bind(SpotifyPlaylistResponse.Item item) {
            SpotifyPlaylistResponse.Track track = item.track;
            trackName.setText(track.name);

            // Concatenate artist names
            StringBuilder artistsBuilder = new StringBuilder();
            for (int i = 0; i < track.artists.size(); i++) {
                artistsBuilder.append(track.artists.get(i).name);
                if (i < track.artists.size() - 1) {
                    artistsBuilder.append(", ");
                }
            }
            artistName.setText(artistsBuilder.toString());

            // Load album image using Glide

        }
    }
}
