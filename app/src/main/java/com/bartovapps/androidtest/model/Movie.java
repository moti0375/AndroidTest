package com.bartovapps.androidtest.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BartovMoti on 01/24/17.
 */
public class Movie implements Parcelable {

    long id;
    String title;
    String overview;
    String release_date;
    long duration;
    String homepage;
    String imageUrl;
    double rating;


    public Movie() {
    }

    public Movie(String title, String overview, String release_date, long duration, double rating, String imageUrl) {
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.duration = duration;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    public Movie(Bundle b){
        if(b != null){
            this.title = b.getString("title");
            this.overview = b.getString("overview");
            this.release_date = b.getString("release_date");
            this.rating = b.getDouble("rating");
            this.duration = b.getLong("duration");
            this.imageUrl = b.getString("imageUrl");
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", release_date='" + release_date + '\'' +
                ", rating='" + rating + '\'' +
                ", duration=" + duration +
                ", homepage='" + homepage + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
        dest.writeLong(this.duration);
        dest.writeString(this.homepage);
        dest.writeString(this.imageUrl);
    }

    protected Movie(Parcel in) {
        this.title = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
        this.duration = in.readLong();
        this.homepage = in.readString();
        this.imageUrl = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Bundle toBundle(){
        Bundle b = new Bundle();

        b.putString("title", this.title);
        b.putString("overview", this.overview);
        b.putString("release_date", this.release_date);
        b.putLong("duration", this.duration);
        b.putDouble("rating", this.rating);
        b.putString("imageUrl", this.imageUrl);

        return b;
    }
}
