package com.bartovapps.androidtest.movies_details;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.model.Trailer;
import com.bartovapps.androidtest.utils.Utils;
import com.google.gson.Gson;

/**
 * Created by motibartov on 23/03/2017.
 */

public class DetailsPresenter implements DetailsContract.Presenter {
    private static final String TAG = "DetailsPresenter";

    Context mContext;
    Gson gson;
    Movie mMovie;
    DetailsContract.View detailsView;

    public DetailsPresenter(Context context, DetailsContract.View view){
        Log.i(TAG, "DetailsPresenter created");
        mContext = context;
        detailsView = view;
        gson = new Gson();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void onTrailerItemClicked(Trailer trailer) {

    }

    @Override
    public void loadMovieDetails(long movie_api_id) {

        getDataWithVolley(Utils.buildMovieInfoQueryString(movie_api_id));

    }

    public void getDataWithVolley(Uri url) {
        Log.i(TAG, "About the get data with Volley, Uri: " + url.toString());
        RequestQueue queue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Volley onResponse: " + response);

                        mMovie = gson.fromJson(response, Movie.class);
                        Log.i(TAG, "Trailer Search Response: " + mMovie.toString());
                        detailsView.showMovieDetails(mMovie);
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
}
