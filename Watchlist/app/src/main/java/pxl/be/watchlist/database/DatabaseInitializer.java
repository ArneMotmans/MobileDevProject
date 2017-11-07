package pxl.be.watchlist.database;

import android.app.Application;
import com.raizlabs.android.dbflow.config.FlowManager;

public class DatabaseInitializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
