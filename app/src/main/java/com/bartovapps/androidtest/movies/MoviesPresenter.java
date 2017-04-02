package com.bartovapps.androidtest.movies;

import android.content.ContentValues;
import android.content.Context;
import android.content.ServiceConnection;
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
import com.bartovapps.androidtest.R;
import com.bartovapps.androidtest.api.ApiClient;
import com.bartovapps.androidtest.api.ApiHelper;
import com.bartovapps.androidtest.api.ApiInterface;
import com.bartovapps.androidtest.data.DbContract;
import com.bartovapps.androidtest.data.MoviesProvider;
import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.model.SearchResponse;
import com.bartovapps.androidtest.utils.Utils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import rx.Observable;
import rx.Subscriber;
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
    private Gson gson;
    private List<Movie> mMovies;
    private Cursor mCursor;
    private int mClientApi;
    private int retrofit;
    private int volley;

    private Subscription subscription;


    public MoviesPresenter(Context context, LoaderManager loaderManager, MoviesContract.View movieView) {
        Log.i(TAG, "MoviesPresenter created..");
        this.movieView = movieView;
        mContext = context;
        movieView.setPresenter(this);
        mLoaderManager = loaderManager;
        mLoaderManager.initLoader(LOADER_ID, null, this);
        gson = new Gson();
        retrofit = mContext.getResources().getInteger(R.integer.Retrofit);
        volley = mContext.getResources().getInteger(R.integer.Volley);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }

    @Override
    public void loadMovies(String search, boolean forceUpdate) {
        mLoaderManager.restartLoader(LOADER_ID, null, MoviesPresenter.this);

        if (forceUpdate) {
            Log.i(TAG, "loadMovies called, apiClient = " + mClientApi);
            if (mClientApi == mContext.getResources().getInteger(R.integer.Retrofit)) {
                Log.i(TAG, "loadMovies: Retrofit...");
                getDataWithRetrofit(search);
            } else if (mClientApi == mContext.getResources().getInteger(R.integer.Volley)) {
                Log.i(TAG, "loadMovies: Volley...");
                getDataWithVolley(search);
            } else if (mClientApi == mContext.getResources().getInteger(R.integer.OkHttp)) {
                Log.i(TAG, "loadMovies: OkHttp...");
                subscription = getObservable(Utils.buildRestQueryString(search))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<SearchResponse>() {
                            @Override
                            public void onCompleted() {
                                Log.i(TAG, "Observable onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "Observable onError: " + e.getMessage());
                            }

                            @Override
                            public void onNext(SearchResponse searchResponse) {
                                mMovies = searchResponse.results;
                                Log.i(TAG, "onNext called, got " + mMovies.size() + " movies");
                                addMoviesToDb(mMovies);
                                mLoaderManager.restartLoader(LOADER_ID, null, MoviesPresenter.this);
                            }
                        });
                // getMoviesWithOkHttp(search);
            } else {
                Log.i(TAG, "loadMovies: Default...");
                getDataWithVolley(search);
            }
        }

    }

    @Override
    public void movieItemClicked(int position) {
        if (mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToPosition(position);

            long movieId = mCursor.getLong(mCursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_API_ID));
            Log.i(TAG, "onItemClick: movie " + movieId + " was clicked");
            movieView.showMovieDetails(movieId);
        }
    }

    @Override
    public void setApiClient(int client) {
        mClientApi = client;
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


    private void getDataWithVolley(String search) {

        Uri uri = Utils.buildRestQueryString(search);
        Log.i(TAG, "getDataWithVolley, req uri: " + uri.toString());

        RequestQueue queue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri.toString(),
                response -> {
                    Log.i(TAG, "Volley onResponse: " + response);

//                        response = Utils.getJsonContentFromFlickerRespose(response);
                    // Display the first 500 characters of the response string.
                    //results = MoviesJsonParser.parseFeed(response);
                    SearchResponse searchResponse = gson.fromJson(response, SearchResponse.class);
                    Log.i(TAG, "Search Response: " + searchResponse.toString());

                    mMovies = searchResponse.results;
                    Log.i(TAG, "Got " + mMovies.size() + " results: " + mMovies.toString());

                    addMoviesToDb(mMovies);
                    if (mLoaderManager == null) {
                        Log.i(TAG, "Loader is null..");
                    } else {
                        mLoaderManager.restartLoader(LOADER_ID, null, MoviesPresenter.this);
                    }

                    //refreshDisplay();
                }, error -> Log.i(TAG, "Volley onErrorResponse: " + error));
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getDataWithRetrofit(String search) {
        Log.i(TAG, "getDataWithRetrofit called");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SearchResponse> call = apiService.searchMovies(search, ApiHelper.TMDB_API_KEY);
        Log.i(TAG, "call url: " + call.request().url());

        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, retrofit2.Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();
                mMovies = searchResponse.results;
                Log.i(TAG, "Retrofit onResponse: got " + mMovies.size() + " movies");
                addMoviesToDb(mMovies);
                if (mLoaderManager == null) {
                    Log.i(TAG, "Loader is null..");
                } else {
                    mLoaderManager.restartLoader(LOADER_ID, null, MoviesPresenter.this);
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.e(TAG, "Retrofit onFailure: " + t.getMessage());
            }
        });


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

    private SearchResponse getMoviesWithOkHttp(Uri uri) throws IOException {
        Log.i(TAG, "getMoviesWithOkHttp uri: " + uri);
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url(uri.toString()).build();
        okhttp3.Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            SearchResponse searchResponse = gson.fromJson(response.body().charStream(), SearchResponse.class);
            return searchResponse;
        }else{
            Log.e(TAG, "getMoviesWithOkHttp error code: " + response.code());
        }
        return null;
    }


    private Observable<SearchResponse> getObservable(final Uri uri) {
        Log.i(TAG, "getObservable was called");
        return Observable.defer(() -> {
            try {
                return Observable.just(getMoviesWithOkHttp(uri));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
}
