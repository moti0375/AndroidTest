package com.bartovapps.androidtest.parsers;

import android.net.Uri;
import android.util.Log;

import com.bartovapps.androidtest.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BartovMoti on 01/24/17.
 */
public class MoviesJsonParser {

    private static final String TAG = MoviesJsonParser.class.getSimpleName();

    public static final String TITLE = "title";
    public static final String OVERVIEW = "overview";
    public static final String IMAGE_PATH = "poster_path";
    public static final String RELEASE_DATE = "release_date";
    public static final String RATING = "vote_average";



    public static List<Movie> parseFeed(String content) {
        try {

            JSONObject reader = new JSONObject(content);

            JSONArray ar =  reader.getJSONArray("results");

            Log.i(TAG, "Json array size " + ar.length());

            List<Movie> movies = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Movie movie = new Movie(obj.getString(TITLE), obj.getString(OVERVIEW), obj.getString(RELEASE_DATE), 0, obj.getDouble(RATING), obj.getString(IMAGE_PATH));

                Log.i(TAG, movie.toString());


                movies.add(movie);

                }

            return movies;
        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Uri buildImageUri(JSONObject obj) {

        return null;
    }
}
