package com.example.a11502064.watchlist.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.a11502064.watchlist.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class MovieDetailsActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener{

    public static final String API_KEY = "AIzaSyCzyQUfjEdK1UtZ-RgfcgzmXp6GH584rY8";

    public static final String VIDEO_ID = "tAV_ehyZmTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        YouTubePlayerSupportFragment frag =
                (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        frag.initialize(API_KEY, this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer.cueVideo(VIDEO_ID);
        }
    }

    @Override
        public void onInitializationFailure (YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
            Toast.makeText(getApplicationContext(), "It failed", Toast.LENGTH_LONG).show();
        }
    }
