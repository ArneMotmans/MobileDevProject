package com.example.a11502064.watchlist.activities;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.a11502064.watchlist.R;
import com.example.a11502064.watchlist.adapters.MoviePostersAdapter;
import com.example.a11502064.watchlist.domain.Movie;
import com.example.a11502064.watchlist.domain.MoviePage;
import com.example.a11502064.watchlist.fragments.MovieDetailsFragment;
import com.example.a11502064.watchlist.services.MovieApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;

public class HomePageActivity extends BaseActivity {

    Retrofit retrofit;
    MovieApiService movieApiService;
    GridView moviePostersGridView;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        retrofit = new Retrofit.Builder()
                .baseUrl(MovieApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(GuavaCallAdapterFactory.create())
                .build();

        movieApiService = retrofit.create(MovieApiService.class);
        moviePostersGridView = (GridView) findViewById(R.id.moviePostersGridView);
        fragmentManager = getFragmentManager();

        Button popularButton = (Button) findViewById(R.id.popularButton);
        Button topRatedButton = (Button) findViewById(R.id.topRatedButton);
        Button upcomingButton = (Button) findViewById(R.id.upcomingButton);

        popularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopularTabClicked();
            }
        });

        topRatedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTopRatedClicked();
            }
        });

        upcomingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpcomingClicked();
            }
        });
    }

    private void onUpcomingClicked() {
        MoviePostersAdapter moviePostersAdapter = new MoviePostersAdapter(HomePageActivity.this, new ArrayList<Movie>(), fragmentManager, movieApiService);
        moviePostersGridView.setAdapter(moviePostersAdapter);

        for (int i = 1; i <= 5; i++) {
            movieApiService.getUpcomingMovies(i,movieApiService.API_KEY).enqueue(new Callback<MoviePage>() {
                @Override
                public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                    MoviePage responseBody = response.body();
                    GridView moviePostersGridView = (GridView) findViewById(R.id.moviePostersGridView);
                    MoviePostersAdapter adapter = ((MoviePostersAdapter) moviePostersGridView.getAdapter());
                    adapter.addMovies(responseBody.getMovies());
                    Collections.sort(moviePostersAdapter.movies, (m1,m2) -> (int)(m1.getPopularity() - m2.getPopularity()));
                    moviePostersAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MoviePage> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void onTopRatedClicked() {
        MoviePostersAdapter moviePostersAdapter = new MoviePostersAdapter(HomePageActivity.this, new ArrayList<Movie>(), fragmentManager, movieApiService);
        moviePostersGridView.setAdapter(moviePostersAdapter);

        for (int i = 1; i <= 5; i++) {
            movieApiService.getTopRatedMovies(i,movieApiService.API_KEY).enqueue(new Callback<MoviePage>() {
                @Override
                public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                    MoviePage responseBody = response.body();
                    GridView moviePostersGridView = (GridView) findViewById(R.id.moviePostersGridView);
                    MoviePostersAdapter adapter = ((MoviePostersAdapter) moviePostersGridView.getAdapter());
                    adapter.addMovies(responseBody.getMovies());
                    Collections.sort(moviePostersAdapter.movies, (m1,m2) -> (int)(m1.getVoteCount() - m2.getVoteCount()));
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MoviePage> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void onPopularTabClicked(){
        MoviePostersAdapter moviePostersAdapter = new MoviePostersAdapter(HomePageActivity.this, new ArrayList<Movie>(), fragmentManager, movieApiService);
        moviePostersGridView.setAdapter(moviePostersAdapter);

        for (int i = 1; i <= 5; i++) {
            movieApiService.getPopularMovies(i,movieApiService.API_KEY).enqueue(new Callback<MoviePage>() {
                @Override
                public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                    MoviePage responseBody = response.body();
                    GridView moviePostersGridView = (GridView) findViewById(R.id.moviePostersGridView);
                    MoviePostersAdapter adapter = ((MoviePostersAdapter) moviePostersGridView.getAdapter());
                    adapter.addMovies(responseBody.getMovies());
                    Collections.sort(moviePostersAdapter.movies, (m1,m2) ->  (int)(m1.getPopularity() - m2.getPopularity()));
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MoviePage> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}
