package pxl.be.watchlist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import pxl.be.watchlist.R;


public class WatchedListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watched_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.findItem(R.id.filterSpinner).getActionView().setVisibility(View.GONE);
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
        }
        return true;
    }
}
