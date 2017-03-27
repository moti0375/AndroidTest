package com.bartovapps.androidtest.api;

import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.model.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryName;

/**
 * Created by motibartov on 25/03/2017.
 */

public interface ApiInterface {

    @GET("movie/top_rated" )
    Call<SearchResponse> getTopRatedMovies(@Query(ApiHelper.API_KEY_URL_PARAM) String apiKey);

    @GET("movie/{search}" )
    Call<SearchResponse> searchMovies(@Path("search") String search,@Query(ApiHelper.API_KEY_URL_PARAM) String apiKey);


    @GET("movie/{id}?" + ApiHelper.API_APPEND_TO_RESP + "=videos")
    Call<Movie> getMovieDetails(@Path("id") long id, @Query(ApiHelper.API_KEY_URL_PARAM) String apiKey);
}
