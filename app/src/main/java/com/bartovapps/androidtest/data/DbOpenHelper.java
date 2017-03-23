package com.bartovapps.androidtest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by BartovMoti on 01/24/17.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = DbOpenHelper.class.getSimpleName();

    //Constants for db name and version
    private static final String DATABASE_NAME = "results.db";
    private static final int DATABASE_VERSION = 1;

    //Constants for identifying table and columns
    public static final String TABLE_MOVIES = "results";
    public static final String MOVIE_ID = "_id";

    public static final String[] ALL_COLUMNS = {DbContract.MoviesEntry._ID,
            DbContract.MoviesEntry.COLUMN_API_ID,
            DbContract.MoviesEntry.COLUMN_TITLE,
            DbContract.MoviesEntry.COLUMN_RELEASED,
            DbContract.MoviesEntry.COLUMN_RUNTIME,
            DbContract.MoviesEntry.COLUMN_OVERVIEW,
            DbContract.MoviesEntry.COLUMN_RATING,
            DbContract.MoviesEntry.COLUMN_IMAGE_URI};

    //SQL to create table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + DbContract.MoviesEntry.TABLE_NAME + " (" +
                    DbContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DbContract.MoviesEntry.COLUMN_API_ID + " NUMERIC, " +
                    DbContract.MoviesEntry.COLUMN_TITLE + " TEXT, " +
                    DbContract.MoviesEntry.COLUMN_RELEASED + " TEXT, " +
                    DbContract.MoviesEntry.COLUMN_RUNTIME + " NUMERIC, " +
                    DbContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT, " +
                    DbContract.MoviesEntry.COLUMN_RATING + " NUMERIC, " +
                    DbContract.MoviesEntry.COLUMN_IMAGE_URI + " TEXT " + ")";


    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        Log.i(LOG_TAG, "Database was created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
