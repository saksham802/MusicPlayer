package com.sak.musicplayer.spotifyapi;

import com.sak.musicplayer.spotifysearchapi.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpotifyApiService {

    @GET("playlists/{playlist_id}")
    Call<SpotifyPlaylistResponse> getPlaylist(@Path("playlist_id") String playlistId,
                                              @Header("Authorization") String authorization);

    @GET("search")
    Call<SearchResponse> searchTracks(
            @Query("q") String query,
            @Query("type") String type,
            @Query("limit") int limit,
            @Header("Authorization") String authorization
    );

    // Optional: you might want to add a method to handle different search types
    @GET("search")
    Call<SearchResponse> search(
            @Query("q") String query,
            @Query("type") String type,
            @Query("market") String market,
            @Query("limit") int limit,
            @Header("Authorization") String authorization
    );
}
