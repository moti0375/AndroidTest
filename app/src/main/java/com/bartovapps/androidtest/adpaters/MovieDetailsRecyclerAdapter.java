package com.bartovapps.androidtest.adpaters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bartovapps.androidtest.R;
import com.bartovapps.androidtest.model.Movie;
import com.bartovapps.androidtest.model.Trailer;
import com.bartovapps.androidtest.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by BartovMoti on 11/08/15.
 */
public class MovieDetailsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = MovieDetailsRecyclerAdapter.class.getSimpleName();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private LayoutInflater inflater;
    private List<Trailer> data = new ArrayList<>();
    private Movie movie;
    private Activity context;
    private MoviesRecyclerItemClickListener clickListener;
    private SimpleDateFormat apiDateFormat;
    private SimpleDateFormat appDateFormat;

    public MovieDetailsRecyclerAdapter(Activity context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        appDateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    }

    public void setItemClickedListener(MoviesRecyclerItemClickListener clickListener) {

        this.clickListener = clickListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                View headerView = inflater.inflate(R.layout.header_list_item, parent, false);
                return new HeaderViewHolder(headerView);
            case TYPE_ITEM:
                View itemView = inflater.inflate(R.layout.video_list_item, parent, false);
                return new ItemViewHolder(itemView);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            Log.i(TAG, "onBindViewHolder instanceof ItemViewHolder");

            ((HeaderViewHolder) holder).tvTitle = (TextView) ((HeaderViewHolder) holder).itemView.findViewById(R.id.tvTitle);
            ((HeaderViewHolder) holder).ivHeaderImage = (ImageView) ((HeaderViewHolder) holder).itemView.findViewById(R.id.ivDetailedImage);
            ((HeaderViewHolder) holder).tvReleased = (TextView) ((HeaderViewHolder) holder).itemView.findViewById(R.id.tvReleased);
            ((HeaderViewHolder) holder).tvDuration = (TextView) ((HeaderViewHolder) holder).itemView.findViewById(R.id.tvDuration);
            ((HeaderViewHolder) holder).tvRating = (TextView) ((HeaderViewHolder) holder).itemView.findViewById(R.id.tvRating);
            ((HeaderViewHolder) holder).tvOverview = (TextView) ((HeaderViewHolder) holder).itemView.findViewById(R.id.tvOverview);

            ((HeaderViewHolder) holder).tvTitle.setText(movie.title);
           // Picasso.with(context).load(Utils.buildImageUri(movie.poster_path)).fit().centerCrop().into(((HeaderViewHolder) holder).ivHeaderImage);
            Glide.with(context)
                    .load(Utils.buildImageUri(movie.poster_path))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(((HeaderViewHolder) holder).ivHeaderImage);

            try {
                Date date = apiDateFormat.parse(movie.release_date);

                ((HeaderViewHolder) holder).tvReleased.setText(appDateFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((HeaderViewHolder) holder).tvDuration.setText(movie.runtime + "Min");
            ((HeaderViewHolder) holder).tvRating.setText(movie.vote_average + "/10");
            ((HeaderViewHolder) holder).tvOverview.setText(movie.overview);


            //cast holder to VHItem and set data
        } else if (holder instanceof ItemViewHolder) {
            Log.i(TAG, "onBindViewHolder instanceof ItemViewHolder");
            ((ItemViewHolder) holder).tvVideoTitle = (TextView) ((ItemViewHolder) holder).itemView.findViewById(R.id.tvVideoTitle);
//            ((ItemViewHolder) holder).tvVideoTitle.setText(data.get(position - 1).name);
            ((ItemViewHolder) holder).tvVideoTitle.setText(data.get(position).name);

            ((ItemViewHolder) holder).itemView.setOnClickListener(view -> {
                Toast.makeText(context, "Trailer " + (position) + "was clicked..", Toast.LENGTH_SHORT).show();
                clickListener.onItemClicked(data.get(position));
            });

            //cast holder to VHHeader and set data for header.
        }
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == 0)
//            return TYPE_HEADER;

        return TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        int count;
        if (movie == null) {
            count = 0;
        } else {
            if (data.isEmpty()) { // No trailers, there will be header only
//                count = 1;
                count = 0;
            } else {
                //count = data.size() + 1; //Number of trailers + header
                count = data.size();
            }
        }

        Log.i(TAG, "get item count: " + count);
        return count;
    }


    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private ImageView ivHeaderImage;
        private TextView tvReleased;
        private TextView tvDuration;
        private TextView tvRating;
        private TextView tvOverview;
        private View itemView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            //drawerRowIcon.setOnClickListener(this);
            this.itemView = itemView;
        }

    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tvVideoTitle;
        private View itemView;

        private ItemViewHolder(View itemView) {
            super(itemView);
            //drawerRowIcon.setOnClickListener(this);
            this.itemView = itemView;
        }

    }


    public void updateVideos(Movie movie) {
        this.movie = movie;
        if (this.data != null) {
            this.data.clear();
        }

        this.data.addAll(this.movie.videos.results);
        Log.i(TAG, "data size: " + this.data.size());
        context.runOnUiThread(() -> notifyDataSetChanged());
    }

    public interface MoviesRecyclerItemClickListener {
        void onItemClicked(Trailer trailer);
    }


}
