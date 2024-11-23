package com.sak.musicplayer.data;

public class GoldenEra {
    String name;

    public GoldenEra(String name, String coverUrl) {
        this.name = name;
        this.coverUrl = coverUrl;
    }

    public GoldenEra() {
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

    String coverUrl;
}
