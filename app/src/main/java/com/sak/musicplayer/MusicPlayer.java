package com.sak.musicplayer;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.squareup.picasso.Picasso;

public class MusicPlayer extends Fragment {
    private ExoPlayer exoPlayer;
    private String songUrl;
    private String songName;
    private String imgUrl;
    private String songsinger;

    private StyledPlayerView playerView;
    private TextView name, singerofsong, nameofsong;
    private ImageView musicPic, imgofsong, back;
    private RelativeLayout relativeLayout;

    public MusicPlayer() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            songName = getArguments().getString("Name", "");
            songUrl = getArguments().getString("Url", "");
            imgUrl = getArguments().getString("coverUrl", "");
            songsinger = getArguments().getString("Singer", "");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);
        name = view.findViewById(R.id.musicname);
        musicPic = view.findViewById(R.id.musicimg);
        playerView = view.findViewById(R.id.playermusic);
        singerofsong = view.findViewById(R.id.author);
        back = view.findViewById(R.id.backtofragment);
        Picasso.get().load(imgUrl).into(musicPic);
        name.setText(songName);
        singerofsong.setText(songsinger);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (exoPlayer != null && exoPlayer.isPlaying()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Name", songName);
                    bundle.putInt("play", 1);
                    bundle.putString("Url", imgUrl);
                    HomeFrag homeFrag = new HomeFrag();
                    homeFrag.setArguments(bundle);
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.replacement, homeFrag);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else{
                    HomeFrag homeFrag = new HomeFrag();
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.replacement, homeFrag);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        exoPlayer = new ExoPlayer.Builder(requireContext()).build();
        playerView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(songUrl);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
