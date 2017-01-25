package com.bartovapps.androidtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.bartovapps.androidtest.fragments.MovieDetailsFragment;
import com.bartovapps.androidtest.fragments.MoviesFeedFragment;
import com.bartovapps.androidtest.model.Movie;

public class MainActivity extends AppCompatActivity implements MoviesFeedFragment.FragmentEventListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String MOVIE_BUNDLE = "MOVIE_BUNDLE";
    MoviesFeedFragment moviesFeedFragment;
    boolean mTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesFeedFragment = new MoviesFeedFragment();
        moviesFeedFragment.setFragmentEventListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, moviesFeedFragment).commit();

        ViewGroup fragmentContainer = (ViewGroup) findViewById(R.id.details_fragment_container);
        mTablet = (fragmentContainer != null);

        Log.i(TAG, "onCreate: is tablet = " + mTablet);

        if(mTablet){
            MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.details_fragment_container, movieDetailsFragment).commit();
        }



    }

    @Override
    public void onFragmentEvent(Movie movie) {
        Bundle b = movie.toBundle();

        if(mTablet){
            MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
            detailsFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.details_fragment_container, detailsFragment).commit();
        }else{
            Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
            intent.putExtra(MOVIE_BUNDLE, b);
            startActivityForResult(intent, 1001);
        }
    }
}
