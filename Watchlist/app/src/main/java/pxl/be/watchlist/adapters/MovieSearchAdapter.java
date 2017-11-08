package pxl.be.watchlist.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pxl.be.watchlist.domain.Movie;
import pxl.be.watchlist.services.ImageApiService;
import pxl.be.watchlist.services.MovieApiService;
import pxl.be.watchlist.services.ReleaseDateService;
import pxl.be.watchlist.utilities.ViewRetrieverUtility;

import static pxl.be.watchlist.R.drawable;
import static pxl.be.watchlist.R.id;
import static pxl.be.watchlist.R.layout;

public class MovieSearchAdapter extends BaseAdapter {
    private Context context;
    private List<Movie> movies;

    public MovieSearchAdapter(@NonNull Context context, @NonNull List<Movie> movies, MovieApiService movieApiService) {
        this.movies = movies;
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
            convertView = inflater.inflate(layout.search_items_layout, null);

        ViewRetrieverUtility.getView(convertView, id.searchMovieTitleTextView, TextView.class)
                .setText(movies.get(position).getTitle());
        ViewRetrieverUtility.getView(convertView, id.searchMovieReleaseDateTextView, TextView.class)
                .setText(ReleaseDateService.getFormattedDate(movies.get(position).getReleaseDate()));
        ViewRetrieverUtility.getView(convertView, id.searchMovieDescriptionTextView, TextView.class)
                .setText(movies.get(position).getOverview());

        Picasso.with(context)
                .load(ImageApiService.BASE_URL + movies.get(position).getPosterPath())
                .placeholder(drawable.ic_poster_placeholder)
                .error(drawable.ic_poster_error)
                .into((ImageView) convertView.findViewById(id.searchMoviePosterImageView));
        return convertView;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
