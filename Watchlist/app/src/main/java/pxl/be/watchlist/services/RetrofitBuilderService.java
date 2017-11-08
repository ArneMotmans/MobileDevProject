package pxl.be.watchlist.services;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;

public class RetrofitBuilderService {
    public static Retrofit build(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(GuavaCallAdapterFactory.create())
                .build();
    }
}
