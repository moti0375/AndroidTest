package com.bartovapps.androidtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bartovapps.androidtest.fragments.MovieDetailsFragment;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Intent intent = getIntent();
        Bundle b = intent.getBundleExtra(MainActivity.MOVIE_BUNDLE);
        if (b != null) {
            fragment.setArguments(b);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.details_fragment_container, fragment).commit();

    }
}
