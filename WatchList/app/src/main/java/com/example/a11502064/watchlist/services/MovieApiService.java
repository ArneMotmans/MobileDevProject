package com.example.a11502064.watchlist.services;

import com.example.a11502064.watchlist.domain.MovieDetails;
import com.example.a11502064.watchlist.domain.MoviePage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by 11501537 on 14/10/2017.
 */
public interface MovieApiService {
    public final String BASE_URL = "https://api.themoviedb.org/";
    public final String API_KEY = "0dfa5086610d5e269cd38e12d8778589";

    @GET("/3/movie/popular")
    Call<MoviePage> getPopularMovies(@Query("api_key")String apiKey);

    @GET("/3/movie/now_playing")
    Call<MoviePage> getNowPlayingMovies(@Query("api_key")String apiKey);

    @GET("/3/movie/upcoming")
    Call<MoviePage> getUpcomingMovies(@Query("api_key")String apiKey);

    @GET("/3/movie/top_rated")
    Call<MoviePage> getTopRatedMovies(@Query("api_key")String apiKey);

    @GET("/3/search/movie")
    Call<MoviePage> getMovie(@Query("api_key")String apiKey,@Query("query") String title);

    @GET("/3/movie/{movie_id}")
    Call<MovieDetails> getMovieDetails(@Path("movie_id") long id, @Query("api_key") String apiKey);
}
