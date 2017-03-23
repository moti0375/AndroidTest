package com.bartovapps.androidtest.movies_details;

import com.bartovapps.androidtest.BasePresenter;
import com.bartovapps.androidtest.BaseView;
import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.model.Trailer;

/**
 * Created by motibartov on 23/03/2017.
 */

public interface DetailsContract {

    interface View extends BaseView{
        void playTrailer(Trailer trailer);
        void showMovieDetails(Movie movie);
    }

    interface Presenter extends BasePresenter{
        void onTrailerItemClicked(Trailer trailer);
        void loadMovieDetails(long movie_api_id);
    }
}
