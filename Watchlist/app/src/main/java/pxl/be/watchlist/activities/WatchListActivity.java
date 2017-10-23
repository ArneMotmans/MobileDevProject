package pxl.be.watchlist.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import pxl.be.watchlist.R;
import pxl.be.watchlist.adapters.MovieSearchAdapter;
import pxl.be.watchlist.domain.Movie;

public class WatchListActivity extends AppCompatActivity {

    Context context;
    ListView watchListListView;
    ArrayList<Movie> watchListMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

        Firebase.setAndroidContext(this);
        final Firebase myRef = new Firebase("https://watchlistapp-183207.firebaseio.com/");

        myRef.child("Watchlist").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    watchListMovies.add((Movie) dsp.getValue());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


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
