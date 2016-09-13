package nullworks.com.inkfolio.models.custom;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import nullworks.com.inkfolio.models.Images;

public class InkDatum implements Parcelable {

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
    @SerializedName("created_time")
    @Expose
    private Long createdTime;
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
    public InkDatum() {
    }

    /**
     *
     * @param tags
     * @param link
     * @param caption
     * @param id
     * @param images
     * @param createdTime
     * @param latitude
     * @param longitude
     */
    public InkDatum(String caption, String link, String userId, Images images, List<String> tags, String id, Long createdTime, Double latitude, Double longitude) {
        this.caption = caption;

        this.link = link;
        this.userId = userId;
        this.images = images;
        this.tags = tags;
        this.id = id;
        this.createdTime = createdTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected InkDatum(Parcel in) {
        caption = in.readString();
        link = in.readString();
        userId = in.readString();
        images = in.readParcelable(Images.class.getClassLoader());
        tags = in.createStringArrayList();
        id = in.readString();
    }

    public static final Creator<InkDatum> CREATOR = new Creator<InkDatum>() {
        @Override
        public InkDatum createFromParcel(Parcel in) {
            return new InkDatum(in);
        }

        @Override
        public InkDatum[] newArray(int size) {
            return new InkDatum[size];
        }
    };

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
     * The createdTime
     */
    public Long getCreatedTime() {
        return createdTime;
    }

    /**
     *
     * @param createdTime
     * The created_time
     */
    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(caption);
        parcel.writeString(link);
        parcel.writeString(userId);
        parcel.writeParcelable(images, i);
        parcel.writeStringList(tags);
        parcel.writeString(id);
    }
}
