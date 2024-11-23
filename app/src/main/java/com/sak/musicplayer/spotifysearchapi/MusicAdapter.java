package com.sak.musicplayer.spotifysearchapi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sak.musicplayer.R;
import com.sak.musicplayer.SelectListnerSearch;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private List<SearchResponse.Track> trackList;
    SelectListnerSearch selectListnerSearch;

    public MusicAdapter(List<SearchResponse.Track> trackList,SelectListnerSearch search) {
        this.trackList = trackList;
        this.selectListnerSearch=search;
    }

    public void updateData(List<SearchResponse.Track> newTracks) {
        this.trackList = newTracks;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_track, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        SearchResponse.Track track = trackList.get(position);
        holder.trackName.setText(track.getName());
        holder.artistName.setText(track.getArtists().get(0).getName());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    selectListnerSearch.onItemClick(track);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (trackList != null) ? trackList.size() : 0;
    }

    static class MusicViewHolder extends RecyclerView.ViewHolder {
        TextView trackName, artistName;
        ConstraintLayout constraintLayout;
        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            trackName = itemView.findViewById(R.id.track_name);
            artistName = itemView.findViewById(R.id.artist_name);
            constraintLayout=itemView.findViewById(R.id.clickablesearch);
        }
    }
}
