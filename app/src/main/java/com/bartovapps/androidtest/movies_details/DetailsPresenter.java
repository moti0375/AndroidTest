package com.bartovapps.androidtest.movies_details;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bartovapps.androidtest.R;
import com.bartovapps.androidtest.api.ApiClient;
import com.bartovapps.androidtest.api.ApiHelper;
import com.bartovapps.androidtest.api.RetrofitService;
import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.model.Trailer;
import com.bartovapps.androidtest.utils.Utils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by motibartov on 23/03/2017.
 */

class DetailsPresenter implements DetailsContract.Presenter {
    private static final String TAG = "DetailsPresenter";

    private Context mContext;
    private Gson gson;
    private Movie mMovie;
    private DetailsContract.View detailsView;
    private int mPrefApi;
    private Subscription subscription;
    private ApiClient mApiClient;


    public DetailsPresenter(Context context, DetailsContract.View view){
        Log.i(TAG, "DetailsPresenter created");
        mApiClient = new ApiClient();
        mContext = context;
        detailsView = view;
        gson = new Gson();
    }

    @Override
    public void subscribe() {

    }
    @Override
    public void unsubscribe() {
        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.isUnsubscribed();
        }
    }

    @Override
    public void onTrailerItemClicked(Trailer trailer) {
        detailsView.playTrailer(trailer);
    }

    @Override
    public void loadMovieDetails(long movie_api_id) {
        getDataWithRetrofit(movie_api_id);
    }




    private void getDataWithRetrofit(long movie_id) {
        Log.i(TAG, "getDataWithRetrofit called");
        RetrofitService apiService = mApiClient.getApiService();

        apiService.getMovieDetails(movie_id, ApiHelper.TMDB_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Movie>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Movie movie) {
                        Log.i(TAG, "onNext: Got your movie details you asshole!");
                        detailsView.showMovieDetails(movie);
                    }
                });

    }

    private Movie getMovieWithOkHttp(Uri uri) throws IOException {
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url(uri.toString()).build();
        okhttp3.Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            Movie movie = gson.fromJson(response.body().charStream(), Movie.class);
            Log.i(TAG, "getMovieWithOkHttp: " + movie.toString() );
            return movie;
        }
        return null;
    }

    private Observable<Movie> getObservable(final Uri uri) {
        Log.i(TAG, "getObservable was called");
        return Observable.defer(() -> {

            try {
                return Observable.just(getMovieWithOkHttp(uri));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
}
