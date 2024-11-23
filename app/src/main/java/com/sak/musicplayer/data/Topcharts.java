package com.sak.musicplayer.data;

public class Topcharts {
    public Topcharts(String name, String coverUrl) {
        this.name = name;
        this.coverUrl = coverUrl;
    }

    public Topcharts() {
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
