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
import pxl.be.watchlist.databaaaz.WatchList;
import pxl.be.watchlist.domain.MovieDetails;
import pxl.be.watchlist.domain.TrailersPage;
import pxl.be.watchlist.services.DatabaseService;
import pxl.be.watchlist.services.ImageApiService;
import pxl.be.watchlist.services.LanguageService;
import pxl.be.watchlist.services.MovieApiService;
import pxl.be.watchlist.services.ReleaseDateService;
import pxl.be.watchlist.services.RunTimeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;

public class MovieDetailsActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {

    public static final String API_KEY = "AIzaSyCzyQUfjEdK1UtZ-RgfcgzmXp6GH584rY8";

    private Retrofit retrofit;
    private MovieApiService movieApiService;
    private MovieDetails movieDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        retrofit = new Retrofit.Builder()
                .baseUrl(MovieApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(GuavaCallAdapterFactory.create())
                .build();

        movieApiService = retrofit.create(MovieApiService.class);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        movieDetails = (MovieDetails) bundle.get("movieDetails");
        if (DatabaseService.checkIfAddedToWatchlist(movieDetails.getId())) {
            DisableAddToWatchlistButton("Added to watchlist");
        }
        if (DatabaseService.checkIfAddedToWatchedList(movieDetails.getId())) {
            DisableAddToWatchlistButton("Watched");
        }
        showMovieDetails(movieDetails);

//        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
//        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);

        getView(R.id.addToWatchListButton, Button.class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Save 'movieDetails" to firebase database
                view.startAnimation(animAlpha);
                WatchList movieToBeAdded = new WatchList();
                movieToBeAdded.setId(movieDetails.getId());
                movieToBeAdded.save();
                DisableAddToWatchlistButton("Added to watchlist");
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Details");

        YouTubePlayerSupportFragment frag =
                (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        frag.initialize(API_KEY, this);

    }

    private void DisableAddToWatchlistButton(String text){
        Button button = getView(R.id.addToWatchListButton, Button.class);
        button.setText(text);
        button.setEnabled(false);
    }

    private void showMovieDetails(MovieDetails movieDetails) {
        getView(R.id.movieTitleTextView, TextView.class).setText(movieDetails.getTitle());
        getView(R.id.movieGenreTextView, TextView.class).setText(movieDetails.getGenresString());
        getView(R.id.movieDurationTextView, TextView.class).setText(RunTimeService.getRunTime(movieDetails.getRuntime()));
        getView(R.id.movieReleaseDateTextView, TextView.class).setText(ReleaseDateService.getFormattedDate(movieDetails.getReleaseDate()));
        getView(R.id.movieRatingTextView, TextView.class).setText(String.format(" %s/10", movieDetails.getVoteAverage()));
        getView(R.id.movieVoteCountTextView, TextView.class).setText(String.format("\t(%s votes)", movieDetails.getVoteCount()));
        getView(R.id.movieLanguageTextView, TextView.class).setText(LanguageService.getLanguages(movieDetails.getSpokenLanguages()));
        getView(R.id.movieDescriptionTextView, TextView.class).setText(movieDetails.getOverview());
        Picasso.with(this).load(ImageApiService.BASE_URL + movieDetails.getPosterPath())
                .placeholder(R.drawable.ic_poster_placeholder)
                .error(R.drawable.ic_poster_error)
                .into(getView(R.id.moviePosterImageView, ImageView.class));
    }

    private <T extends View> T getView(int id, Class<T> type) {
        return type.cast(findViewById(id));
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
                    youTubePlayer.cueVideo(responseBody.getTrailers().get(0).getKey());
                } else {
                    onInitializationFailure(provider, null);
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
        Toast.makeText(getApplicationContext(), "No trailer available.", Toast.LENGTH_LONG).show();
    }
}
