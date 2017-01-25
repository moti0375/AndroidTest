package com.bartovapps.androidtest.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bartovapps.androidtest.ApiManager;
import com.bartovapps.androidtest.R;
import com.bartovapps.androidtest.adpaters.MoviesRecyclerAdapter;
import com.bartovapps.androidtest.data.MoviesDataSource;
import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.parsers.MoviesJsonParser;
import com.bartovapps.androidtest.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFeedFragment extends Fragment implements MoviesRecyclerAdapter.AdapterEventListener{

    public static final String TAG = MoviesFeedFragment.class.getSimpleName();
    RecyclerView rvMoviesFeed;
    MoviesRecyclerAdapter moviesRecyclerAdapter;
    List<Movie> movies = new ArrayList<>();
    FragmentEventListener mFragmentEventListener;

    public MoviesFeedFragment() {
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
        moviesRecyclerAdapter = new MoviesRecyclerAdapter(getActivity(), movies);
        moviesRecyclerAdapter.setAdapterEventListener(this);
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

        Uri uri = Utils.buildRestQueryString(ApiManager.POPULAR_SEARCH);
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
                        refreshDisplay();
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
        MoviesDataSource dataSource = new MoviesDataSource(getActivity());
        dataSource.open();
        movies = dataSource.findAll();
        dataSource.close();
        Log.i(TAG, "About to refresh display with " + movies.size() + " items");
        moviesRecyclerAdapter.updateList(movies);
    }

    @Override
    public void itemClicked(Movie movie) {
        Log.i(TAG, "itemClicked: " + movie);
        mFragmentEventListener.onFragmentEvent(movie);
    }

    public interface FragmentEventListener{
       void onFragmentEvent(Movie movie);
    }

    private void addMoviesToDb(List<Movie> movies){
        MoviesDataSource dataSource = new MoviesDataSource(getActivity());
        dataSource.open();
        dataSource.deleteAll();
        dataSource.addAll(movies);
        dataSource.close();
    }
}
