package com.bartovapps.androidtest.parsers;

import android.net.Uri;
import android.util.Log;

import com.bartovapps.androidtest.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar c = Calendar.getInstance();

        Date date;
        List<Movie> movies = new ArrayList<>();

        try {

            JSONObject reader = new JSONObject(content);


            JSONArray ar = reader.getJSONArray("results");

            Log.i(TAG, "Json array size " + ar.length());


            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                date = sdf.parse(obj.getString(RELEASE_DATE));
                c.setTime(date);
                Movie movie = new Movie(obj.getString(TITLE), obj.getString(OVERVIEW), "" + c.get(Calendar.YEAR), 0, obj.getDouble(RATING), obj.getString(IMAGE_PATH));

                Log.i(TAG, movie.toString());


                movies.add(movie);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return movies;

    }

    private static Uri buildImageUri(JSONObject obj) {

        return null;
    }
}
