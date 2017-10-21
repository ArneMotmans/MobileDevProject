package com.example.a11502064.watchlist.services;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by 11501537 on 14/10/2017.
 */

public class ImageApiService {
    public static final String BASE_URL = "https://image.tmdb.org/t/p/w500/";

    public static void GetImageAndSetIntoImageView(Context context, String url, ImageView imageView){
        Picasso.with(context).load(BASE_URL+url).into(imageView);
    }
}
