package com.sak.musicplayer.data;

public class Podcast {
    String name;

    public Podcast() {
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

    public Podcast(String name, String coverUrl) {
        this.name = name;
        this.coverUrl = coverUrl;
    }

    String coverUrl;

}
