package com.bartovapps.androidtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bartovapps.androidtest.fragments.MovieDetailsFragment;

public class MovieDetailsActivity extends AppCompatActivity {
    MovieDetailsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportFragmentManager().beginTransaction().replace(R.id.details_fragment_container, fragment).commit();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        long movie_api_id = intent.getLongExtra(MainActivity.INTENT_LONG_EXTRA, 0);
        fragment = (MovieDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_fragment);
        fragment.setMovie(movie_api_id);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
