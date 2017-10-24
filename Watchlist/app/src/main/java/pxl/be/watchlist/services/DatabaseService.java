package pxl.be.watchlist.services;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import pxl.be.watchlist.databaaaz.WatchList;

/**
 * Created by 11501537 on 24/10/2017.
 */

public class DatabaseService {
    public static boolean checkIfAddedToWatchlist(long id){
        List<WatchList> movieIds = SQLite.select().
                from(WatchList.class).queryList();
        for (WatchList movie: movieIds) {
            if (id == movie.getId())
                return true;
        }
        return false;
    }
}
