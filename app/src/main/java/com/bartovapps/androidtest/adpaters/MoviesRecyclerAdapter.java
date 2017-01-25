package com.bartovapps.androidtest.adpaters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bartovapps.androidtest.R;
import com.bartovapps.androidtest.data.DbContract;
import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BartovMoti on 01/24/17.
 */
public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.RecyclerViewHolder> {

    private static final String TAG = MoviesRecyclerAdapter.class.getSimpleName();
    LayoutInflater inflater;
    List<Movie> movies = new ArrayList<>();
    Context mContext;
    AdapterEventListener mAdapterEventListener;
    CursorAdapter mCursorAdapter;
    Cursor mCursor;
    boolean mDataValid;


//    public MoviesRecyclerAdapter(Context context, List<Movie> movies) {
//        inflater = LayoutInflater.from(context);
//        this.mContext = context;
//        this.movies = movies;
//    }

    public MoviesRecyclerAdapter(Context context, Cursor c) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        mCursor = c;

        mCursorAdapter = new CursorAdapter(mContext, c, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = inflater.inflate(R.layout.movie_list_item, parent, false);

                return view;
            }

            @Override
            public void bindView(View view, Context context, final Cursor cursor) {
                Log.i(TAG, "onBindViewHolder: cursor position: " + cursor.getPosition());
                ImageView imageView = (ImageView) view.findViewById(R.id.ivMovieImage);


                if (cursor != null && cursor.getCount() > 0) {
                    Uri imageUri = Utils.buildImageUri(cursor.getString(cursor.getColumnIndex(DbContract.MoviesEntry.COLUMN_IMAGE_URI)));
                    Picasso.with(mContext).load(imageUri).fit().centerCrop().into(imageView);
//                    view.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            Toast.makeText(mContext, "Item " + cursor.getPosition() + " was clicked", Toast.LENGTH_SHORT).show();
//                            if (mAdapterEventListener != null) {
//                                //mAdapterEventListener.itemClicked(movies.get(position));
//                            }
//                        }
//                    });
                }
                //  holder.itemView.setActivated(selectedItems.get(position, false));
            }
        };

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {

        mCursorAdapter.getCursor().moveToPosition(position); //EDITED: added this line as suggested in the comments below, thanks :)
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());


    }

    @Override
    public int getItemCount() {

        if (mDataValid) {
            Log.i(TAG, "getItemCount: " + mCursorAdapter.getCount());
            return mCursorAdapter.getCount();
        }
        return 0;

    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView movieImage;

        View mView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            movieImage = (ImageView) itemView.findViewById(R.id.ivMovieImage);
            //drawerRowIcon.setOnClickListener(this);
            mView = itemView;

        }

    }

    public void updateCursor(Cursor data) {
        Log.i(TAG, "updateCursor called");
        mCursor = data;
        if (data != null) {
            mDataValid = true;
            Log.i(TAG, "New cursor size: " + data.getCount());


            Cursor oldCursor = mCursorAdapter.swapCursor(data);
            if (oldCursor != null) {
                oldCursor.close();
                Log.i(TAG, "old cursor closed");
            }
        }
        notifyDataSetChanged();
    }


    public void setAdapterEventListener(AdapterEventListener adapterEventListener) {
        mAdapterEventListener = adapterEventListener;
    }


    public interface AdapterEventListener {
        void itemClicked(Movie movie);
    }


}
