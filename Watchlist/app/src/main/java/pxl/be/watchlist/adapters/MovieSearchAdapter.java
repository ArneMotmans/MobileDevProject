package pxl.be.watchlist.adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pxl.be.watchlist.R;
import pxl.be.watchlist.activities.MovieDetailsActivity;
import pxl.be.watchlist.activities.WatchListActivity;
import pxl.be.watchlist.databaaaz.WatchList;
import pxl.be.watchlist.domain.Movie;
import pxl.be.watchlist.domain.MovieDetails;
import pxl.be.watchlist.services.ImageApiService;
import pxl.be.watchlist.services.MovieApiService;
import pxl.be.watchlist.services.ReleaseDateService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieSearchAdapter extends BaseAdapter {
    private Context context;
    public List<Movie> movies;
    public List<WatchList> watchListMovies;

    public MovieSearchAdapter(@NonNull Context context, @NonNull List<Movie> movies, MovieApiService movieApiService) {
        this.movies = movies;
        this.context = context;
    }

    public MovieSearchAdapter(WatchListActivity context, List<WatchList> watchListMovies, Object movieApiService) {
        this.watchListMovies = watchListMovies;
        this.context = context;
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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.search_items_layout,null);

        if (watchListMovies != null){
            ((TextView) convertView.findViewById(R.id.searchMovieTitleTextView)).setText(watchListMovies.get(position).getmTitle());
            ((TextView) convertView.findViewById(R.id.searchMovieReleaseDateTextView)).setText(ReleaseDateService.getFormattedDate(watchListMovies.get(position).getmReleaseDate()));
            ((TextView) convertView.findViewById(R.id.searchMovieDescriptionTextView)).setText(watchListMovies.get(position).getmOverview());

            Picasso.with(context)
                    .load(ImageApiService.BASE_URL+watchListMovies.get(position).getmPosterPath())
                    .placeholder(R.drawable.ic_poster_placeholder)
                    .error(R.drawable.ic_poster_error)
                    .into((ImageView)convertView.findViewById(R.id.searchMoviePosterImageView));
        } else {
            ((TextView) convertView.findViewById(R.id.searchMovieTitleTextView)).setText(movies.get(position).getTitle());
            ((TextView) convertView.findViewById(R.id.searchMovieReleaseDateTextView)).setText(ReleaseDateService.getFormattedDate(movies.get(position).getReleaseDate()));
            ((TextView) convertView.findViewById(R.id.searchMovieDescriptionTextView)).setText(movies.get(position).getOverview());

            Picasso.with(context)
                    .load(ImageApiService.BASE_URL+movies.get(position).getPosterPath())
                    .placeholder(R.drawable.ic_poster_placeholder)
                    .error(R.drawable.ic_poster_error)
                    .into((ImageView)convertView.findViewById(R.id.searchMoviePosterImageView));
        }

        return convertView;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
