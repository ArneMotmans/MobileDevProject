package com.example.a11502064.watchlist.adapters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.a11502064.watchlist.R;
import com.example.a11502064.watchlist.activities.HomePageActivity;
import com.example.a11502064.watchlist.domain.Movie;
import com.example.a11502064.watchlist.domain.MovieDetails;
import com.example.a11502064.watchlist.domain.MoviePage;
import com.example.a11502064.watchlist.fragments.MovieDetailsFragment;
import com.example.a11502064.watchlist.services.ImageApiService;
import com.example.a11502064.watchlist.services.MovieApiService;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 11501537 on 17/10/2017.
 */

public class MoviePostersAdapter extends ArrayAdapter<Movie> {
    private Context context;
    public List<Movie> movies;
    private FragmentManager fragmentManager;
    private MovieApiService movieApiService;

    public MoviePostersAdapter(@NonNull Context context, @NonNull List<Movie> movies, FragmentManager fragmentManager, MovieApiService movieApiService) {
        super(context, -1, movies);
        this.context = context;
        this.movies = movies;
        this.fragmentManager = fragmentManager;
        this.movieApiService = movieApiService;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        ImageView imageView;
        if (convertView == null){
            imageView = new ImageView(context);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setAdjustViewBounds(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieApiService.getMovieDetails(movies.get(position).getId(), movieApiService.API_KEY).enqueue(new Callback<MovieDetails>() {
                    @Override
                    public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                        MovieDetails responseBody = response.body();
                    }

                    @Override
                    public void onFailure(Call<MovieDetails> call, Throwable t) {

                    }
                });
            }
        });

        Picasso.with(context)
                .load(ImageApiService.BASE_URL+movies.get(position).getPosterPath())
                .placeholder(R.drawable.ic_poster_placeholder)
                .error(R.drawable.ic_poster_error)
                .into(imageView);
        return imageView;
    }

    public void addMovies(List<Movie> moviesToAdd){
        movies.addAll(moviesToAdd);
    }
}
