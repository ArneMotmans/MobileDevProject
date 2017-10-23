package pxl.be.watchlist.databaaaz;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

import pxl.be.watchlist.domain.Genre;
import pxl.be.watchlist.domain.MovieDetails;
import pxl.be.watchlist.domain.ProductionCompany;
import pxl.be.watchlist.domain.ProductionCountry;
import pxl.be.watchlist.domain.SpokenLanguage;

/**
 * Created by 11502064 on 23/10/2017.
 */

@Table(database = MyDatabase.class)
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
