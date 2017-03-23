package com.bartovapps.androidtest.parsers;

import android.net.Uri;
import android.util.Log;

import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.model.Trailer;

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

    public static final String API_ID = "id";
    public static final String TITLE = "name";
    public static final String OVERVIEW = "overview";
    public static final String DURATION = "runtime";
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
//                Movie movie = new Movie(obj.getLong(API_ID), obj.getString(TITLE), obj.getString(OVERVIEW), "" + c.get(Calendar.YEAR), 0, obj.getDouble(RATING), obj.getString(IMAGE_PATH));
                Movie movie = new Movie();

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


    public static Movie parseMovieDetailsFromResponse(String api_response) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar c = Calendar.getInstance();

        Date date;
        JSONObject reader = null;
        try {
            reader = new JSONObject(api_response);

            date = sdf.parse(reader.getString(RELEASE_DATE));
            c.setTime(date);

            JSONObject videos = reader.getJSONObject("videos");
            if (videos != null) {
                JSONArray videos_results = videos.getJSONArray("results");
                Log.i(TAG, "parseMovieDetailsFromResponse: found " + videos_results + " videos..");
                List<Trailer> movie_trailers = new ArrayList<>();
                for (int i = 0; i < videos_results.length(); i++) {
                    JSONObject obj = videos_results.getJSONObject(i);

                    Trailer trailer = new Trailer();
                    trailer.key = (obj.getString("key"));
                    trailer.name = (obj.getString("name"));
                    movie_trailers.add(trailer);
                }
                //Movie movie = new Movie(reader.getLong(API_ID), reader.getString(TITLE), reader.getString(OVERVIEW),  "" + c.get(Calendar.YEAR), reader.getLong(DURATION), reader.getDouble(RATING), reader.getString(IMAGE_PATH));
                Movie movie = new Movie();
                movie.trailers = movie_trailers;
                return movie;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Uri buildImageUri(JSONObject obj) {

        return null;
    }
}
