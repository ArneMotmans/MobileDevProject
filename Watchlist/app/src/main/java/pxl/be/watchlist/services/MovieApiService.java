package pxl.be.watchlist.services;

import pxl.be.watchlist.domain.MovieDetails;
import pxl.be.watchlist.domain.MoviePage;
import pxl.be.watchlist.domain.TrailersPage;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {
    String BASE_URL = "https://api.themoviedb.org/";
    String API_KEY = "0dfa5086610d5e269cd38e12d8778589";
    String YOUTUBE_API_KEY = "AIzaSyCzyQUfjEdK1UtZ-RgfcgzmXp6GH584rY8";

    @GET("/3/movie/popular")
    Call<MoviePage> getPopularMovies(@Query("page") int page, @Query("api_key") String apiKey);

    @GET("/3/movie/now_playing")
    Call<MoviePage> getNowPlayingMovies(@Query("page") int page, @Query("api_key") String apiKey);

    @GET("/3/movie/upcoming")
    Call<MoviePage> getUpcomingMovies(@Query("page") int page, @Query("api_key") String apiKey);

    @GET("/3/movie/top_rated")
    Call<MoviePage> getTopRatedMovies(@Query("page") int page, @Query("api_key") String apiKey);

    @GET("/3/search/movie")
    Call<MoviePage> searchMovie(@Query("api_key") String apiKey, @Query("query") String title);

    @GET("/3/movie/{movie_id}")
    Call<MovieDetails> getMovieDetails(@Path("movie_id") long id, @Query("api_key") String apiKey);

    @GET("/3/movie/{movie_id}/videos")
    Call<TrailersPage> getMovieTrailerKeys(@Path("movie_id") long id, @Query("api_key") String apiKey);
}
