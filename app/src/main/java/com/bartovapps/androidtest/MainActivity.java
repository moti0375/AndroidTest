package com.bartovapps.androidtest;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.bartovapps.androidtest.animations.DetailsTransition;
import com.bartovapps.androidtest.movies_details.MovieDetailsActivity;
import com.bartovapps.androidtest.movies_details.MovieDetailsFragment;
import com.bartovapps.androidtest.movies.MoviesFeedFragment;

import static android.R.attr.transitionName;

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
    LayoutTransition layoutTransition;

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
        layoutTransition = new LayoutTransition();
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragmentContainer);
        frameLayout.setLayoutTransition(layoutTransition);

        mTablet = findViewById(R.id.llWideScreen) != null;

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
        mNavView.setNavigationItemSelectedListener(item -> {
            Log.i(TAG, "onNavigationItemSelected");

            int itemId = item.getItemId();
            mPresenter.drawerSettingsItemClicked(itemId);
            return true;
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
        Log.i(TAG, "onResume - adding MoviesFragment");
        if (getSupportFragmentManager().findFragmentByTag(MOVIES_FRAGMENT_TAG) == null) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer, moviesFeedFragment, MOVIES_FRAGMENT_TAG).commit();
        }
    }


    @Override
    public void onFragmentEvent(Fragment f, View v, long movie_api_id) {

        f.setSharedElementReturnTransition(new DetailsTransition());
        f.setExitTransition(new DetailsTransition());

        movieDetailsFragment = MovieDetailsFragment.newInstance(movie_api_id);
        movieDetailsFragment.setSharedElementEnterTransition(new DetailsTransition());
        movieDetailsFragment.setEnterTransition(new DetailsTransition());

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        String transitionName = getString(R.string.shared_transition_name) + movie_api_id;
        v.setTransitionName(transitionName);

        if (mTablet) {
            transaction.setCustomAnimations(R.anim.fade_in,
                    R.anim.fade_out);
            transaction.replace(R.id.detailedContainer, movieDetailsFragment, DETAILED_FRAGMENT_TAG);
            transaction.addSharedElement(v, transitionName);
            transaction.commit();

        } else {
            Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
            intent.putExtra(INTENT_LONG_EXTRA, movie_api_id);

            ActivityOptionsCompat options =

                    ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                            v,   // Starting view
                            transitionName    // The String
                    );
            //Start the Intent
            ActivityCompat.startActivity(this, intent, options.toBundle());

            // startActivity(intent);
//            Log.i(TAG, "TransitionName: " + transitionName);
//            //transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
//            transaction.replace(R.id.fragmentContainer, movieDetailsFragment, DETAILED_FRAGMENT_TAG);
//            transaction.addToBackStack(DETAILED_FRAGMENT_TAG);
//            transaction.addSharedElement(v, transitionName);
//            transaction.commit();
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

        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.top_rated:
//                getSupportActionBar().setTitle(getString(R.string.top_rated));
                break;
            case R.id.most_popular:
//                getSupportActionBar().setTitle(getString(R.string.most_popular));
                break;
            case R.id.now_playing:
//                getSupportActionBar().setTitle(getString(R.string.now_playing));
                break;
            case R.id.upcoming:
//                getSupportActionBar().setTitle(getString(R.string.upcoming));
                break;
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
