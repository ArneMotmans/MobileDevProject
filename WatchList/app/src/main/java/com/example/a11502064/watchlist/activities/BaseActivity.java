package com.example.a11502064.watchlist.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.a11502064.watchlist.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuoptions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuHome){
            startActivity(new Intent(this, MainActivity.class));

        } else if (id == R.id.menuWatchlist){
            startActivity(new Intent(this, Main2Activity.class));

        } else if (id == R.id.menuWatched){
            startActivity(new Intent(this, Main3Activity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    public void basicToastPopular(View view){
        Context context = getApplicationContext();
        CharSequence text = "You clicked the 'Popular' tab";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,text,duration);
        toast.show();
    }

    public void basicToastTopRated(View view){
        Context context = getApplicationContext();
        CharSequence text = "You clicked the 'Top Rated' tab";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,text,duration);
        toast.show();
    }

    public void basicToastUpcoming(View view){
        Context context = getApplicationContext();
        CharSequence text = "You clicked the 'Upcoming' tab";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,text,duration);
        toast.show();
    }
}
