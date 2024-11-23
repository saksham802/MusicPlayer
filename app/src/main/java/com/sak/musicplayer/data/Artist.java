package com.sak.musicplayer.data;

public class Artist {
    public Artist(String coverUrl, String name) {
        this.coverUrl = coverUrl;
        this.name = name;
    }

    public Artist() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    String name,coverUrl;
}
