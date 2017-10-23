package pxl.be.watchlist.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import pxl.be.watchlist.R;
import pxl.be.watchlist.adapters.MoviePostersAdapter;
import pxl.be.watchlist.adapters.MovieSearchAdapter;
import pxl.be.watchlist.domain.Movie;
import pxl.be.watchlist.domain.MovieDetails;
import pxl.be.watchlist.domain.MoviePage;
import pxl.be.watchlist.services.MovieApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;

public class SearchActivity extends AppCompatActivity {

    Retrofit retrofit;
    MovieApiService movieApiService;
    ListView seachResultListView;
    SearchView movieSearchView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        retrofit = new Retrofit.Builder()
                .baseUrl(MovieApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(GuavaCallAdapterFactory.create())
                .build();

        movieApiService = retrofit.create(MovieApiService.class);
        MovieSearchAdapter movieSearchAdapter = new MovieSearchAdapter(SearchActivity.this, new ArrayList<Movie>(), movieApiService);
        context = this;

        seachResultListView = (ListView) findViewById(R.id.seachResultListView);
        seachResultListView.setAdapter(movieSearchAdapter);
        seachResultListView.setOnItemClickListener(searchResultClickListener);

        movieSearchView = ((SearchView) findViewById(R.id.movieSearchView));
        movieSearchView.setOnQueryTextListener(searchInputListener);
    }

    private AdapterView.OnItemClickListener searchResultClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            movieApiService.getMovieDetails(l, MovieApiService.API_KEY).enqueue(new Callback<MovieDetails>() {
                @Override
                public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                    MovieDetails responseBody = response.body();
                    Intent intent = new Intent(context, MovieDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("movieDetails", responseBody);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

                @Override
                public void onFailure(Call<MovieDetails> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    };

    private SearchView.OnQueryTextListener searchInputListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (!newText.equals("")) {
                movieApiService.searchMovie(movieApiService.API_KEY, newText).enqueue(new Callback<MoviePage>() {
                    @Override
                    public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                        MoviePage responseBody = response.body();
                        ListView seachResultListView = (ListView) findViewById(R.id.seachResultListView);
                        MovieSearchAdapter adapter = ((MovieSearchAdapter) seachResultListView.getAdapter());
                        adapter.setMovies(responseBody.getMovies());
                        adapter.notifyDataSetChanged();
                        //movieSearchView.clearFocus();
                    }

                    @Override
                    public void onFailure(Call<MoviePage> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
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
}
