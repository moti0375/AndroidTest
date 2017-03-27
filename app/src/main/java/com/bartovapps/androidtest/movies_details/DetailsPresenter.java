package com.bartovapps.androidtest.movies_details;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bartovapps.androidtest.R;
import com.bartovapps.androidtest.api.ApiClient;
import com.bartovapps.androidtest.api.ApiHelper;
import com.bartovapps.androidtest.api.ApiInterface;
import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.model.SearchResponse;
import com.bartovapps.androidtest.model.Trailer;
import com.bartovapps.androidtest.movies.MoviesPresenter;
import com.bartovapps.androidtest.utils.Utils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by motibartov on 23/03/2017.
 */

public class DetailsPresenter implements DetailsContract.Presenter {
    private static final String TAG = "DetailsPresenter";

    Context mContext;
    Gson gson;
    Movie mMovie;
    DetailsContract.View detailsView;
    int mApiClient;
    Subscription subscription;


    public DetailsPresenter(Context context, DetailsContract.View view){
        Log.i(TAG, "DetailsPresenter created");
        mContext = context;
        detailsView = view;
        gson = new Gson();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void setApiClient(int apiClient) {
        mApiClient = apiClient;
    }

    @Override
    public void onTrailerItemClicked(Trailer trailer) {

    }

    @Override
    public void loadMovieDetails(long movie_api_id) {

        if(mApiClient == mContext.getResources().getInteger(R.integer.Volley)){
            getDataWithVolley(Utils.buildMovieInfoQueryString(movie_api_id));
        }else if(mApiClient == mContext.getResources().getInteger(R.integer.Retrofit)){
            getDataWithRetrofit(movie_api_id);
        }else if(mApiClient == mContext.getResources().getInteger(R.integer.OkHttp)){
            subscription = getObservable(Utils.buildMovieInfoQueryString(movie_api_id))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Movie>() {
                        @Override
                        public void onCompleted() {
                            Log.i(TAG, "onCompleted");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i(TAG, "onError");
                        }

                        @Override
                        public void onNext(Movie movie) {
                            mMovie = movie;
                            detailsView.showMovieDetails(mMovie);
                        }
                    });
        }else{
            getDataWithVolley(Utils.buildMovieInfoQueryString(movie_api_id));
        }

    }


    public void getDataWithVolley(Uri url) {
        Log.i(TAG, "getDataWithVolley, Uri: " + url.toString());
        RequestQueue queue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Volley onResponse: " + response);

                        mMovie = gson.fromJson(response, Movie.class);
                        Log.i(TAG, "Trailer Search Response: " + mMovie.toString());
                        detailsView.showMovieDetails(mMovie);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Volley onErrorResponse: " + error);
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getDataWithRetrofit(long movie_id) {
        Log.i(TAG, "getDataWithRetrofit called");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Movie> call = apiService.getMovieDetails(movie_id, ApiHelper.TMDB_API_KEY);
        Log.i(TAG, "call url: " + call.request().url());

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, retrofit2.Response<Movie> response) {
                mMovie = response.body();
                Log.i(TAG, "Retrofit onResponse: " + mMovie);
                detailsView.showMovieDetails(mMovie);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e(TAG, "Retrofit onFailure: " + t.getMessage());
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

    public Observable<Movie> getObservable(final Uri uri) {
        Log.i(TAG, "getObservable was called");
        return Observable.defer(new Func0<Observable<Movie>>() {  //Returns no value..
            @Override
            public Observable<Movie> call() {

                try {
                    return Observable.just(getMovieWithOkHttp(uri));
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
    }
}
