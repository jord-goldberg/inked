package nullworks.com.inked.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pagination {

    @SerializedName("next_url")
    @Expose
    private String nextUrl;
    @SerializedName("next_max_id")
    @Expose
    private String nextMaxId;

    /**
     * No args constructor for use in serialization
     *
     */
    public Pagination() {
    }

    /**
     *
     * @param nextUrl
     * @param nextMaxId
     */
    public Pagination(String nextUrl, String nextMaxId) {
        this.nextUrl = nextUrl;
        this.nextMaxId = nextMaxId;
    }

    /**
     *
     * @return
     * The nextUrl
     */
    public String getNextUrl() {
        return nextUrl;
    }

    /**
     *
     * @param nextUrl
     * The next_url
     */
    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    /**
     *
     * @return
     * The nextMaxId
     */
    public String getNextMaxId() {
        return nextMaxId;
    }

    /**
     *
     * @param nextMaxId
     * The next_max_id
     */
    public void setNextMaxId(String nextMaxId) {
        this.nextMaxId = nextMaxId;
    }

}
