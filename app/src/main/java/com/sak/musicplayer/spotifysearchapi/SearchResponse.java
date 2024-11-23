package com.sak.musicplayer.spotifysearchapi;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SearchResponse {

    @SerializedName("tracks")
    private TrackContainer tracks;

    public TrackContainer getTracks() {
        return tracks;
    }

    public static class TrackContainer {
        @SerializedName("items")
        private List<Track> items;

        public List<Track> getItems() {
            return items;
        }
    }

    public static class Track {
        @SerializedName("name")
        private String name;

        @SerializedName("artists")
        private List<Artist> artists;

        @SerializedName("preview_url")
        private String previewUrl;

        @SerializedName("album")
        private Album album;

        public String getName() {
            return name;
        }

        public List<Artist> getArtists() {
            return artists;
        }

        public Album getAlbum() {
            return album;
        }

        public String getPreviewUrl() {
            return previewUrl;
        }
    }

    public static class Artist {
        @SerializedName("name")
        private String name;

        public String getName() {
            return name;
        }
    }

    public static class Album {
        @SerializedName("name")
        private String name;

        public String getName() {
            return name;
        }
    }
}
