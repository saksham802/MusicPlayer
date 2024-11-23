package com.sak.musicplayer;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sak.musicplayer.spotifysearchapi.SearchResponse;
import com.sak.musicplayer.spotifyapi.SpotifyApiService;
import com.sak.musicplayer.spotifyapi.SpotifyAuth;
import com.sak.musicplayer.spotifysearchapi.MusicAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchPage extends AppCompatActivity implements SelectListnerSearch {

    private EditText searchBar;
    private RecyclerView recyclerView;
    private MusicAdapter musicAdapter;
    private SpotifyApiService spotifyApiService;
    private String accessToken;
    private ProgressBar progressBar;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        searchBar = findViewById(R.id.search_bar);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progressBar);
        back = findViewById(R.id.backonhome);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.GONE);  // Initially hidden
        musicAdapter = new MusicAdapter(null, this);
        recyclerView.setAdapter(musicAdapter);


        back.setOnClickListener(view -> onBackPressed());


        initSpotifyApiService();
        fetchAccessToken();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2 && accessToken != null) {
                    searchTracks(s.toString());
                } else {
                    musicAdapter.updateData(null);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void initSpotifyApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        spotifyApiService = retrofit.create(SpotifyApiService.class);
    }

    private void fetchAccessToken() {
        SpotifyAuth.getAccessToken(new SpotifyAuth.AccessTokenCallback() {
            @Override
            public void onSuccess(String token) {
                accessToken = token;  // Store the access token
                Log.d("SpotifyAuth", "Access Token: " + accessToken);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(SearchPage.this, "Error fetching access token", Toast.LENGTH_SHORT).show();
                Log.e("SpotifyAuth", "Error: " + e.getMessage());
            }
        });
    }

    private void searchTracks(String query) {
        if (accessToken == null) {
            Toast.makeText(SearchPage.this, "Access token not available", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            Log.d("SpotifySearch", "Searching for: " + encodedQuery);
            String authHeader = "Bearer " + accessToken;

            spotifyApiService.searchTracks(encodedQuery, "track", 10, authHeader).enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.isSuccessful() && response.body() != null) {
                        List<SearchResponse.Track> tracks = response.body().getTracks().getItems();
                        updateRecyclerView(tracks);
                    } else {
                        Log.e("SpotifySearch", "Search failed: " + response.message() + " Code: " + response.code());
                        Toast.makeText(SearchPage.this, "Search failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SearchResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE); // Hide loading indicator
                    Log.e("SpotifySearch", "Error: " + t.getMessage(), t);
                    Toast.makeText(SearchPage.this, "Error fetching data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (UnsupportedEncodingException e) {
            Log.e("SpotifySearch", "Error encoding query: " + e.getMessage());
            Toast.makeText(SearchPage.this, "Error processing search query", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE); // Hide loading indicator
        }
    }

    private void updateRecyclerView(List<SearchResponse.Track> tracks) {
        if (tracks != null && !tracks.isEmpty()) {
            musicAdapter.updateData(tracks);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(SearchPage.this, "No results found", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);  // Hide RecyclerView if no results
        }
    }

    @Override
    public void onItemClick(SearchResponse.Track track) {
        String albumImageUrl = track.getAlbum() != null ? track.getAlbum().getName() : "No Image";
        String singer = track.getArtists() != null && !track.getArtists().isEmpty()
                ? track.getArtists().get(0).getName() : "Unknown Artist";

        Bundle bundle = new Bundle();
        bundle.putString("Name", track.getName());
        bundle.putString("Url", track.getPreviewUrl());
        bundle.putString("coverUrl", albumImageUrl);
        bundle.putString("Singer", singer);

        MusicPlayer musicPlayerFragment = new MusicPlayer();
        musicPlayerFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.swicher   , musicPlayerFragment)
                .commit();

    }
}
