
package pxl.be.watchlist.domain;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
public class Genre implements Serializable{

    @SerializedName("id")
    private Long mId;
    @SerializedName("name")
    private String mName;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
