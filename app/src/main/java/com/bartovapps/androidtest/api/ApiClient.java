package com.bartovapps.androidtest.api;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by motibartov on 25/03/2017.
 */

public class ApiClient {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private RetrofitService mRetrofitService;

    public RetrofitService getApiService(){
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ClientAuthInterceptor(ApiHelper.TMDB_API_KEY))
                .build();


            mRetrofitService = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()
                    .create(RetrofitService.class);

        return mRetrofitService;
    }


}
