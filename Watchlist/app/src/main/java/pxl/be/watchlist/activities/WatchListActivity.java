package pxl.be.watchlist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import pxl.be.watchlist.R;
import pxl.be.watchlist.adapters.WatchListAdapter;
import pxl.be.watchlist.database.WatchList;
import pxl.be.watchlist.domain.MovieDetails;
import pxl.be.watchlist.services.MovieApiService;
import pxl.be.watchlist.services.RetrofitBuilderService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static pxl.be.watchlist.services.MovieApiService.API_KEY;
import static pxl.be.watchlist.services.MovieApiService.BASE_URL;

public class WatchListActivity extends AppCompatActivity {

    private ListView watchListListView;
    private MovieApiService movieApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

        Retrofit retrofit = RetrofitBuilderService.build(BASE_URL);
        movieApiService = retrofit.create(MovieApiService.class);

        loadWatchlistMovies();
    }

    private void loadWatchlistMovies() {
        List<WatchList> movieIds = SQLite.select().
                from(WatchList.class).queryList();

        WatchListAdapter watchListAdapter = new WatchListAdapter(this, new ArrayList<MovieDetails>(), movieApiService, true);
        watchListListView = (ListView) findViewById(R.id.watchListListView);
        watchListListView.setAdapter(watchListAdapter);
        watchListListView.setEmptyView(findViewById(R.id.emptyWatchListTextView));

        for (WatchList watchlistMovie : movieIds) {
            movieApiService.getMovieDetails(watchlistMovie.getId(), API_KEY).enqueue(new Callback<MovieDetails>() {
                @Override
                public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                    MovieDetails responseBody = response.body();
                    WatchListAdapter watchListAdapter = (WatchListAdapter) watchListListView.getAdapter();
                    watchListAdapter.movies.add(responseBody);
                    watchListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MovieDetails> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

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
