package com.bartovapps.androidtest.movies;

import android.database.Cursor;
import android.net.Uri;
import android.view.View;

import com.bartovapps.androidtest.BasePresenter;
import com.bartovapps.androidtest.BaseView;
import com.bartovapps.androidtest.model.Movie;

import java.util.List;

/**
 * Created by motibartov on 22/03/2017.
 */

public interface MoviesContract {

    interface View extends BaseView{
        void showMovies(Cursor cursor);
        void showLoadMoviesError();
        void showNoMovies();
        void showMovieDetails(android.view.View v, long movieId);
    }

    interface Presenter extends BasePresenter{

        void loadMovies(String search, boolean forceUpdate);
        void movieItemClicked(android.view.View v, int position);
    }

}
