package com.bartovapps.androidtest.utils;

import android.net.Uri;

import com.bartovapps.androidtest.api.ApiHelper;
import com.bartovapps.androidtest.api.YouTubeApiHelper;

/**
 * Created by BartovMoti on 01/24/17.
 */
public class Utils {



    public static Uri buildRestQueryString(String searchParam){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ApiHelper.API_SCHEMA)
                .authority(ApiHelper.TMDB_BASE_URL)
                .appendPath(ApiHelper.API_VERSION_PARAM)
                .appendPath(ApiHelper.API_MOVIE_PARAM)
                .appendPath(searchParam)
                .appendQueryParameter(ApiHelper.API_KEY_URL_PARAM, ApiHelper.TMDB_API_KEY)
                .appendQueryParameter(ApiHelper.API_PAGES_PARAM, "1");


        return builder.build();
    }

    public static Uri buildImageUri(String imageUrl) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ApiHelper.API_SCHEMA)
                .authority(ApiHelper.TMDB_IMAGES_BASE_URL)
                .appendPath(ApiHelper.TMDB_IMAGES_T)
                .appendPath(ApiHelper.TMDB_IMAGES_P)
                .appendPath(ApiHelper.TMDB_IMAGES_SIZE)
                .appendEncodedPath(imageUrl);

        return builder.build();
    }

    public static Uri buildMovieInfoQueryString(long api_id){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ApiHelper.API_SCHEMA)
                .authority(ApiHelper.TMDB_BASE_URL)
                .appendPath(ApiHelper.API_VERSION_PARAM)
                .appendPath(ApiHelper.API_MOVIE_PARAM)
                .appendPath(Long.toString(api_id))
                .appendQueryParameter(ApiHelper.API_KEY_URL_PARAM, ApiHelper.TMDB_API_KEY)
                .appendQueryParameter(ApiHelper.API_APPEND_TO_RESP, ApiHelper.API_VIDEOS_PARAM);


        return builder.build();
    }

    public static Uri buildYouTubeUri(String key){
        Uri.Builder builder = new Uri.Builder();

        builder.scheme(YouTubeApiHelper.API_SCHEMA)
                .authority(YouTubeApiHelper.YOU_TUBE_BASE_URL)
                .appendPath(YouTubeApiHelper.YOU_TUBE_WATCH_PATH)
                .appendQueryParameter(YouTubeApiHelper.YOUTUBE_VIDEO_PARAM, key);

        return builder.build();
    }
}
