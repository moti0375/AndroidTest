package com.bartovapps.androidtest.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bartovapps.androidtest.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BartovMoti on 01/25/17.
 */
public class MoviesDataSource {
    public static final String TAG = MoviesDataSource.class.getSimpleName();

    SQLiteOpenHelper dbHelper;
    SQLiteDatabase database;


    public MoviesDataSource(Context context) {
        dbHelper = new DbOpenHelper(context);
    }

    public void open() {
//		Log.i(LOG_TAG, "Database opened");
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
//		Log.i(LOG_TAG, "Database closed");
        dbHelper.close();
    }

    public Movie insert(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(DbContract.MoviesEntry.COLUMN_TITLE, movie.title);
        values.put(DbContract.MoviesEntry.COLUMN_RELEASED, movie.release_date);
        values.put(DbContract.MoviesEntry.COLUMN_RUNTIME, movie.runtime);
        values.put(DbContract.MoviesEntry.COLUMN_OVERVIEW, movie.overview);
        values.put(DbContract.MoviesEntry.COLUMN_RATING, movie.vote_average);
        long insertId = database.insert(DbContract.MoviesEntry.TABLE_NAME, null, values);
        movie.setSqlId(insertId);
        Log.i(TAG, "inserted:  " + movie.toString());

        return movie;
    }


    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();
        Cursor cursor;
        try {
            cursor = database.query(DbContract.MoviesEntry.TABLE_NAME, DbOpenHelper.ALL_COLUMNS,
                    null, null, null, null, DbContract.MoviesEntry.COLUMN_ID + " DESC");
        } catch (SQLiteException e) {
            e.printStackTrace();
//            Log.i(LOG_TAG, "There was an SQLite exception: " + e.getMessage());

            return movies;
        }

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                Movie movie = new Movie();
             //   movie.setSqlId(cursor.getLong(cursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_ID)));
                movie.title = (cursor.getString(cursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_TITLE)));
                movie.release_date = (cursor.getString(cursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_RELEASED)));
                movie.runtime = (cursor.getLong(cursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_RUNTIME)));
                movie.overview = (cursor.getString(cursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_OVERVIEW)));
                movie.vote_average = (cursor.getDouble(cursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_RATING)));
                movie.poster_path = (cursor.getString(cursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_IMAGE_URI)));

                movies.add(movie);

                Log.i(TAG, "movie added: " + movie.toString());
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return movies;
    }

    public List<Movie> addAll(List<Movie> movies) {
        ContentValues values = new ContentValues();
        for (Movie movie : movies) {
            values.clear();
            values.put(DbContract.MoviesEntry.COLUMN_TITLE, movie.title);
            values.put(DbContract.MoviesEntry.COLUMN_RELEASED, movie.release_date);
            values.put(DbContract.MoviesEntry.COLUMN_RUNTIME, movie.runtime);
            values.put(DbContract.MoviesEntry.COLUMN_OVERVIEW, movie.overview);
            values.put(DbContract.MoviesEntry.COLUMN_RATING, movie.vote_average);
            values.put(DbContract.MoviesEntry.COLUMN_IMAGE_URI, movie.poster_path);
            long insertId = database.insert(DbContract.MoviesEntry.TABLE_NAME, null, values);
        }

//        flower.setProductId((int) insertId);
//		Log.i(LOG_TAG, "Trip created, map: " + trip.getKml());

        return movies;
    }

    public int deleteAll() {

        int result = database.delete(DbContract.MoviesEntry.TABLE_NAME, null, null);
        Log.i(TAG, "delete All result: " + result);

        return result;
    }

}

