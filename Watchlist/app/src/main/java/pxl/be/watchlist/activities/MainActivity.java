package pxl.be.watchlist.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import pxl.be.watchlist.services.RetrofitBuilderService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private MovieApiService movieApiService;
    private GridView moviePostersGridView;
    private Tab selectedTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Home");

        Retrofit retrofit = RetrofitBuilderService.build(MovieApiService.BASE_URL);

        movieApiService = retrofit.create(MovieApiService.class);
        moviePostersGridView = (GridView) findViewById(R.id.moviePostersGridView);

        if (savedInstanceState == null) {
            selectedTab = Tab.POPULAR;
        }

        toggleNoMovieSelectedMessage(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        Spinner filterSpinner = (Spinner) menu.findItem(R.id.filterSpinnerItem).getActionView();
        ArrayAdapter filtersAdapter = new ArrayAdapter(this, R.layout.spinner_item_layout, R.id.spinnerItem);
        filtersAdapter.addAll(getResources().getStringArray(R.array.homePageFilters));
        filterSpinner.setAdapter(filtersAdapter);
        filterSpinner.setOnItemSelectedListener(filterSpinnerItemClickListener);
        filterSpinner.setSelection(selectedTab.index, false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedTab", selectedTab.index);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectedTab = Tab.getTab(savedInstanceState.getInt("selectedTab"));
    }

    private AdapterView.OnItemSelectedListener filterSpinnerItemClickListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            LoadTab(i);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

    private void onUpcomingClicked() {
        showNoInternetConnectionMessage();
        MoviePostersAdapter moviePostersAdapter = new MoviePostersAdapter(MainActivity.this, new ArrayList<Movie>(), movieApiService, getResources().getConfiguration().orientation);
        moviePostersGridView.setAdapter(moviePostersAdapter);
        selectedTab = Tab.UPCOMING;

        for (int i = 1; i <= 5; i++) {
            movieApiService
                    .getUpcomingMovies(i, MovieApiService.API_KEY)
                    .enqueue(getGetMoviesRequestCallBack(new Comparator<Movie>() {
                        @Override
                        public int compare(Movie o1, Movie o2) {
                            return o2.getPopularity().compareTo(o1.getPopularity());
                        }
                    }));
        }
    }

    private void onTopRatedClicked() {
        showNoInternetConnectionMessage();
        MoviePostersAdapter moviePostersAdapter = new MoviePostersAdapter(MainActivity.this, new ArrayList<Movie>(), movieApiService, getResources().getConfiguration().orientation);
        moviePostersGridView.setAdapter(moviePostersAdapter);
        selectedTab = Tab.TOP_RATED;

        for (int i = 1; i <= 5; i++) {
            movieApiService
                    .getTopRatedMovies(i, MovieApiService.API_KEY)
                    .enqueue(getGetMoviesRequestCallBack(new Comparator<Movie>() {
                        @Override
                        public int compare(Movie o1, Movie o2) {
                            return o2.getVoteAverage().compareTo(o1.getVoteAverage());
                        }
                    }));
        }
    }

    private void onPopularTabClicked() {
        showNoInternetConnectionMessage();
        MoviePostersAdapter moviePostersAdapter = new MoviePostersAdapter(MainActivity.this, new ArrayList<Movie>(), movieApiService, getResources().getConfiguration().orientation);
        moviePostersGridView.setAdapter(moviePostersAdapter);
        selectedTab = Tab.POPULAR;

        for (int i = 1; i <= 5; i++) {
            movieApiService
                    .getPopularMovies(i, MovieApiService.API_KEY)
                    .enqueue(getGetMoviesRequestCallBack(new Comparator<Movie>() {
                        @Override
                        public int compare(Movie o1, Movie o2) {
                            return o2.getPopularity().compareTo(o1.getPopularity());
                        }
                    }));
        }
    }

    private void onOutNowClicked() {
        showNoInternetConnectionMessage();
        MoviePostersAdapter moviePostersAdapter = new MoviePostersAdapter(MainActivity.this, new ArrayList<Movie>(), movieApiService, getResources().getConfiguration().orientation);
        moviePostersGridView.setAdapter(moviePostersAdapter);
        selectedTab = Tab.OUT_NOW;

        for (int i = 1; i <= 5; i++) {
            movieApiService
                    .getNowPlayingMovies(i, MovieApiService.API_KEY)
                    .enqueue(getGetMoviesRequestCallBack(new Comparator<Movie>() {
                        @Override
                        public int compare(Movie o1, Movie o2) {
                            return o2.getPopularity().compareTo(o1.getPopularity());
                        }
                    }));
        }
    }

    public static boolean isNetworkStatusAvialable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if (netInfos != null)
                if (netInfos.isConnected())
                    return true;
        }
        return false;
    }

    public void showNoInternetConnectionMessage() {
        if (isNetworkStatusAvialable(getApplicationContext())) {
            findViewById(R.id.noInternetTextView).setVisibility(View.GONE);
        } else {
            findViewById(R.id.noInternetTextView).setVisibility(View.VISIBLE);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                findViewById(R.id.movieDetailsContainer).setVisibility(View.GONE);
        }
    }

    private Callback<MoviePage> getGetMoviesRequestCallBack(final Comparator orderBy) {
        return new Callback<MoviePage>() {
            @Override
            public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                MoviePage responseBody = response.body();
                GridView moviePostersGridView = (GridView) findViewById(R.id.moviePostersGridView);
                MoviePostersAdapter adapter = ((MoviePostersAdapter) moviePostersGridView.getAdapter());
                adapter.addMovies(responseBody.getMovies());
                Collections.sort(adapter.getMovies(), orderBy);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MoviePage> call, Throwable t) {
                t.printStackTrace();
            }
        };
    };

    private void LoadTab(int tabIndex) {
        switch (tabIndex) {
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

    private enum Tab {
        POPULAR(0),
        TOP_RATED(1),
        OUT_NOW(2),
        UPCOMING(3);

        public int index;

        Tab(int index) {
            this.index = index;
        }

        public static Tab getTab(int i) {
            switch (i) {
                case 0:
                    return POPULAR;
                case 1:
                    return TOP_RATED;
                case 2:
                    return OUT_NOW;
                case 3:
                    return UPCOMING;
                default:
                    return POPULAR;
            }
        }
    }

    public void toggleNoMovieSelectedMessage(boolean visible){
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (visible) {
                findViewById(R.id.noDetailsSelectedTextView).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.noDetailsSelectedTextView).setVisibility(View.GONE);
            }
        }
    }
}
