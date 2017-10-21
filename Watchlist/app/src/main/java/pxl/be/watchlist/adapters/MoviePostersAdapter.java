package pxl.be.watchlist.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pxl.be.watchlist.R;
import pxl.be.watchlist.activities.MainActivity;
import pxl.be.watchlist.activities.MovieDetailsActivity;
import pxl.be.watchlist.domain.Movie;
import pxl.be.watchlist.domain.MovieDetails;
import pxl.be.watchlist.services.ImageApiService;
import pxl.be.watchlist.services.MovieApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoviePostersAdapter extends ArrayAdapter<Movie> {
    private Context context;
    public List<Movie> movies;
    private MovieApiService movieApiService;

    public MoviePostersAdapter(@NonNull Context context, @NonNull List<Movie> movies, MovieApiService movieApiService) {
        super(context, -1, movies);
        this.context = context;
        this.movies = movies;
        this.movieApiService = movieApiService;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        ImageView imageView;
        if (convertView == null){
            imageView = new ImageView(context);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setAdjustViewBounds(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieApiService.getMovieDetails(movies.get(position).getId(), MovieApiService.API_KEY).enqueue(new Callback<MovieDetails>() {
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

        Picasso.with(context)
                .load(ImageApiService.BASE_URL+movies.get(position).getPosterPath())
                .placeholder(R.drawable.ic_poster_placeholder)
                .error(R.drawable.ic_poster_error)
                .into(imageView);
        return imageView;
    }

    public void addMovies(List<Movie> moviesToAdd){
        movies.addAll(moviesToAdd);
    }
}
