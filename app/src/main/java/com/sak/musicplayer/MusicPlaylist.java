package com.sak.musicplayer;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sak.musicplayer.spotifyapi.PlaylistAdapter;
import com.sak.musicplayer.spotifyapi.SelectListnerSpotify;
import com.sak.musicplayer.spotifyapi.SpotifyApiService;
import com.sak.musicplayer.spotifyapi.SpotifyAuth;
import com.sak.musicplayer.spotifyapi.SpotifyPlaylistResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicPlaylist extends Fragment implements SelectListnerSpotify {
    private static final String BASE_URL = "https://api.spotify.com/v1/";
    private SpotifyApiService apiService;
    private RecyclerView playlistRecyclerView;
    private PlaylistAdapter playlistAdapter;
    private TextView playlistName,nameofsong;
    private ImageView playlistImage, goback,imgofsong;
    private String playlistId = "", name = "";
    private ConstraintLayout constraintLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_playlist, container, false);

        // Initialize RecyclerView
        goback = view.findViewById(R.id.backbtn);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.playlist, new HomeFrag());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        playlistRecyclerView = view.findViewById(R.id.playlistrecycle);
        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        playlistName = view.findViewById(R.id.nameofplaylist);
        playlistImage = view.findViewById(R.id.artofmusic);
        LayoutInflater navflatter = getLayoutInflater();
        View Nav_view = inflater.inflate(R.layout.activity_main, null);
        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("Name", "");
            String link = bundle.getString("topimg", "");

            Log.d("ImageURL", "Loading image from URL: " + link);
            Picasso.get().load(link).into(playlistImage);
            playlistName.setText(name);
        }

        playlistId = getPlaylistIdTop(name);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(SpotifyApiService.class);
        SpotifyAuth.getAccessToken(new SpotifyAuth.AccessTokenCallback() {
            @Override
            public void onSuccess(String accessToken) {
                fetchPlaylist(playlistId, accessToken);
            }

            @Override
            public void onError(Exception e) {
                Log.e("SpotifyAuth", "Error: " + e.getMessage());
                showToast("Error fetching access token");
            }
        });

        return view;
    }

    private String getPlaylistIdTop(String playlistName) {
        switch (playlistName) {
            case "All Time Top 50": return "4stlIpoPS7uKCsmUA7D8KZ";
            case "Hindi Top 50": return "37i9dQZEVXbLZ52XmnySJg";
            case "Top 50 Bhakti": return "3x2T9XfomkMqiLYfuoW30q";
            case "Electronic Top 30": return "3tRhisNDv5YZXPQltBbJNc";
            case "International Top 50": return "37i9dQZEVXbMDoHDwVN2tF";
            case "Punjabi Top 50": return "5vGMsjreMahQxS4tk6wLi6";
            case "Top 50 Rap": return "28jmBP66fq7vAzcXlyJvZU";
            case "Podcast" :return "37i9dQZF1DX4Glla9HCJfl";
            case "Inspiration" :return"37i9dQZF1EIeMOnMimNdXO";
            case "Romantic" :return "37i9dQZF1DX14CbVHtvHRB";
            case "Sad" :return "2sOMIgioNPngXojcOuR4tn";
            case "Workout Mood" :return "37i9dQZF1EIhiCgCg8FX5u";
            case "Bollywood Mood" :return "37i9dQZF1EIgIRUt8ro3FU";
            case "Old is Gold" :return "37i9dQZF1DWYRTlrhMB12D";
            case "70's Love Hits" :return "37i9dQZF1DX57WIZsVQSIn";
            case "Classic Safar Hits" :return "37i9dQZF1DXcW9e9oxFsr1";
            case "Punjabi Legacy" :return "37i9dQZF1DWU9w5Zm8nbx8";
            case "80's Tamil Hits" :return "37i9dQZF1DX99RE1bJcD5N";
            case "Best Of Comedy Podcast" :return "7bgPapPFqfWcgnUCVtXynD";
            case "Indian Podcast" :return "5BWM8ix0K8t2tZV4xfESvg";
            case "Medical Podcast": return "388WLn7LZ7arccsLwzhGeo";
            case "Mental Health Podcast": return "77AOjGgwOTmcDiH15lARCh";
            case "Raw and Real Podcast":return  "5JswDqiPuzHjfXmn5sZeiF";
            case "Andrew Tate Podcast" :return "7cZO7qbB2Z0DVgu1p8Qv50";
            case "Best TED Talks" : return "797iEBtd9Kwrj2FfcluQa7";
            case "ColdPlay" :return "08xbLKPX6hILf7tDiWWSkt";
            case "Post Malone" :return "2yq3unySgZzQ9lnwCygLmR";
            case "Tylor Swift":return "7L6mdgEKVTpXlr4UCuG6dt";
            case "Arjit Singh" :return "37i9dQZF1EIUrvRct3yQWs";
            case "Diljit" :return "37i9dQZF1EIZNOFIsiFGWV";
            case "sidhu moose wala" :return "37i9dQZF1EIWYlyFTYAKYk";
            default:
                Log.w("Playlist", "Unknown playlist name: " + playlistName);
                return "";
        }
    }

    private void fetchPlaylist(String playlistId, String accessToken) {
        Log.d("Spotify", "Fetching Playlist with Token: " + accessToken);

        String authHeader = "Bearer " + accessToken;

        apiService.getPlaylist(playlistId, authHeader).enqueue(new Callback<SpotifyPlaylistResponse>() {
            @Override
            public void onResponse(Call<SpotifyPlaylistResponse> call, Response<SpotifyPlaylistResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Spotify", "Playlist retrieved successfully");
                    List<SpotifyPlaylistResponse.Item> playlistItems = response.body().tracks.items;

                    // Check if the playlistRecyclerView is initialized before setting the adapter
                    if (playlistRecyclerView != null) {
                        playlistAdapter = new PlaylistAdapter(playlistItems, new SelectListnerSpotify() {
                            @Override
                            public void onItemClicked(SpotifyPlaylistResponse.Item item) {
                                String previewUrl = item.track.preview_url;
                                String albumImageUrl = (item.track.album.images == null || item.track.album.images.isEmpty()) ? "" : item.track.album.images.get(0).url;
                                String singer = item.track.artists != null && !item.track.artists.isEmpty()
                                        ? item.track.artists.get(0).name : "Unknown Artist";

                                Bundle bundle = new Bundle();
                                bundle.putString("Name", item.track.name);
                                bundle.putString("Url", previewUrl);
                                bundle.putString("coverUrl",albumImageUrl);
                                bundle.putString("Singer",singer);


                                MusicPlayer musicPlayerFragment = new MusicPlayer();
                                musicPlayerFragment.setArguments(bundle);

                                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                transaction.replace(R.id.playlist, musicPlayerFragment);  // Correct container ID
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        });
                        playlistRecyclerView.setAdapter(playlistAdapter);
                    }
                } else {
                    Log.e("Spotify", "Failed to retrieve playlist: " + response.code() + " " + response.message());
                    showToast("Failed to retrieve playlist");
                }
            }

            @Override
            public void onFailure(Call<SpotifyPlaylistResponse> call, Throwable t) {
                Log.e("Spotify", "Error fetching playlist: " + t.getMessage());
                showToast("Error fetching playlist");
            }
        });
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClicked(SpotifyPlaylistResponse.Item item) {
        // Not used here, handled in the adapter
    }
}
