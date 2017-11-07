package pxl.be.watchlist.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = DatabaseConfiguration.class)
public class WatchList extends BaseModel {
    @Column
    @PrimaryKey
    long id;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
