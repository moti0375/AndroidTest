package com.bartovapps.androidtest.adpaters;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bartovapps.androidtest.R;
import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BartovMoti on 01/24/17.
 */
public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.RecyclerViewHolder> {

    private static final String LOG_TAG = MoviesRecyclerAdapter.class.getSimpleName();
    LayoutInflater inflater;
    List<Movie> movies = new ArrayList<>();
    Activity mContext;
    AdapterEventListener mAdapterEventListener;

    public MoviesRecyclerAdapter(Activity context, List<Movie> movies) {
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.movies = movies;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {

        Uri imageUri = Utils.buildImageUri(movies.get(position).getImageUrl());
        Log.i(LOG_TAG, "onBindViewHolder: " + imageUri.toString());

        Picasso.with(mContext).load(imageUri).fit().centerCrop().into(holder.movieImage);
      //  holder.itemView.setActivated(selectedItems.get(position, false));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(mContext, "Item " + position + " was clicked", Toast.LENGTH_SHORT).show();
                if(mAdapterEventListener != null){
                    mAdapterEventListener.itemClicked(movies.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
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


    public void updateList(List<Movie> data){
        this.movies.clear();
        this.movies.addAll(data);
        Log.i(LOG_TAG, "movies size: " + this.movies.size());
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });

    }

    public void setAdapterEventListener(AdapterEventListener adapterEventListener){
        mAdapterEventListener = adapterEventListener;
    }



    public interface AdapterEventListener{
        void itemClicked(Movie movie);
    }


}
