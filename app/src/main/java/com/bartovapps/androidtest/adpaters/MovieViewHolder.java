package com.bartovapps.androidtest.adpaters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bartovapps.androidtest.R;

/**
 * Created by motibartov on 03/04/2017.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder {

    ImageView ivMovieImage;


    public MovieViewHolder(View itemView) {
        super(itemView);
        ivMovieImage = (ImageView) itemView.findViewById(R.id.ivMovieImage);
    }

    public ImageView getIvMovieImage() {
        return ivMovieImage;
    }
}
