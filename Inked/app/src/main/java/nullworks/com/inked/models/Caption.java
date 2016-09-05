package nullworks.com.inked.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Caption {

    @SerializedName("created_time")
    @Expose
    private String createdTime;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("from")
    @Expose
    private From from;
    @SerializedName("id")
    @Expose
    private String id;

    /**
     * No args constructor for use in serialization
     *
     */
    public Caption() {
    }

    /**
     *
     * @param id
     * @param text
     * @param createdTime
     * @param from
     */
    public Caption(String createdTime, String text, From from, String id) {
        this.createdTime = createdTime;
        this.text = text;
        this.from = from;
        this.id = id;
    }

    /**
     *
     * @return
     * The createdTime
     */
    public String getCreatedTime() {
        return createdTime;
    }

    /**
     *
     * @param createdTime
     * The created_time
     */
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    /**
     *
     * @return
     * The text
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     * The text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     * The from
     */
    public From getFrom() {
        return from;
    }

    /**
     *
     * @param from
     * The from
     */
    public void setFrom(From from) {
        this.from = from;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

}