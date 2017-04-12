package com.bartovapps.androidtest.movies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;

import com.bartovapps.androidtest.api.ApiClient;
import com.bartovapps.androidtest.api.ApiHelper;
import com.bartovapps.androidtest.api.RetrofitService;
import com.bartovapps.androidtest.data.DbContract;
import com.bartovapps.androidtest.data.MoviesProvider;
import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.model.SearchResponse;
import com.google.gson.Gson;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by motibartov on 22/03/2017.
 */

public class MoviesPresenter implements MoviesContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MoviesPresenter";
    private static final int LOADER_ID = 100;
    private MoviesContract.View movieView;
    private Context mContext;
    private LoaderManager mLoaderManager;
    private List<Movie> mMovies;
    private Cursor mCursor;

    private Subscription mSubscription;
    private ApiClient mApiClient;
    private Observable<SearchResponse> mSearchObservable;


    public MoviesPresenter(Context context, LoaderManager loaderManager, MoviesContract.View movieView) {
        Log.i(TAG, "MoviesPresenter created..");
        mApiClient = new ApiClient();
        this.movieView = movieView;
        mContext = context;
        movieView.setPresenter(this);
        mLoaderManager = loaderManager;
        mLoaderManager.initLoader(LOADER_ID, null, this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void loadMovies(String search, boolean forceUpdate) {
        mLoaderManager.restartLoader(LOADER_ID, null, MoviesPresenter.this);

        if (forceUpdate) {
            Log.i(TAG, "loadMovies: Retrofit...");
            getDataWithRetrofit(search);
        } else {
            mLoaderManager.restartLoader(LOADER_ID, null, MoviesPresenter.this);
        }

    }

    @Override
    public void movieItemClicked(View v, int position) {
        if (mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToPosition(position);

            long movieId = mCursor.getLong(mCursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_API_ID));
            Log.i(TAG, "onItemClick: movie " + movieId + " was clicked");
            movieView.showMovieDetails(v, movieId);
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


    private void getDataWithRetrofit(String search) {
        Log.i(TAG, "getDataWithRetrofit called");
        RetrofitService apiService = mApiClient.getApiService();

        mSearchObservable = apiService.searchMovies(search, ApiHelper.TMDB_API_KEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

        mSubscription = mSearchObservable.subscribe((searchResponse)->{
            Log.i(TAG, "onNext: I've got your search response Asshole!!");
            mMovies = searchResponse.results;
            Log.i(TAG, "Got " + mMovies.size() + " movies");
            addMoviesToDb(mMovies);
            mLoaderManager.restartLoader(LOADER_ID, null, MoviesPresenter.this);
        }, (e)->{}, () -> {});


    }

    private void addMoviesToDb(List<Movie> movies) {
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
