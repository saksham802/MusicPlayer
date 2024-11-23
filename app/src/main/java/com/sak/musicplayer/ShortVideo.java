package com.sak.musicplayer;

public class ShortVideo {
    String title;
    String desc;

    public ShortVideo(String title, String desc, String videoUrl) {
        this.title = title;
        this.desc = desc;
        this.videoUrl = videoUrl;
    }


    public ShortVideo() {
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String videoUrl;

}
