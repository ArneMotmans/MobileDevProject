package pxl.be.watchlist.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pxl.be.watchlist.R;
import pxl.be.watchlist.adapters.MoviePostersAdapter;
import pxl.be.watchlist.domain.Movie;
import pxl.be.watchlist.domain.MoviePage;
import pxl.be.watchlist.services.MovieApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;

public class MainActivity extends AppCompatActivity {

    Retrofit retrofit;
    MovieApiService movieApiService;
    GridView moviePostersGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Home");

        retrofit = new Retrofit.Builder()
                .baseUrl(MovieApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(GuavaCallAdapterFactory.create())
                .build();

        movieApiService = retrofit.create(MovieApiService.class);
        moviePostersGridView = (GridView) findViewById(R.id.moviePostersGridView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        Spinner filterSpinner = (Spinner)menu.findItem(R.id.filterSpinnerItem).getActionView();
        ArrayAdapter filtersAdapter = new ArrayAdapter(this, R.layout.spinner_item_layout, R.id.spinnerItem);
        filtersAdapter.addAll(getResources().getStringArray(R.array.homePageFilters));
        filterSpinner.setAdapter(filtersAdapter);
        filterSpinner.setOnItemSelectedListener(filterSpinnerItemClickListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.homeMenuItem:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.watchListMenuItem:
                startActivity(new Intent(this, WatchListActivity.class));
                break;
            case R.id.watchedListMenuItem:
                startActivity(new Intent(this, WatchedListActivity.class));
                break;
            case R.id.searchListMenuItem:
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }
        return true;
    }

    private AdapterView.OnItemSelectedListener filterSpinnerItemClickListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch (i){
                case 0:
                    onPopularTabClicked();
                    break;
                case 1:
                    onTopRatedClicked();
                    break;
                case 2:
                    onOutNowClicked();
                    break;
                case 3:
                    onUpcomingClicked();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private void onUpcomingClicked() {
        MoviePostersAdapter moviePostersAdapter = new MoviePostersAdapter(MainActivity.this, new ArrayList<Movie>(), movieApiService);
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
                            return o2.getPopularity().compareTo(o1.getPopularity());
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
        MoviePostersAdapter moviePostersAdapter = new MoviePostersAdapter(MainActivity.this, new ArrayList<Movie>(), movieApiService);
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
                            return o2.getVoteAverage().compareTo(o1.getVoteAverage());
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
        MoviePostersAdapter moviePostersAdapter = new MoviePostersAdapter(MainActivity.this, new ArrayList<Movie>(), movieApiService);
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
                            return o1.getPopularity().compareTo(o2.getPopularity());
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

    private void onOutNowClicked(){
        MoviePostersAdapter moviePostersAdapter = new MoviePostersAdapter(MainActivity.this, new ArrayList<Movie>(), movieApiService);
        moviePostersGridView.setAdapter(moviePostersAdapter);

        for (int i = 1; i <= 5; i++) {
            movieApiService.getNowPlayingMovies(i, MovieApiService.API_KEY).enqueue(new Callback<MoviePage>() {
                @Override
                public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                    MoviePage responseBody = response.body();
                    GridView moviePostersGridView = (GridView) findViewById(R.id.moviePostersGridView);
                    MoviePostersAdapter adapter = ((MoviePostersAdapter) moviePostersGridView.getAdapter());
                    adapter.addMovies(responseBody.getMovies());
                    Collections.sort(adapter.movies, new Comparator<Movie>() {
                        @Override
                        public int compare(Movie o1, Movie o2) {
                            return o2.getPopularity().compareTo(o1.getPopularity());
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
