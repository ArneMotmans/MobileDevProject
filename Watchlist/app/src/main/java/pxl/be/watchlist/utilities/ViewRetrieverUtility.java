package pxl.be.watchlist.utilities;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ViewRetrieverUtility {
    public static <T extends View> T getView(AppCompatActivity activity, int id, Class<T> type) {
        return type.cast(activity.findViewById(id));
    }

    public static <T extends View> T getView(View view, int id, Class<T> type) {
        return type.cast(view.findViewById(id));
    }
}
