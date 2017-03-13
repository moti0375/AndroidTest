package com.bartovapps.androidtest.utils;

import android.net.Uri;

import com.bartovapps.androidtest.ApiManager;

/**
 * Created by BartovMoti on 01/24/17.
 */
public class Utils {



    public static Uri buildRestQueryString(String searchParam){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ApiManager.API_SCHEMA)
                .authority(ApiManager.TMDB_BASE_URL)
                .appendPath(ApiManager.API_VERSION_PARAM)
                .appendPath(ApiManager.API_MOVIE_PARAM)
                .appendPath(searchParam)
                .appendQueryParameter(ApiManager.API_KEY_URL_PARAM, ApiManager.TMDB_API_KEY)
                .appendQueryParameter(ApiManager.API_PAGES_PARAM, "1");


        return builder.build();
    }

    public static Uri buildImageUri(String imageUrl) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ApiManager.API_SCHEMA)
                .authority(ApiManager.TMDB_IMAGES_BASE_URL)
                .appendPath(ApiManager.TMDB_IMAGES_T)
                .appendPath(ApiManager.TMDB_IMAGES_P)
                .appendPath(ApiManager.TMDB_IMAGES_SIZE)
                .appendEncodedPath(imageUrl);

        return builder.build();
    }

    public static Uri buildMovieInfoQueryString(long api_id){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ApiManager.API_SCHEMA)
                .authority(ApiManager.TMDB_BASE_URL)
                .appendPath(ApiManager.API_VERSION_PARAM)
                .appendPath(ApiManager.API_MOVIE_PARAM)
                .appendPath(Long.toString(api_id))
                .appendQueryParameter(ApiManager.API_KEY_URL_PARAM, ApiManager.TMDB_API_KEY)
                .appendQueryParameter(ApiManager.API_APPEND_TO_RESP, ApiManager.API_VIDEOS_PARAM);


        return builder.build();
    }
}
