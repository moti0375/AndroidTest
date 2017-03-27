package com.bartovapps.androidtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.FrameLayout;
import android.widget.ListView;

import com.bartovapps.androidtest.movies_details.MovieDetailsActivity;
import com.bartovapps.androidtest.movies_details.MovieDetailsFragment;
import com.bartovapps.androidtest.movies.MoviesFeedFragment;

public class MainActivity extends AppCompatActivity implements MoviesFeedFragment.FragmentEventListener, MainActivityContract.View {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String MOVIES_FRAGMENT_TAG = "MoviesFragment";
    public static final String DETAILED_FRAGMENT_TAG = "DetailsFragment";
    public static final String MOVIE_BUNDLE = "MOVIE_BUNDLE";
    public static final String INTENT_LONG_EXTRA = "movie_api_id";
    MovieDetailsFragment movieDetailsFragment;
    MoviesFeedFragment moviesFeedFragment;
    boolean mTablet;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    DrawerLayout mDrawerLayout;
    NavigationView mNavView;

    FragmentManager mFragmentManager;
    MainActivityContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mFragmentManager = getSupportFragmentManager();
        moviesFeedFragment = MoviesFeedFragment.newInstance();
        mPresenter = new MainPresenter(this, this);
        setViews();
    }

    private void setViews() {
        if (findViewById(R.id.llWideScreen) != null) {
            mTablet = true;
        } else {
            mTablet = false;
        }

        Log.i(TAG, "Tablet = " + mTablet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_closed, R.string.drawer_opened) {

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
                mPresenter.drawerSettingsItemClicked(itemId);
                return true;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "on Resume - adding MoviesFragment");
        if (getSupportFragmentManager().findFragmentByTag(MOVIES_FRAGMENT_TAG) == null) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.add(R.id.fragmentContainer, moviesFeedFragment, MOVIES_FRAGMENT_TAG).commit();
        }
    }


    @Override
    public void onFragmentEvent(long movie_api_id) {
        movieDetailsFragment = MovieDetailsFragment.newInstance(movie_api_id);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mTablet) {
            transaction.replace(R.id.detailedContainer, movieDetailsFragment, DETAILED_FRAGMENT_TAG).commit();
        } else {
            transaction.add(R.id.fragmentContainer, movieDetailsFragment, DETAILED_FRAGMENT_TAG);
            transaction.addToBackStack(null);
            transaction.commit();
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
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void openSettings() {
        Log.i(TAG, "About to open settings Activity");
        Intent prefsIntent = new Intent(MainActivity.this, PrefsActivity.class);
        mDrawerLayout.closeDrawer(mNavView);
        startActivity(prefsIntent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
