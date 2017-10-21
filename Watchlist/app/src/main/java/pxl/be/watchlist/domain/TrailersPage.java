
package pxl.be.watchlist.domain;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class TrailersPage {

    @SerializedName("id")
    private Long mId;
    @SerializedName("results")
    private List<Trailer> mTrailers;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public List<Trailer> getTrailers() {
        return mTrailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        mTrailers = trailers;
    }

}
