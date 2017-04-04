package com.bartovapps.androidtest.utils;

import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.bartovapps.androidtest.R.id.tvReleased;

/**
 * Created by motibartov on 04/04/2017.
 */

public class ReleasedDisplayer implements Displayer {


    TextView mTextView;
    private SimpleDateFormat apiDateFormat;
    private SimpleDateFormat appDateFormat;


    public ReleasedDisplayer(TextView textView){
        mTextView = textView;
        apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        appDateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    }

    @Override
    public void setText(String dateStr) {
        Date date = null;
        try {
            date = apiDateFormat.parse(dateStr);
            mTextView.setText(appDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
