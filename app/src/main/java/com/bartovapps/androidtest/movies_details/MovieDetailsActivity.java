package com.bartovapps.androidtest.movies_details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bartovapps.androidtest.MainActivity;
import com.bartovapps.androidtest.R;
import com.bartovapps.androidtest.adpaters.MovieDetailsRecyclerAdapter;
import com.bartovapps.androidtest.adpaters.RecyclerItemClickListener;
import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.model.Trailer;
import com.bartovapps.androidtest.movies_details.MovieDetailsFragment;
import com.bartovapps.androidtest.utils.Displayer;
import com.bartovapps.androidtest.utils.ReleasedDisplayer;
import com.bartovapps.androidtest.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import static com.bartovapps.androidtest.R.id.ivMovieImage;
import static com.bartovapps.androidtest.R.id.tvDuration;

public class MovieDetailsActivity extends AppCompatActivity implements DetailsContract.View, MovieDetailsRecyclerAdapter.MoviesRecyclerItemClickListener {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    MovieDetailsFragment fragment;

    TextView tvTitle;
    TextView tvOverview;
    TextView tvReleased;
    TextView tvRating;
    TextView tvDuration;

    ImageView ivPoster;
    Displayer releasedDisplayer;
    RecyclerView mRvTrailers;

    long mMovieId;

    DetailsPresenter mPresenter;
    MovieDetailsRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportFragmentManager().beginTransaction().replace(R.id.details_fragment_container, fragment).commit();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPresenter = new DetailsPresenter(this, this);

        Intent intent = getIntent();
        mMovieId = intent.getLongExtra(MainActivity.INTENT_LONG_EXTRA, 0);
        setViews();
      //  fragment = (MovieDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_fragment);
//        fragment.setMovieId(movie_api_id);
    }

    public void setViews(){
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        tvReleased = (TextView) findViewById(R.id.tvReleased);
        releasedDisplayer = new ReleasedDisplayer(tvReleased);
        tvRating = (TextView) findViewById(R.id.tvRating);
        tvDuration = (TextView) findViewById(R.id.tvDuration);

        ivPoster = (ImageView) findViewById(R.id.ivDetailedImage);
        ivPoster.setTransitionName(getString(R.string.shared_transition_name) + mMovieId);

        mRvTrailers = (RecyclerView) findViewById(R.id.rvMovieDetails);
        mRvTrailers.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MovieDetailsRecyclerAdapter(this);
        mAdapter.setItemClickedListener(this);
        mRvTrailers.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadMovieDetails(mMovieId);
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void playTrailer(Trailer trailer) {
        Uri uri = Utils.buildYouTubeUri(trailer.key);
        Log.i(TAG, "YouTube URI: " + uri.toString());
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @Override
    public void showMovieDetails(Movie movie) {
        Log.i(TAG, "showMovieDetails called");
        tvTitle.setText(movie.title);
        tvOverview.setText(movie.overview);
        tvRating.setText(movie.vote_average + "/10");
        tvDuration.setText(movie.runtime + "Min");
        releasedDisplayer.setText(movie.release_date);



        Glide.with(this)
                .load(Utils.buildImageUri(movie.poster_path))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into((ivPoster));
        mAdapter.updateVideos(movie);

    }


    @Override
    public void onItemClicked(Trailer trailer) {
        mPresenter.onTrailerItemClicked(trailer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected");

        int itemId = item.getItemId();

        if(itemId  == android.R.id.home){
            setResult(RESULT_OK);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
