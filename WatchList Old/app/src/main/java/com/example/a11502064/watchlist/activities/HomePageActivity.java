package com.example.a11502064.watchlist.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toolbar;

import com.example.a11502064.watchlist.R;
import com.example.a11502064.watchlist.adapters.MoviePostersAdapter;
import com.example.a11502064.watchlist.domain.Movie;
import com.example.a11502064.watchlist.domain.MoviePage;
import com.example.a11502064.watchlist.services.MovieApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;

public class HomePageActivity extends AppCompatActivity {

    Retrofit retrofit;
    MovieApiService movieApiService;
    GridView moviePostersGridView;

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

        onPopularTabClicked();

//        Button popularButton = (Button) findViewById(R.id.popularButton);
//        Button topRatedButton = (Button) findViewById(R.id.topRatedButton);
//        Button upcomingButton = (Button) findViewById(R.id.upcomingButton);
//
//        popularButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onPopularTabClicked();
//            }
//        });
//
//        topRatedButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onTopRatedClicked();
//            }
//        });
//
//        upcomingButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onUpcomingClicked();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void onUpcomingClicked() {
        MoviePostersAdapter moviePostersAdapter = new MoviePostersAdapter(HomePageActivity.this, new ArrayList<Movie>(), movieApiService);
        moviePostersGridView.setAdapter(moviePostersAdapter);

        for (int i = 1; i <= 5; i++) {
            movieApiService.getUpcomingMovies(i, MovieApiService.API_KEY).enqueue(new Callback<MoviePage>() {
                @Override
                public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                    MoviePage responseBody = response.body();
                    GridView moviePostersGridView = (GridView) findViewById(R.id.moviePostersGridView);
                    MoviePostersAdapter adapter = ((MoviePostersAdapter) moviePostersGridView.getAdapter());
                    adapter.addMovies(responseBody.getMovies());
                    Collections.sort(adapter.movies, new Comparator<Movie>() {
                        @Override
                        public int compare(Movie o1, Movie o2) {
                            return (int)(o1.getPopularity() - o2.getPopularity());
                        }
                    });
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MoviePage> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void onTopRatedClicked() {
        MoviePostersAdapter moviePostersAdapter = new MoviePostersAdapter(HomePageActivity.this, new ArrayList<Movie>(), movieApiService);
        moviePostersGridView.setAdapter(moviePostersAdapter);

        for (int i = 1; i <= 5; i++) {
            movieApiService.getTopRatedMovies(i, MovieApiService.API_KEY).enqueue(new Callback<MoviePage>() {
                @Override
                public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                    MoviePage responseBody = response.body();
                    GridView moviePostersGridView = (GridView) findViewById(R.id.moviePostersGridView);
                    MoviePostersAdapter adapter = ((MoviePostersAdapter) moviePostersGridView.getAdapter());
                    adapter.addMovies(responseBody.getMovies());
                    Collections.sort(adapter.movies, new Comparator<Movie>() {
                        @Override
                        public int compare(Movie o1, Movie o2) {
                            return (int) (o1.getVoteCount() - o2.getVoteCount());
                        }
                    });
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
        MoviePostersAdapter moviePostersAdapter = new MoviePostersAdapter(HomePageActivity.this, new ArrayList<Movie>(), movieApiService);
        moviePostersGridView.setAdapter(moviePostersAdapter);

        for (int i = 1; i <= 5; i++) {
            movieApiService.getPopularMovies(i, MovieApiService.API_KEY).enqueue(new Callback<MoviePage>() {
                @Override
                public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                    MoviePage responseBody = response.body();
                    GridView moviePostersGridView = (GridView) findViewById(R.id.moviePostersGridView);
                    MoviePostersAdapter adapter = ((MoviePostersAdapter) moviePostersGridView.getAdapter());
                    adapter.addMovies(responseBody.getMovies());
                    Collections.sort(adapter.movies, new Comparator<Movie>() {
                        @Override
                        public int compare(Movie o1, Movie o2) {
                            return (int)(o1.getPopularity() - o2.getPopularity());
                        }
                    });
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
