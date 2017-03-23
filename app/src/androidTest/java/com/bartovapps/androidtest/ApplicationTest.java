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

        Movie movie = new Movie();

        movie.id = 100;
        movie.title = "Hagiga Basnooker";
        movie.overview = "Funny movie";
        movie.release_date = "1975";
        movie.runtime = 120;
        movie.vote_average = 9.5;


        ContentValues values = new ContentValues();
        values.put(DbContract.MoviesEntry.COLUMN_TITLE, movie.title);
        values.put(DbContract.MoviesEntry.COLUMN_OVERVIEW, movie.overview);
        values.put(DbContract.MoviesEntry.COLUMN_RELEASED, movie.release_date);
        values.put(DbContract.MoviesEntry.COLUMN_RUNTIME, movie.runtime);
        values.put(DbContract.MoviesEntry.COLUMN_RATING, movie.vote_average);
        values.put(DbContract.MoviesEntry.COLUMN_IMAGE_URI, movie.poster_path);

        Uri movieUri = mContext.getContentResolver().insert(MoviesProvider.CONTENT_URI, values);

        assertTrue(movieUri != null);
    }
}