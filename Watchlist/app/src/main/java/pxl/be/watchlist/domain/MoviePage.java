package pxl.be.watchlist.domain;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
public class MoviePage {

    @SerializedName("page")
    private Long mPage;
    @SerializedName("results")
    private List<Movie> mMovies;
    @SerializedName("total_pages")
    private Long mTotalPages;
    @SerializedName("total_results")
    private Long mTotalResults;

    public Long getPage() {
        return mPage;
    }

    public void setPage(Long page) {
        mPage = page;
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    public void setMovies(List<Movie> movies) {
        mMovies = movies;
    }

    public Long getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(Long totalPages) {
        mTotalPages = totalPages;
    }

    public Long getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(Long totalResults) {
        mTotalResults = totalResults;
    }

}
