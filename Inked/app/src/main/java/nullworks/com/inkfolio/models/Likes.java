package nullworks.com.inkfolio.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Likes {

    @SerializedName("count")
    @Expose
    private Integer count;

    /**
     * No args constructor for use in serialization
     *
     */
    public Likes() {
    }

    /**
     *
     * @param count
     */
    public Likes(Integer count) {
        this.count = count;
    }

    /**
     *
     * @return
     * The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     *
     * @param count
     * The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

}