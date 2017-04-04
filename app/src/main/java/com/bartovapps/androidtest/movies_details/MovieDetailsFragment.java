package com.bartovapps.androidtest.movies_details;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bartovapps.androidtest.R;
import com.bartovapps.androidtest.adpaters.MovieDetailsRecyclerAdapter;
import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.model.Trailer;
import com.bartovapps.androidtest.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailsFragment extends Fragment implements MovieDetailsRecyclerAdapter.MoviesRecyclerItemClickListener, DetailsContract.View {

    public static final String TAG = MovieDetailsFragment.class.getSimpleName();
    public static final String MOVIE_ID_ARG = "movieId";
    ImageView ivMovieImage;
    TextView tvTitle;
    TextView tvReleased;
    TextView tvOverview;
    TextView tvRating;
    TextView tvDuration;
    MovieDetailsRecyclerAdapter movieDetailsRecyclerAdapter;
    RecyclerView recyclerView;
    Movie mMovie;
    Gson gson;
    DetailsContract.Presenter mPresenter;
    long mMovieId;
    SharedPreferences sharedPreferences;
    int mApiClient;

    private SimpleDateFormat apiDateFormat;
    private SimpleDateFormat appDateFormat;


    public static MovieDetailsFragment newInstance(long movieId) {
        MovieDetailsFragment f = new MovieDetailsFragment();
        Bundle b = new Bundle();
        b.putLong(MOVIE_ID_ARG, movieId);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        gson = new Gson();
        mPresenter = new DetailsPresenter(getActivity(), this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        appDateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

        // movie = getActivity().getContentResolver().query(CONTENT_URI, )
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        mMovieId = getArguments().getLong(MOVIE_ID_ARG);
        setViews(view);
        mApiClient = Integer.parseInt(sharedPreferences.getString("api_client", "" + getResources().getInteger(R.integer.Volley)));
        mPresenter.setApiClient(mApiClient);
        loadMovieDetail(mMovieId);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void setViews(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        ivMovieImage = (ImageView) view.findViewById(R.id.ivDetailedImage);
        String transitionName = getString(R.string.shared_transition_name) + mMovieId;
        Log.i(TAG, "TransitionName: " + transitionName);
        ivMovieImage.setTransitionName(transitionName);

        tvReleased = (TextView) view.findViewById(R.id.tvReleased);
        tvOverview = (TextView) view.findViewById(R.id.tvOverview);
        tvRating = (TextView) view.findViewById(R.id.tvRating);
        tvDuration = (TextView) view.findViewById(R.id.tvDuration);

        movieDetailsRecyclerAdapter = new MovieDetailsRecyclerAdapter(getActivity());
        movieDetailsRecyclerAdapter.setItemClickedListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.rvMovieDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(movieDetailsRecyclerAdapter);

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void loadMovieDetail(long movieId){
        mPresenter.loadMovieDetails(movieId);
    }
    private void refreshDisplay() {
        if (mMovie != null) {
            Date date = null;
            try {
                date = apiDateFormat.parse(mMovie.release_date);
                tvReleased.setText(appDateFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvTitle.setText(mMovie.title);
            tvOverview.setText(mMovie.overview);
            tvDuration.setText(mMovie.runtime + "Min");
            tvRating.setText(mMovie.vote_average + "/10");

            Glide.with(getActivity())
                    .load(Utils.buildImageUri(mMovie.poster_path))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into((ivMovieImage));


            movieDetailsRecyclerAdapter.updateVideos(mMovie);
        }
    }

    @Override
    public void onItemClicked(Trailer trailer) {
        Log.i(TAG, "onItemClicked");
        mPresenter.onTrailerItemClicked(trailer);
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void playTrailer(Trailer trailer) {
        Uri uri = Utils.buildYouTubeUri(trailer.key);
        Log.i(TAG, "YouTube URI: " + uri.toString());
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @Override
    public void showMovieDetails(Movie movie) {
        mMovie = movie;
        refreshDisplay();
    }
}
