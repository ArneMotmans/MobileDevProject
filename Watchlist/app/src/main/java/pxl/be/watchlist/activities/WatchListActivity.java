package pxl.be.watchlist.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import pxl.be.watchlist.R;
import pxl.be.watchlist.adapters.MovieSearchAdapter;
import pxl.be.watchlist.databaaaz.WatchList;
import pxl.be.watchlist.domain.Movie;

public class WatchListActivity extends AppCompatActivity {

    Context context;
    ListView watchListListView;
    List<WatchList> watchListMovies;
    Movie movie;
    static int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

        watchListMovies = SQLite.select().
                from(WatchList.class).queryList();


        MovieSearchAdapter movieSearchAdapter = new MovieSearchAdapter(WatchListActivity.this, watchListMovies, null);
        context = getApplicationContext();

        watchListListView = (ListView) findViewById(R.id.watchListListView);
        watchListListView.setAdapter(movieSearchAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.findItem(R.id.filterSpinnerItem).getActionView().setVisibility(View.GONE);
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
