package com.bartovapps.androidtest.fragments;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bartovapps.androidtest.ApiManager;
import com.bartovapps.androidtest.R;
import com.bartovapps.androidtest.adpaters.MoviesRecyclerAdapter;
import com.bartovapps.androidtest.adpaters.RecyclerItemClickListener;
import com.bartovapps.androidtest.adpaters.SimpleCursorRecyclerAdapter;
import com.bartovapps.androidtest.data.DbContract;
import com.bartovapps.androidtest.data.DbOpenHelper;
import com.bartovapps.androidtest.data.MoviesDataSource;
import com.bartovapps.androidtest.data.MoviesProvider;
import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.parsers.MoviesJsonParser;
import com.bartovapps.androidtest.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFeedFragment extends Fragment implements MoviesRecyclerAdapter.AdapterEventListener, LoaderManager.LoaderCallbacks<Cursor>{

    public static final String TAG = MoviesFeedFragment.class.getSimpleName();
    private static final int LOADER_ID = 1;
    RecyclerView rvMoviesFeed;
    SimpleCursorRecyclerAdapter moviesRecyclerAdapter;
    List<Movie> movies = new ArrayList<>();
    FragmentEventListener mFragmentEventListener;
    Cursor mCursor;
    LoaderManager loaderManager;


    public MoviesFeedFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_feed, container, false);
        setupViews(view);

        return view;
    }

    private void setupViews(View view) {
        rvMoviesFeed = (RecyclerView) view.findViewById(R.id.rvMoviesFeed);
        rvMoviesFeed.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),rvMoviesFeed, new RecyclerItemClickListener.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), position + " Item clicked.. ", Toast.LENGTH_SHORT).show();
                if(mCursor != null && mCursor.getCount() > 0){
                    mCursor.moveToPosition(position);
                    Movie movie = new Movie();
                    movie.setTitle(mCursor.getString(mCursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_TITLE)));
                    movie.setRelease_date(mCursor.getString(mCursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_RELEASED)));
                    movie.setRating(mCursor.getDouble(mCursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_RATING)));
                    movie.setOverview(mCursor.getString(mCursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_OVERVIEW)));
                    movie.setDuration(mCursor.getLong(mCursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_DURATION)));
                    movie.setImageUrl(mCursor.getString(mCursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_IMAGE_URI)));

                    Log.i(TAG, "onItemClick: created movie: " + movie);
                    mFragmentEventListener.onFragmentEvent(movie);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        moviesRecyclerAdapter = new SimpleCursorRecyclerAdapter(getActivity(), null);
        rvMoviesFeed.setAdapter(moviesRecyclerAdapter);
        rvMoviesFeed.setHasFixedSize(true);
//        rvMoviesFeed.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(this, R.drawable.divider_drawable)));
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
//        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return (3 - position % 3);
//            }
//        });

        rvMoviesFeed.setLayoutManager(manager);
//        rvImagesRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, rvImagesRecyclerView, new ClickListener() {
        }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Uri uri = Utils.buildRestQueryString(ApiManager.TOP_RATED_SEARCH);
        Log.i(TAG, "onActivityCreated: api uri: " + uri.toString());
        getDataWithVolley(uri);
    }

    public void setFragmentEventListener(FragmentEventListener fragmentEventListener){
        mFragmentEventListener = fragmentEventListener;
    }

    public void getDataWithVolley(Uri url) {
        Log.i(TAG, "About the get data with Volley");
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Volley onResponse: " + response);

//                        response = Utils.getJsonContentFromFlickerRespose(response);
                        // Display the first 500 characters of the response string.
                        movies = MoviesJsonParser.parseFeed(response);
                        addMoviesToDb(movies);
                        loaderManager.initLoader(LOADER_ID, null, MoviesFeedFragment.this);

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

    private void refreshDisplay() {
//        MoviesDataSource dataSource = new MoviesDataSource(getActivity());
//        dataSource.open();
//        movies = dataSource.findAll();
//        dataSource.close();
        mCursor = getActivity().getContentResolver().query(MoviesProvider.CONTENT_URI, DbOpenHelper.ALL_COLUMNS, null, null, null);
        Log.i(TAG, "About to refresh display with " + mCursor.getCount() + " items");
        moviesRecyclerAdapter.changeCursor(mCursor);
    }

    @Override
    public void itemClicked(Movie movie) {
        Log.i(TAG, "itemClicked: " + movie);
        mFragmentEventListener.onFragmentEvent(movie);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(), MoviesProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        moviesRecyclerAdapter.changeCursor(data);
        mCursor = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        moviesRecyclerAdapter.changeCursor(null);
        mCursor = null;
    }

    public interface FragmentEventListener{
       void onFragmentEvent(Movie movie);
    }

    private void addMoviesToDb(List<Movie> movies){
//        MoviesDataSource dataSource = new MoviesDataSource(getActivity());
//        dataSource.open();
//        dataSource.deleteAll();
//        dataSource.addAll(movies);
//        dataSource.close();
        getActivity().getContentResolver().delete(MoviesProvider.CONTENT_URI, null, null);

        for(Movie movie: movies){
            ContentValues values = new ContentValues();
            values.put(DbContract.MoviesEntry.COLUMN_TITLE, movie.getTitle());
            values.put(DbContract.MoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
            values.put(DbContract.MoviesEntry.COLUMN_RELEASED, movie.getRelease_date());
            values.put(DbContract.MoviesEntry.COLUMN_DURATION, movie.getDuration());
            values.put(DbContract.MoviesEntry.COLUMN_RATING, movie.getRating());
            values.put(DbContract.MoviesEntry.COLUMN_IMAGE_URI, movie.getImageUrl());

            getActivity().getContentResolver().insert(MoviesProvider.CONTENT_URI, values);
        }

    }

}
