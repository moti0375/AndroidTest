package com.bartovapps.androidtest.data;

import android.provider.BaseColumns;

/**
 * Created by BartovMoti on 01/24/17.
 */
public class DbContract {

    public static final class MoviesEntry implements BaseColumns {
        public static final String TABLE_NAME = "results";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_API_ID = "api_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASED = "released";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_IMAGE_URI = "image_uri";
    }
}
