package com.bartovapps.androidtest.model;

/**
 * Created by BartovMoti on 01/27/17.
 */
public class Video {

    String title;
    String youTubeKey;

    public Video() {
    }

    public Video(String title, String youTubeKey) {
        this.title = title;
        this.youTubeKey = youTubeKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYouTubeKey() {
        return youTubeKey;
    }

    public void setYouTubeKey(String youTubeKey) {
        this.youTubeKey = youTubeKey;
    }

    @Override
    public String toString() {
        return "Video{" +
                "title='" + title + '\'' +
                ", youTubeKey='" + youTubeKey + '\'' +
                '}';
    }
}
