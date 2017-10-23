package pxl.be.watchlist.databaaaz;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by 11502064 on 23/10/2017.
 */

@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase  {
    public static final String NAME = "WatchListDB";
    public static final int VERSION = 1;

}
