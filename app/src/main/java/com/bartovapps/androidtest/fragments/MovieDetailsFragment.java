package com.bartovapps.androidtest.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bartovapps.androidtest.R;
import com.bartovapps.androidtest.adpaters.MoviesRecyclerAdapter;
import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.model.Video;
import com.bartovapps.androidtest.parsers.MoviesJsonParser;
import com.bartovapps.androidtest.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailsFragment extends Fragment implements MoviesRecyclerAdapter.MoviesRecyclerItemClickListener{

    public static final String TAG = MovieDetailsFragment.class.getSimpleName();
    ImageView ivMovieImage;
    TextView tvReleased;
    TextView tvOverview;
    TextView tvRating;
    TextView tvDuration;
    MoviesRecyclerAdapter moviesRecyclerAdapter;
    RecyclerView recyclerView;
    Movie movie;
    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if(b != null){
            movie = new Movie(b);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        setViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setViews(View view) {
        ivMovieImage = (ImageView) view.findViewById(R.id.ivDetailedImage);
        tvReleased = (TextView) view.findViewById(R.id.tvReleased);
        tvOverview = (TextView) view.findViewById(R.id.tvOverview);
        tvRating = (TextView) view.findViewById(R.id.tvRating);
        tvDuration = (TextView) view.findViewById(R.id.tvDuration);

        moviesRecyclerAdapter = new MoviesRecyclerAdapter(getActivity());
        moviesRecyclerAdapter.setItemClickedListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.rvMovieDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(moviesRecyclerAdapter);

    }

    public void setMovie(long movie_api_id){
        Log.i(TAG, "setMovie called: movie id = " + movie_api_id);
        getDataWithVolley(Utils.buildMovieInfoQueryString(movie_api_id));
    }

    public void getDataWithVolley(Uri url) {
        Log.i(TAG, "About the get data with Volley, Uri: " + url.toString());
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Volley onResponse: " + response);

//                        response = Utils.getJsonContentFromFlickerRespose(response);
                        // Display the first 500 characters of the response string.
//                        movies = MoviesJsonParser.parseFeed(response);
//                        addMoviesToDb(movies);
//                        loaderManager.initLoader(LOADER_ID, null, MoviesFeedFragment.this);
                        movie = MoviesJsonParser.parseMovieDetailsFromResponse(response);
                        Log.i(TAG, "onResponse: movie details: " + movie.toString());
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

    private void refreshDisplay(){
        if(movie != null){
//            tvOverview.setText(movie.getOverview());
//            tvReleased.setText(movie.getRelease_date());
//            String rating = Double.toString(movie.getRating()) + getActivity().getString(R.string.rating_suffix_str);
//            tvRating.setText(rating);
//            tvDuration.setText(movie.getDuration() + "Min");
//            NumberFormat fmt = NumberFormat.getCurrencyInstance();
//            Picasso.with(getActivity()).load(Utils.buildImageUri(movie.getImageUrl())).fit().centerCrop().into(ivMovieImage);

//            ImageLoader imageLoader = new ImageLoader();
//            imageLoader.execute(fov);
            moviesRecyclerAdapter.updateVideos(movie, movie.getVideos());
        }


    }

    @Override
    public void onItemClicked(Video video) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getYouTubeKey())));
    }
}
