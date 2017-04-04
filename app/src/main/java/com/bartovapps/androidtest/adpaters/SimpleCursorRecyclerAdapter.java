package com.bartovapps.androidtest.adpaters;/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 ARNAUD FRUGIER
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bartovapps.androidtest.R;
import com.bartovapps.androidtest.adpaters.CursorRecyclerAdapter;
import com.bartovapps.androidtest.data.DbContract;
import com.bartovapps.androidtest.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

public class SimpleCursorRecyclerAdapter extends CursorRecyclerAdapter<MovieViewHolder> {
    public static final String TAG = SimpleCursorRecyclerAdapter.class.getSimpleName();

    private int mLayout;
    private int[] mFrom;
    private int[] mTo;
    private String[] mOriginalFrom;
    private Context mContext;
    MovieViewHolder mViewHolder;

    public SimpleCursorRecyclerAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(c);
        mLayout = layout;
        mTo = to;
        mOriginalFrom = from;
        mContext = context;
        findColumns(c, from);
    }

    public SimpleCursorRecyclerAdapter(Context context, Cursor c) {
        super(c);
        mContext = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        mViewHolder = new MovieViewHolder(v);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final Cursor cursor) {
        Uri imageUri = Utils.buildImageUri(cursor.getString(cursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_IMAGE_URI)));
        Log.i(TAG, "onBindViewHolder image Uri:  " + imageUri);
//        Picasso.with(mContext).load(imageUri).fit().centerCrop().into(holder.ivMovieImage);
        Glide.with(mContext)
                .load(imageUri)
                .centerCrop()
                .crossFade(200)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.ivMovieImage);

    }

    /**
     * Create a map from an array of strings to an array of column-id integers in cursor c.
     * If c is null, the array will be discarded.
     *
     * @param c    the cursor to find the columns from
     * @param from the Strings naming the columns of interest
     */
    private void findColumns(Cursor c, String[] from) {
        if (c != null) {
            int i;
            int count = from.length;
            if (mFrom == null || mFrom.length != count) {
                mFrom = new int[count];
            }
            for (i = 0; i < count; i++) {
                mFrom[i] = c.getColumnIndexOrThrow(from[i]);
            }
        } else {
            mFrom = null;
        }
    }

    @Override
    public Cursor swapCursor(Cursor c) {
        // findColumns(c, mOriginalFrom);
        return super.swapCursor(c);
    }

    public MovieViewHolder getViewHolder() {
        return mViewHolder;
    }

}


