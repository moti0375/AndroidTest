package com.bartovapps.androidtest.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by bam059 on 1/25/2017.
 */
public class MoviesProvider extends ContentProvider{

    public static final String AUTHORITY = "com.bartovapps.androidtest.data.moviesprovider";
    public static final String BASE_PATH = "results";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final int MOVIES = 1;
    public static final int MOVIES_ID = 2;

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        matcher.addURI(AUTHORITY, BASE_PATH, MOVIES);
        matcher.addURI(AUTHORITY, BASE_PATH + "/#", MOVIES_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DbOpenHelper helper = new DbOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        return database.query(DbContract.MoviesEntry.TABLE_NAME, DbOpenHelper.ALL_COLUMNS,
                                                                 selection,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 DbContract.MoviesEntry.COLUMN_ID + " DESC" );

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = database.insert(DbContract.MoviesEntry.TABLE_NAME, null, contentValues);

        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(DbContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        return database.update(DbContract.MoviesEntry.TABLE_NAME, contentValues, selection, selectionArgs);
    }
}
