package nullworks.com.inkfolio.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("code")
    @Expose
    private Integer code;

    /**
     * No args constructor for use in serialization
     *
     */
    public Meta() {
    }

    /**
     *
     * @param code
     */
    public Meta(Integer code) {
        this.code = code;
    }

    /**
     *
     * @return
     * The code
     */
    public Integer getCode() {
        return code;
    }

    /**
     *
     * @param code
     * The code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

}