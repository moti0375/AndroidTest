package com.bartovapps.androidtest.movies_details;


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
import com.bartovapps.androidtest.model.Trailer;
import com.bartovapps.androidtest.utils.Utils;
import com.google.gson.Gson;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailsFragment extends Fragment implements MoviesRecyclerAdapter.MoviesRecyclerItemClickListener, DetailsContract.View {

    public static final String TAG = MovieDetailsFragment.class.getSimpleName();
    ImageView ivMovieImage;
    TextView tvReleased;
    TextView tvOverview;
    TextView tvRating;
    TextView tvDuration;
    MoviesRecyclerAdapter moviesRecyclerAdapter;
    RecyclerView recyclerView;
    Movie mMovie;
    Gson gson;
    DetailsContract.Presenter mPresenter;
    long mMovieId;

    public MovieDetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        gson = new Gson();
        mPresenter = new DetailsPresenter(getActivity(), this);
        // movie = getActivity().getContentResolver().query(CONTENT_URI, )

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

    public void setMovieId(long movieId){
        mMovieId = movieId;
    }


    @Override
    public void onResume() {
        super.onResume();

        mPresenter.loadMovieDetails(mMovieId);

    }

    private void refreshDisplay() {
        if (mMovie != null) {
            moviesRecyclerAdapter.updateVideos(mMovie);
        }
    }

    @Override
    public void onItemClicked(Trailer trailer) {
        mPresenter.onTrailerItemClicked(trailer);
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void playTrailer(Trailer trailer) {
        startActivity(new Intent(Intent.ACTION_VIEW, Utils.buildYouTubeUri(trailer.key)));
    }

    @Override
    public void showMovieDetails(Movie movie) {
        mMovie = movie;
        refreshDisplay();
    }
}
