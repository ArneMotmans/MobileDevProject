package pxl.be.watchlist.database;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = DatabaseConfiguration.NAME, version = DatabaseConfiguration.VERSION)
public class DatabaseConfiguration {
    public static final String NAME = "WatchListDB";
    public static final int VERSION = 1;

}
