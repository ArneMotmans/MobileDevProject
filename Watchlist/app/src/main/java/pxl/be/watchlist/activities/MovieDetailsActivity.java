package pxl.be.watchlist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import pxl.be.watchlist.R;
import pxl.be.watchlist.database.WatchList;
import pxl.be.watchlist.domain.MovieDetails;
import pxl.be.watchlist.domain.TrailersPage;
import pxl.be.watchlist.services.DatabaseService;
import pxl.be.watchlist.services.ImageApiService;
import pxl.be.watchlist.services.LanguageService;
import pxl.be.watchlist.services.MovieApiService;
import pxl.be.watchlist.services.ReleaseDateService;
import pxl.be.watchlist.services.RetrofitBuilderService;
import pxl.be.watchlist.services.RunTimeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static pxl.be.watchlist.R.anim;
import static pxl.be.watchlist.R.drawable;
import static pxl.be.watchlist.R.id;
import static pxl.be.watchlist.R.layout;
import static pxl.be.watchlist.utilities.ViewRetrieverUtility.getView;

public class MovieDetailsActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {

    private MovieApiService movieApiService;
    private MovieDetails movieDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_movie_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Details");

        Retrofit retrofit = RetrofitBuilderService.build(MovieApiService.BASE_URL);
        movieApiService = retrofit.create(MovieApiService.class);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        movieDetails = (MovieDetails) bundle.get("movieDetails");

        initializeAddToWatchlistButton();
        showMovieDetails(movieDetails);
        initializeYoutubePlayer();
    }

    private void disableAddToWatchlistButton(String text) {
        Button button = getView(this, id.addToWatchListButton, Button.class);
        button.setText(text);
        button.setEnabled(false);
    }

    private void showMovieDetails(MovieDetails movieDetails) {
        getView(this, id.movieTitleTextView, TextView.class).setText(movieDetails.getTitle());
        getView(this, id.movieGenreTextView, TextView.class).setText(movieDetails.getGenresString());
        getView(this, id.movieDurationTextView, TextView.class).setText(RunTimeService.getRunTime(movieDetails.getRuntime()));
        getView(this, id.movieReleaseDateTextView, TextView.class).setText(ReleaseDateService.getFormattedDate(movieDetails.getReleaseDate()));
        getView(this, id.movieRatingTextView, TextView.class).setText(String.format(" %s/10", movieDetails.getVoteAverage()));
        getView(this, id.movieVoteCountTextView, TextView.class).setText(String.format("\t(%s votes)", movieDetails.getVoteCount()));
        getView(this, id.movieLanguageTextView, TextView.class).setText(LanguageService.getLanguages(movieDetails.getSpokenLanguages()));
        getView(this, id.movieDescriptionTextView, TextView.class).setText(movieDetails.getOverview());
        Picasso.with(this).load(ImageApiService.BASE_URL + movieDetails.getPosterPath())
                .placeholder(drawable.ic_poster_placeholder)
                .error(drawable.ic_poster_error)
                .into(getView(this, id.moviePosterImageView, ImageView.class));
    }

    private void initializeAddToWatchlistButton() {
        if (DatabaseService.checkIfAddedToWatchlist(movieDetails.getId())) {
            disableAddToWatchlistButton("Added to watchlist");
        }
        if (DatabaseService.checkIfAddedToWatchedList(movieDetails.getId())) {
            disableAddToWatchlistButton("Watched");
        }
        final Animation animAlpha = AnimationUtils.loadAnimation(this, anim.anim_alpha);

        getView(this, id.addToWatchListButton, Button.class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                WatchList movieToBeAdded = new WatchList();
                movieToBeAdded.setId(movieDetails.getId());
                movieToBeAdded.save();
                disableAddToWatchlistButton("Added to watchlist");
            }
        });
    }

    private void initializeYoutubePlayer() {
        YouTubePlayerSupportFragment youTubePlayerSupportFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(id.youtube_fragment);
        youTubePlayerSupportFragment.initialize(MovieApiService.YOUTUBE_API_KEY, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem item = menu.findItem(id.filterSpinnerItem);
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
            case 16908332:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onInitializationSuccess(final YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, final boolean wasRestored) {
        movieApiService.getMovieTrailerKeys(movieDetails.getId(), movieApiService.API_KEY).enqueue(new Callback<TrailersPage>() {
            @Override
            public void onResponse(Call<TrailersPage> call, Response<TrailersPage> response) {
                TrailersPage responseBody = response.body();
                if (!wasRestored && responseBody.getTrailers().size() > 0) {
                    try {
                        youTubePlayer.cueVideo(responseBody.getTrailers().get(0).getKey());
                    } catch (IllegalStateException e) {
                        onInitializationFailure(provider, null);
                    }
                }
            }

            @Override
            public void onFailure(Call<TrailersPage> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        Toast.makeText(getApplicationContext(), "Error loading trailer", Toast.LENGTH_LONG).show();
    }
}
