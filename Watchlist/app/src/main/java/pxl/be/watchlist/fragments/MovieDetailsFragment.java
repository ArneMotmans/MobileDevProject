package pxl.be.watchlist.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import pxl.be.watchlist.R;
import pxl.be.watchlist.activities.MovieDetailsActivity;
import pxl.be.watchlist.databaaaz.WatchList;
import pxl.be.watchlist.domain.MovieDetails;
import pxl.be.watchlist.domain.TrailersPage;
import pxl.be.watchlist.services.DatabaseService;
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

public class MovieDetailsFragment extends Fragment implements YouTubePlayer.OnInitializedListener {

    private MovieDetails movieDetails;
    private YouTubePlayerFragment youtubePlayerFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        movieDetails = (MovieDetails) bundle.get("movieDetails");

        View view = inflater.inflate(R.layout.movie_details_fragment_layout, container, false);

        if (DatabaseService.checkIfAddedToWatchlist(movieDetails.getId())) {
            DisableAddToWatchlistButton(view, "Added to watchlist");
        }
        if (DatabaseService.checkIfAddedToWatchedList(movieDetails.getId())) {
            DisableAddToWatchlistButton(view, "Watched");
        }
        showMovieDetails(view, movieDetails);

        getView(view, R.id.addToWatchListButton, Button.class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Save 'movieDetails" to firebase database
                WatchList movieToBeAdded = new WatchList();
                movieToBeAdded.setId(movieDetails.getId());
                movieToBeAdded.save();
                DisableAddToWatchlistButton(view, "Added to watchlist");
            }
        });

        youtubePlayerFragment = new YouTubePlayerFragment();
        youtubePlayerFragment.initialize(MovieDetailsActivity.API_KEY, this);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.youtubeFragmentContainer, youtubePlayerFragment);
        fragmentTransaction.commit();

        return view;
    }

    private void DisableAddToWatchlistButton(View view, String text) {
        Button button = getView(view, R.id.addToWatchListButton, Button.class);
        button.setText(text);
        button.setEnabled(false);
    }

    private void showMovieDetails(View view, MovieDetails movieDetails) {
        getView(view, R.id.moviePosterImageView, ImageView.class).setVisibility(View.GONE);
        getView(view, R.id.movieTitleTextView, TextView.class).setText(movieDetails.getTitle());
        getView(view, R.id.movieGenreTextView, TextView.class).setText(movieDetails.getGenresString());
        getView(view, R.id.movieDurationTextView, TextView.class).setText(RunTimeService.getRunTime(movieDetails.getRuntime()));
        getView(view, R.id.movieReleaseDateTextView, TextView.class).setText(ReleaseDateService.getFormattedDate(movieDetails.getReleaseDate()));
        getView(view, R.id.movieRatingTextView, TextView.class).setText(String.format(" %s/10", movieDetails.getVoteAverage()));
        getView(view, R.id.movieVoteCountTextView, TextView.class).setText(String.format("\t(%s votes)", movieDetails.getVoteCount()));
        getView(view, R.id.movieLanguageTextView, TextView.class).setText(LanguageService.getLanguages(movieDetails.getSpokenLanguages()));
        getView(view, R.id.movieDescriptionTextView, TextView.class).setText(movieDetails.getOverview());
    }

    private <T extends View> T getView(View view, int id, Class<T> type) {
        return type.cast(view.findViewById(id));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onInitializationSuccess(final YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, final boolean wasRestored) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(GuavaCallAdapterFactory.create())
                .build();

        MovieApiService movieApiService = retrofit.create(MovieApiService.class);

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
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(getActivity().getApplicationContext(), "No trailer available.", Toast.LENGTH_LONG).show();
    }
}
