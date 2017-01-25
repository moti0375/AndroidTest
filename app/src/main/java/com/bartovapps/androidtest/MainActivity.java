package com.bartovapps.androidtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;

import com.bartovapps.androidtest.fragments.MovieDetailsFragment;
import com.bartovapps.androidtest.fragments.MoviesFeedFragment;
import com.bartovapps.androidtest.model.Movie;

public class MainActivity extends AppCompatActivity implements MoviesFeedFragment.FragmentEventListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String MOVIE_BUNDLE = "MOVIE_BUNDLE";
    MovieDetailsFragment movieDetailsFragment;
    MoviesFeedFragment moviesFeedFragment;
    boolean mTablet;
    ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        viewGroup = (ViewGroup) findViewById(R.id.llWideScreen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTablet = viewGroup != null;
        Log.i(TAG, "onCreate: is tablet = " + mTablet);

        moviesFeedFragment = (MoviesFeedFragment) getSupportFragmentManager().findFragmentById(R.id.moviesFragment);
        if(moviesFeedFragment != null){
            moviesFeedFragment.setFragmentEventListener(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onFragmentEvent(Movie movie) {
        Bundle b = movie.toBundle();

        if (mTablet) {
            movieDetailsFragment = (MovieDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_fragment);

            if(movieDetailsFragment != null){
                movieDetailsFragment.setMovie(new Movie(b));
            }

        } else {
            Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
            intent.putExtra(MOVIE_BUNDLE, b);
            startActivityForResult(intent, 1001);
        }
    }
}
