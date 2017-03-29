package com.bartovapps.androidtest.movies;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bartovapps.androidtest.api.ApiHelper;
import com.bartovapps.androidtest.R;
import com.bartovapps.androidtest.adpaters.RecyclerItemClickListener;
import com.bartovapps.androidtest.adpaters.SimpleCursorRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFeedFragment extends Fragment implements MoviesContract.View {

    public static final String TAG = MoviesFeedFragment.class.getSimpleName();
    private static final int LOADER_ID = 1;
    RecyclerView rvMoviesFeed;
    SimpleCursorRecyclerAdapter moviesRecyclerAdapter;
    FragmentEventListener mFragmentEventListener;
    LoaderManager loaderManager;
    int mApiClient;
    String mSearch = ApiHelper.API_TOP_RATED;

    SharedPreferences sharedPreferences;
    MoviesContract.Presenter mPresenter;

    public static MoviesFeedFragment newInstance() {
        return new MoviesFeedFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        loaderManager = getLoaderManager();
        setRetainInstance(true);
        //  loaderManager.initLoader(LOADER_ID, null, this);
        //loaderManager.initLoader(LOADER_ID, null, MoviesFeedFragment.this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        readPreferences();
        mPresenter = new MoviesPresenter(getActivity(), loaderManager, this);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_movies_feed, container, false);
        setupViews(view);

        setHasOptionsMenu(true);

        return view;
    }

    private void setupViews(View view) {
        rvMoviesFeed = (RecyclerView) view.findViewById(R.id.rvMoviesFeed);
        rvMoviesFeed.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvMoviesFeed, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), position + " Item clicked.. ", Toast.LENGTH_SHORT).show();
                mPresenter.movieItemClicked(position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        moviesRecyclerAdapter = new SimpleCursorRecyclerAdapter(getActivity(), null);
        rvMoviesFeed.setAdapter(moviesRecyclerAdapter);
        rvMoviesFeed.setHasFixedSize(true);
//        rvMoviesFeed.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(this, R.drawable.divider_drawable)));
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);

        rvMoviesFeed.setLayoutManager(manager);
//        rvImagesRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, rvImagesRecyclerView, new ClickListener() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mApiClient = Integer.parseInt(sharedPreferences.getString("api_client", "" + getResources().getInteger(R.integer.Volley)));
        mPresenter.setApiClient(mApiClient);

        if (savedInstanceState == null) {
            mPresenter.loadMovies(mSearch, true);
        } else {
            Log.i(TAG, "onActivityCreated: savedInstanceState not null");
            mPresenter.loadMovies(mSearch, false);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentEventListener = (FragmentEventListener) context;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    private void refreshDisplay(Cursor cursor) {
        Log.i(TAG, "About to refresh display with " + ((cursor == null) ? null : cursor.getCount()) + " items");
        moviesRecyclerAdapter.changeCursor(cursor);
    }


    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void showMovies(Cursor cursor) {

        refreshDisplay(cursor);
    }

    @Override
    public void showLoadMoviesError() {

    }

    @Override
    public void showNoMovies() {

    }

    @Override
    public void showMovieDetails(long movieId) {
        Log.i(TAG, "showMovieDetails called..");
        mFragmentEventListener.onFragmentEvent(movieId);

    }

    public interface FragmentEventListener {
        void onFragmentEvent(long movie_api_id);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected called");
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.top_rated:
                mSearch = ApiHelper.API_TOP_RATED;
                break;
            case R.id.most_popular:
                mSearch = ApiHelper.API_MOST_POPULAR;
                break;
            case R.id.now_playing:
                mSearch = ApiHelper.API_NOW_PLAYING;
                break;
            case R.id.upcoming:
                mSearch = ApiHelper.API_UPCOMING;
                break;
            default:
                mSearch = ApiHelper.API_TOP_RATED;
                break;

        }

        saveSearchToPreferences(mSearch);
        getActivity().setTitle(mSearch.toUpperCase().replace("_", " "));
        mPresenter.loadMovies(mSearch, true);

        return true;
    }

    SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals(getResources().getString(R.string.api_pref_key))){
                mApiClient = Integer.parseInt(sharedPreferences.getString(key, "10"));
                Log.i(TAG, "onSharedPreferenceChanged called, changed key: " + key + ", value = " + mApiClient);
                mPresenter.setApiClient(mApiClient);
            }
        }
    };

    private void readPreferences() {
        mSearch = sharedPreferences.getString("search", ApiHelper.API_TOP_RATED).toUpperCase().replace("_"," ");
        getActivity().setTitle(mSearch);
    }
    private void saveSearchToPreferences(String search){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("search", search).commit();
    }


}
