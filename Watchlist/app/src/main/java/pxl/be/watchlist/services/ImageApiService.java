package pxl.be.watchlist.services;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class ImageApiService {
    public static final String BASE_URL = "https://image.tmdb.org/t/p/w500/";

    public static void GetImageAndSetIntoImageView(Context context, String url, ImageView imageView){
        Picasso.with(context).load(BASE_URL+url).into(imageView);
    }
}
