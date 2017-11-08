package pxl.be.watchlist.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.squareup.picasso.Picasso;

import java.util.List;

import pxl.be.watchlist.R;
import pxl.be.watchlist.activities.MovieDetailsActivity;
import pxl.be.watchlist.database.WatchList;
import pxl.be.watchlist.database.Watched;
import pxl.be.watchlist.domain.MovieDetails;
import pxl.be.watchlist.services.ImageApiService;
import pxl.be.watchlist.services.MovieApiService;
import pxl.be.watchlist.services.ReleaseDateService;
import pxl.be.watchlist.utilities.ViewRetrieverUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pxl.be.watchlist.services.MovieApiService.API_KEY;

public class WatchListAdapter extends BaseAdapter {
    
    private MovieApiService movieApiService;
    private Context context;
    public List<MovieDetails> movies;
    private boolean watchlist;

    public WatchListAdapter(@NonNull Context context, @NonNull List<MovieDetails> movies, MovieApiService movieApiService, boolean watchlist) {
        this.context = context;
        this.movies = movies;
        this.movieApiService = movieApiService;
        this.watchlist = watchlist;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return movies.get(i).getId();
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.watchlist_items_layout, null);

        ViewRetrieverUtility.getView(convertView, R.id.watchlistMovieTitleTextView, TextView.class)
                .setText(movies.get(position).getTitle());
        ViewRetrieverUtility.getView(convertView, R.id.watchlistMovieReleaseDateTextView, TextView.class)
                .setText(ReleaseDateService.getFormattedDate(movies.get(position).getReleaseDate()));

        if (watchlist) {
            getWatchlistView(convertView, position);
        } else {
            convertView.findViewById(R.id.removeFromWatchlistButton).setVisibility(View.GONE);
            convertView.findViewById(R.id.watchedButton).setVisibility(View.GONE);
        }

        Picasso.with(context)
                .load(ImageApiService.BASE_URL + movies.get(position).getPosterPath())
                .placeholder(R.drawable.ic_poster_placeholder)
                .error(R.drawable.ic_poster_error)
                .into((ImageView) convertView.findViewById(R.id.watchlistMoviePosterImageView));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieApiService.getMovieDetails(movies.get(position).getId(), API_KEY).enqueue(new Callback<MovieDetails>() {
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
        });

        return convertView;
    }

    private void getWatchlistView(View convertView, final int position) {
        convertView.findViewById(R.id.removeFromWatchlistButton)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<WatchList> watchListMovies = SQLite.select().from(WatchList.class).queryList();
                        for (WatchList movie : watchListMovies) {
                            if (movie.getId() == movies.get(position).getId()) {
                                movie.delete();
                                movies.remove(position);
                                notifyDataSetChanged();
                                break;
                            }
                        }

                    }
                });
        convertView.findViewById(R.id.watchedButton)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Watched movieToBeAdded = new Watched();
                        movieToBeAdded.setId(movies.get(position).getId());
                        movieToBeAdded.save();

                        List<WatchList> watchListMovies = SQLite.select().from(WatchList.class).queryList();
                        for (WatchList movie : watchListMovies) {
                            if (movie.getId() == movieToBeAdded.getId()) {
                                movie.delete();
                                movies.remove(position);
                                notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                });
    }

    public void addMovieDetails(MovieDetails movie) {
        movies.add(movie);
    }
}
