package pxl.be.watchlist.services;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import java.util.List;
import pxl.be.watchlist.database.WatchList;
import pxl.be.watchlist.database.Watched;

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

    public static boolean checkIfAddedToWatchedList(long id){
        List<Watched> movieIds = SQLite.select().
                from(Watched.class).queryList();
        for (Watched movie: movieIds) {
            if (id == movie.getId())
                return true;
        }
        return false;
    }
}
