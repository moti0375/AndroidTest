package com.bartovapps.androidtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bartovapps.androidtest.movies_details.MovieDetailsActivity;
import com.bartovapps.androidtest.movies_details.MovieDetailsFragment;
import com.bartovapps.androidtest.movies.MoviesFeedFragment;

public class MainActivity extends AppCompatActivity implements MoviesFeedFragment.FragmentEventListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int DETAILED_ACTIVITY = 100;
    public static final String MOVIE_BUNDLE = "MOVIE_BUNDLE";
    public static final String INTENT_LONG_EXTRA = "movie_api_id";
    MovieDetailsFragment movieDetailsFragment;
    MoviesFeedFragment moviesFeedFragment;
    boolean mTablet;
    ViewGroup viewGroup;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    DrawerLayout mDrawerLayout;
    NavigationView mNavView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        setViews();

        moviesFeedFragment = (MoviesFeedFragment) getSupportFragmentManager().findFragmentById(R.id.moviesFragment);
        if(moviesFeedFragment != null){
            moviesFeedFragment.setFragmentEventListener(this);
        }


    }

    private void setViews(){
        viewGroup = (ViewGroup) findViewById(R.id.llWideScreen);
        mTablet = viewGroup != null;
        Log.i(TAG, "onCreate: is tablet = " + mTablet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_closed, R.string.drawer_opened){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.i(TAG, "onDrawerOpened");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.i(TAG, "onDrawerClosed");
            }
        };

        mActionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mNavView = (NavigationView) findViewById(R.id.nvView);
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.i(TAG, "onNavigationItemSelected");

                int itemId = item.getItemId();
                switch (itemId){
                    case R.id.settings:
                        Log.i(TAG, "About to open settings Activity");
                        Intent prefsIntent= new Intent(MainActivity.this, PrefsActivity.class);
                        startActivity(prefsIntent);
                }
                return true;
            }
        });

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onFragmentEvent(long movie_api_id) {

        if (mTablet) {
            movieDetailsFragment = (MovieDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_fragment);

            if(movieDetailsFragment != null){
                movieDetailsFragment.setMovieId(movie_api_id);
            }

        } else {
            Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
          //  intent.putExtra(MOVIE_BUNDLE, b);
            intent.putExtra(INTENT_LONG_EXTRA, movie_api_id);
            startActivityForResult(intent, DETAILED_ACTIVITY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu called");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected called");
        if(mActionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Log.i(TAG, "Drawer onItemClicked");
        }
    }
}
