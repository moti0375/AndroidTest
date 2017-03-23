package com.bartovapps.androidtest.movies;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bartovapps.androidtest.data.DbContract;
import com.bartovapps.androidtest.data.MoviesProvider;
import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.model.SearchResponse;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by motibartov on 22/03/2017.
 */

public class MoviesPresenter implements MoviesContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MoviesPresenter";
    private static final int LOADER_ID = 100;
    MoviesContract.View movieView;
    Context mContext;
    LoaderManager mLoaderManager;
    Gson gson;
    List<Movie> movies;
    Cursor mCursor;

    public MoviesPresenter(Context context, LoaderManager loaderManager, MoviesContract.View movieView) {
        Log.i(TAG, "MoviesPresenter created..");
        this.movieView = movieView;
        mContext = context;
        movieView.setPresenter(this);
        mLoaderManager = loaderManager;
        mLoaderManager.initLoader(LOADER_ID, null, this);
        gson = new Gson();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadMovies(Uri uri) {
        Log.i(TAG, "loadMovies called");
        getDataWithVolley(uri);
    }

    @Override
    public void movieItemClicked(int position) {
        if(mCursor != null && mCursor.getCount() > 0){
            mCursor.moveToPosition(position);

            long movieId = mCursor.getLong(mCursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_API_ID));
            Log.i(TAG, "onItemClick: movie " + movieId + " was clicked");
            movieView.showMovieDetails(movieId);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "onCreateLoader");

        return new CursorLoader(mContext, MoviesProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(TAG, "onLoadFinished");
        mCursor = data;
        movieView.showMovies(mCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> data) {
        Log.i(TAG, "onLoaderReset");
        movieView.showMovies(null);
        mCursor = null;
    }



    public void getDataWithVolley(Uri url) {
        Log.i(TAG, "getDataWithVolley");
        RequestQueue queue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Volley onResponse: " + response);

//                        response = Utils.getJsonContentFromFlickerRespose(response);
                        // Display the first 500 characters of the response string.
                        //results = MoviesJsonParser.parseFeed(response);
                        SearchResponse searchResponse = gson.fromJson(response, SearchResponse.class);
                        Log.i(TAG, "Search Response: " + searchResponse.toString());

                        movies = searchResponse.results;
                        Log.i(TAG, "Got " + movies.size() + " results: " + movies.toString());

                        addMoviesToDb(movies);
                        if (mLoaderManager == null) {
                            Log.i(TAG, "Loader is null..");
                        } else {
                            mLoaderManager.restartLoader(LOADER_ID, null, MoviesPresenter.this);
                        }

                        //refreshDisplay();
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Volley onErrorResponse: " + error);
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void addMoviesToDb(List<Movie> movies) {
//        MoviesDataSource dataSource = new MoviesDataSource(getActivity());
//        dataSource.open();
//        dataSource.deleteAll();
//        dataSource.addAll(results);
//        dataSource.close();
        mContext.getContentResolver().delete(MoviesProvider.CONTENT_URI, null, null);

        for (Movie movie : movies) {
            ContentValues values = new ContentValues();
            values.put(DbContract.MoviesEntry.COLUMN_API_ID, movie.id);
            values.put(DbContract.MoviesEntry.COLUMN_TITLE, movie.title);
            values.put(DbContract.MoviesEntry.COLUMN_OVERVIEW, movie.overview);
            values.put(DbContract.MoviesEntry.COLUMN_RELEASED, movie.release_date);
            values.put(DbContract.MoviesEntry.COLUMN_RUNTIME, movie.runtime);
            values.put(DbContract.MoviesEntry.COLUMN_RATING, movie.vote_average);
            values.put(DbContract.MoviesEntry.COLUMN_IMAGE_URI, movie.poster_path);

            mContext.getContentResolver().insert(MoviesProvider.CONTENT_URI, values);
        }

    }
}
