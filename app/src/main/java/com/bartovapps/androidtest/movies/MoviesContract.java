package com.bartovapps.androidtest.movies;

import android.database.Cursor;
import android.net.Uri;

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
        void showMovieDetails(long movieId);
    }

    interface Presenter extends BasePresenter{

        void loadMovies(Uri uri);
        void movieItemClicked(int position);

    }

}
