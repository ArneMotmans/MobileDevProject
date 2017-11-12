package pxl.be.watchlist.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import pxl.be.watchlist.R;
import pxl.be.watchlist.adapters.MovieSearchAdapter;
import pxl.be.watchlist.domain.Movie;
import pxl.be.watchlist.domain.MovieDetails;
import pxl.be.watchlist.domain.MoviePage;
import pxl.be.watchlist.services.MovieApiService;
import pxl.be.watchlist.services.RetrofitBuilderService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity {

    private MovieApiService movieApiService;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setTitle("Search");

        Retrofit retrofit = RetrofitBuilderService.build(MovieApiService.BASE_URL);
        context = this;

        movieApiService = retrofit.create(MovieApiService.class);
        MovieSearchAdapter movieSearchAdapter = new MovieSearchAdapter(SearchActivity.this, new ArrayList<Movie>(), movieApiService);

        initializeSearchResultsList(movieSearchAdapter);
        openKeyBoard();
    }

    private void initializeSearchResultsList(MovieSearchAdapter movieSearchAdapter) {
        ListView seachResultListView = (ListView) findViewById(R.id.seachResultListView);
        seachResultListView.setAdapter(movieSearchAdapter);
        seachResultListView.setOnItemClickListener(searchResultClickListener);
        seachResultListView.setEmptyView(findViewById(R.id.emptySearchListTextView));
    }

    private void openKeyBoard(){
        SearchView movieSearchView = ((SearchView) findViewById(R.id.movieSearchView));
        movieSearchView.setQuery("",true);
        movieSearchView.setFocusable(true);
        movieSearchView.setIconified(false);
        movieSearchView.requestFocusFromTouch();
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
                movieApiService.searchMovie(newText, movieApiService.API_KEY).enqueue(new Callback<MoviePage>() {
                    @Override
                    public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                        MoviePage responseBody = response.body();
                        ListView seachResultListView = (ListView) findViewById(R.id.seachResultListView);
                        MovieSearchAdapter adapter = ((MovieSearchAdapter) seachResultListView.getAdapter());
                        adapter.setMovies(responseBody.getMovies());
                        adapter.notifyDataSetChanged();
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
        MenuItem item = menu.findItem(R.id.filterSpinnerItem);
        item.setVisible(false);
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
