package com.example.a11502064.watchlist.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a11502064.watchlist.R;
import com.example.a11502064.watchlist.domain.MovieDetails;
import com.example.a11502064.watchlist.services.MovieApiService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;

/**
 * Created by 11501537 on 17/10/2017.
 */

public class MovieDetailsFragment extends Fragment {

    private MovieApiService movieApiService;

    public MovieDetailsFragment() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(GuavaCallAdapterFactory.create())
                .build();

        movieApiService = retrofit.create(MovieApiService.class);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movieApiService.getMovieDetails(getArguments().getLong("id"), MovieApiService.API_KEY).enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                MovieDetails movieDetails = new Gson().fromJson(getArguments().getString("movieDetails"), MovieDetails.class);
                ((TextView) view.findViewById(R.id.movieTitleTextView)).setText(movieDetails.getTitle());
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                //show error
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_details, container, false);
    }
}
