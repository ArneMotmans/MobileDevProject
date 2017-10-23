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

import pxl.be.watchlist.R;
import pxl.be.watchlist.domain.Movie;
import pxl.be.watchlist.domain.MovieDetails;
import pxl.be.watchlist.services.ImageApiService;
import pxl.be.watchlist.services.MovieApiService;
import pxl.be.watchlist.services.ReleaseDateService;


public class WatchListAdapter extends BaseAdapter {
    private Context context;
    public List<MovieDetails> movies;

    public WatchListAdapter(@NonNull Context context, @NonNull List<MovieDetails> movies) {
        this.context = context;
        this.movies = movies;
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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.watchlist_items_layout,null);

        ((TextView) convertView.findViewById(R.id.watchlistMovieTitleTextView)).setText(movies.get(position).getTitle());
        ((TextView) convertView.findViewById(R.id.watchlistMovieReleaseDateTextView)).setText(ReleaseDateService.getFormattedDate(movies.get(position).getReleaseDate()));

        Picasso.with(context)
                .load(ImageApiService.BASE_URL+movies.get(position).getPosterPath())
                .placeholder(R.drawable.ic_poster_placeholder)
                .error(R.drawable.ic_poster_error)
                .into((ImageView)convertView.findViewById(R.id.watchlistMoviePosterImageView));

        return convertView;
    }
}
