package com.sak.musicplayer.spotifyapi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// SpotifyPlaylistResponse.java

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SpotifyPlaylistResponse {
    @SerializedName("tracks")
    public TrackList tracks;

    public static class TrackList {
        @SerializedName("items")
        public List<Item> items;
    }

    public static class Item {
        @SerializedName("track")
        public Track track;
    }

    public static class Track {
        @SerializedName("name")
        public String name;

        @SerializedName("preview_url")
        public String preview_url;  // Ensure this is being populated properly

        @SerializedName("artists")
        public List<Artist> artists;

        @SerializedName("album")
        public Album album;

        @SerializedName("url")
        public String url;
    }
    public static class Artist {
        @SerializedName("name")
        public String name;
    }

    public static class Album {
        @SerializedName("images")
        public List<Image> images;
    }

    public static class Image {
        @SerializedName("url")
        public String url;
    }
}
