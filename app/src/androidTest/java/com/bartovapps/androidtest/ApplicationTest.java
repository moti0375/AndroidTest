package com.bartovapps.androidtest;

import android.app.Application;
import android.content.ContentValues;
import android.net.Uri;
import android.test.ApplicationTestCase;

import com.bartovapps.androidtest.data.DbContract;
import com.bartovapps.androidtest.data.MoviesProvider;
import com.bartovapps.androidtest.model.Movie;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);

           Movie movie = new Movie(100, "Hagiga Basnooker", "Funny movie", "1975", 120, 9.5, null);

            ContentValues values = new ContentValues();
            values.put(DbContract.MoviesEntry.COLUMN_TITLE, movie.getTitle());
            values.put(DbContract.MoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
            values.put(DbContract.MoviesEntry.COLUMN_RELEASED, movie.getRelease_date());
            values.put(DbContract.MoviesEntry.COLUMN_DURATION, movie.getDuration());
            values.put(DbContract.MoviesEntry.COLUMN_RATING, movie.getRating());
            values.put(DbContract.MoviesEntry.COLUMN_IMAGE_URI, movie.getImageUrl());

            Uri movieUri =  mContext.getContentResolver().insert(MoviesProvider.CONTENT_URI, values);

            assertTrue(movieUri != null);
    }
}