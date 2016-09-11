package nullworks.com.inked.models.custom;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import nullworks.com.inked.models.Images;

public class InkedDatum {

    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("images")
    @Expose
    private Images images;
    @SerializedName("tags")
    @Expose
    private List<String> tags = new ArrayList<String>();
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("id")
    @Expose
    private Double longitude;

    /**
     * No args constructor for use in serialization
     *
     */
    public InkedDatum() {
    }

    /**
     *
     * @param tags
     * @param link
     * @param caption
     * @param id
     * @param images
     * @param filter
     * @param latitude
     * @param longitude
     */
    public InkedDatum(String caption, String link, String userId, Images images, String filter, List<String> tags, String id, Double latitude, Double longitude) {
        this.caption = caption;

        this.link = link;
        this.userId = userId;
        this.images = images;
        this.tags = tags;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     *
     * @return
     * The caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     *
     * @param caption
     * The caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     *
     * @return
     * The link
     */
    public String getLink() {
        return link;
    }

    /**
     *
     * @param link
     * The link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     *
     * @return
     * The user
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     * The images
     */
    public Images getImages() {
        return images;
    }

    /**
     *
     * @param images
     * The images
     */
    public void setImages(Images images) {
        this.images = images;
    }

    /**
     *
     * @return
     * The tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     * The tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
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

    /**
     *
     * @return
     * The latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     * The longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
