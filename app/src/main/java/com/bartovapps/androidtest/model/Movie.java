package com.bartovapps.androidtest.model;

import android.os.Bundle;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by BartovMoti on 01/24/17.
 */
public class Movie {

    long sqlId;

    @SerializedName("id")
    public long id;

    @SerializedName(value = "name", alternate = {"title"})
    public String title;

    @SerializedName("overview")
    public String overview;

    @SerializedName("release_date")
    public String release_date;

    @SerializedName("runtime")
    public long runtime;

    @SerializedName("poster_path")
    public String poster_path;

    @SerializedName("vote_average")
    public double vote_average;

    public List<Trailer> trailers;

    public long getSqlId() {
        return sqlId;
    }

    public void setSqlId(long sqlId) {
        this.sqlId = sqlId;
    }

    @SerializedName("videos")
    public Videos videos;

    public class Videos{
        public List<Trailer> results;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "sqlId=" + sqlId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", release_date='" + release_date + '\'' +
                ", runtime=" + runtime +
                ", poster_path='" + poster_path + '\'' +
                ", vote_average=" + vote_average +
                '}';
    }
}
