package com.bartovapps.androidtest.movies;


import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bartovapps.androidtest.api.ApiHelper;
import com.bartovapps.androidtest.R;
import com.bartovapps.androidtest.adpaters.RecyclerItemClickListener;
import com.bartovapps.androidtest.adpaters.SimpleCursorRecyclerAdapter;
import com.bartovapps.androidtest.data.DbContract;
import com.bartovapps.androidtest.data.DbOpenHelper;
import com.bartovapps.androidtest.data.MoviesProvider;
import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.model.SearchResponse;
import com.bartovapps.androidtest.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFeedFragment extends Fragment implements  MoviesContract.View{

    public static final String TAG = MoviesFeedFragment.class.getSimpleName();
    private static final int LOADER_ID = 1;
    RecyclerView rvMoviesFeed;
    SimpleCursorRecyclerAdapter moviesRecyclerAdapter;
    List<Movie> movies = new ArrayList<>();
    FragmentEventListener mFragmentEventListener;
    Cursor mCursor;
    LoaderManager loaderManager;
    int mHttpReqMethod = R.integer.HttpUrlConnection;

    SharedPreferences sharedPreferences;
    Gson gson;
    MoviesContract.Presenter mPresenter;


    public MoviesFeedFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loaderManager = getLoaderManager();
      //  loaderManager.initLoader(LOADER_ID, null, this);
        //loaderManager.initLoader(LOADER_ID, null, MoviesFeedFragment.this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        gson = new Gson();
        mPresenter = new MoviesPresenter(getActivity(), loaderManager, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_feed, container, false);
        setupViews(view);

        setHasOptionsMenu(true);
        return view;
    }

    private void setupViews(View view) {
        rvMoviesFeed = (RecyclerView) view.findViewById(R.id.rvMoviesFeed);
        rvMoviesFeed.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),rvMoviesFeed, new RecyclerItemClickListener.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), position + " Item clicked.. ", Toast.LENGTH_SHORT).show();
                mPresenter.movieItemClicked(position);
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

        rvMoviesFeed.setLayoutManager(manager);
//        rvImagesRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, rvImagesRecyclerView, new ClickListener() {
        }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Uri uri = Utils.buildRestQueryString(ApiHelper.API_TOP_RATED);
        Log.i(TAG, "onActivityCreated: api uri: " + uri.toString());
        mPresenter.loadMovies(uri);
    }

    public void setFragmentEventListener(FragmentEventListener fragmentEventListener){
        mFragmentEventListener = fragmentEventListener;
    }



    private void refreshDisplay(Cursor cursor) {
        Log.i(TAG, "About to refresh display with " + cursor.getCount() + " items");
        moviesRecyclerAdapter.changeCursor(cursor);
    }


    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void showMovies(Cursor cursor) {

        refreshDisplay(cursor);
    }

    @Override
    public void showLoadMoviesError() {

    }

    @Override
    public void showNoMovies() {

    }

    @Override
    public void showMovieDetails(long movieId) {
        mFragmentEventListener.onFragmentEvent(movieId);

    }

    public interface FragmentEventListener{
       void onFragmentEvent(long movie_api_id);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected called");
        int itemId = item.getItemId();
        Uri uri;

        switch (itemId){
            case R.id.top_rated:
                uri = Utils.buildRestQueryString(ApiHelper.API_TOP_RATED);
                break;
            case R.id.most_popular:
                uri = Utils.buildRestQueryString(ApiHelper.API_MOST_POPULAR);
                break;
            case R.id.now_playing:
                uri = Utils.buildRestQueryString(ApiHelper.API_NOW_PLAYING);
                break;
            case R.id.upcoming:
                uri = Utils.buildRestQueryString(ApiHelper.API_UPCOMING);
                break;
            default:
                uri = Utils.buildRestQueryString(ApiHelper.API_UPCOMING);
                break;

        }

        mPresenter.loadMovies(uri);

        return true;
    }

    SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            mHttpReqMethod = Integer.parseInt(sharedPreferences.getString(key, "10"));
            Log.i(TAG, "onSharedPreferenceChanged called, changed key: " + key + ", value = " + mHttpReqMethod);
        }
    };


}
