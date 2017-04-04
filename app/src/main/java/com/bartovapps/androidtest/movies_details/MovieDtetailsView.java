package com.bartovapps.androidtest.movies_details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.model.Trailer;

/**
 * Created by motibartov on 04/04/2017.
 */

public class MovieDtetailsView implements DetailsContract.View{

    Context mContext;
    TextView tvTitle;
    TextView tvOverview;
    TextView tvRuntime;
    TextView tvRating;
    TextView tvReleased;

    ImageView ivPoster;

    RecyclerView rvTrailers;




    public MovieDtetailsView(Context context){
        mContext = context;
    }


    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void playTrailer(Trailer trailer) {

    }

    @Override
    public void showMovieDetails(Movie movie) {

    }
}
