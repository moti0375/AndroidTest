package com.bartovapps.androidtest.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bartovapps.androidtest.R;
import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailsFragment extends Fragment {

    public static final String TAG = MovieDetailsFragment.class.getSimpleName();
    ImageView ivMovieImage;
    TextView tvReleased;
    TextView tvOverview;
    TextView tvRating;

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
        if(movie != null){
            setMovie(movie);
        }
    }

    private void setViews(View view) {
        ivMovieImage = (ImageView) view.findViewById(R.id.ivDetailedImage);
        tvReleased = (TextView) view.findViewById(R.id.tvReleased);
        tvOverview = (TextView) view.findViewById(R.id.overview);
    }

    public void setMovie(Movie movie){
        Log.i(TAG, "setMovie called");

        if(movie != null){
            tvOverview.setText(movie.getOverview());
            tvReleased.setText(movie.getRelease_date());
            NumberFormat fmt = NumberFormat.getCurrencyInstance();
            Picasso.with(getActivity()).load(Utils.buildImageUri(movie.getImageUrl())).fit().centerCrop().into(ivMovieImage);

//            ImageLoader imageLoader = new ImageLoader();
//            imageLoader.execute(fov);
        }

    }
}
