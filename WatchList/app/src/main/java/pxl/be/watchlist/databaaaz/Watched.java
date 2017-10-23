package pxl.be.watchlist.databaaaz;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by 11502064 on 23/10/2017.
 */

@Table(database = MyDatabase.class)
public class Watched extends BaseModel {
    @PrimaryKey
    @Column
    long id;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
