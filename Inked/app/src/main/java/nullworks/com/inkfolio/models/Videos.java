package nullworks.com.inkfolio.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Videos {

    @SerializedName("low_resolution")
    @Expose
    private LowResolution lowResolution;
    @SerializedName("standard_resolution")
    @Expose
    private StandardResolution standardResolution;
    @SerializedName("comments")
    @Expose
    private Comments comments;
    @SerializedName("caption")
    @Expose
    private Object caption;
    @SerializedName("likes")
    @Expose
    private Likes likes;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("created_time")
    @Expose
    private String createdTime;
    @SerializedName("images")
    @Expose
    private Images images;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("users_in_photo")
    @Expose
    private Object usersInPhoto;
    @SerializedName("filter")
    @Expose
    private String filter;
    @SerializedName("tags")
    @Expose
    private List<Object> tags = new ArrayList<Object>();
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("location")
    @Expose
    private Object location;

    /**
     * No args constructor for use in serialization
     *
     */
    public Videos() {
    }

    /**
     *
     * @param tags
     * @param lowResolution
     * @param location
     * @param createdTime
     * @param link
     * @param usersInPhoto
     * @param caption
     * @param type
     * @param id
     * @param likes
     * @param images
     * @param standardResolution
     * @param user
     * @param comments
     * @param filter
     */
    public Videos(LowResolution lowResolution, StandardResolution standardResolution, Comments comments, Object caption, Likes likes, String link, String createdTime, Images images, String type, Object usersInPhoto, String filter, List<Object> tags, String id, User user, Object location) {
        this.lowResolution = lowResolution;
        this.standardResolution = standardResolution;
        this.comments = comments;
        this.caption = caption;
        this.likes = likes;
        this.link = link;
        this.createdTime = createdTime;
        this.images = images;
        this.type = type;
        this.usersInPhoto = usersInPhoto;
        this.filter = filter;
        this.tags = tags;
        this.id = id;
        this.user = user;
        this.location = location;
    }

    /**
     *
     * @return
     * The lowResolution
     */
    public LowResolution getLowResolution() {
        return lowResolution;
    }

    /**
     *
     * @param lowResolution
     * The low_resolution
     */
    public void setLowResolution(LowResolution lowResolution) {
        this.lowResolution = lowResolution;
    }

    /**
     *
     * @return
     * The standardResolution
     */
    public StandardResolution getStandardResolution() {
        return standardResolution;
    }

    /**
     *
     * @param standardResolution
     * The standard_resolution
     */
    public void setStandardResolution(StandardResolution standardResolution) {
        this.standardResolution = standardResolution;
    }

    /**
     *
     * @return
     * The comments
     */
    public Comments getComments() {
        return comments;
    }

    /**
     *
     * @param comments
     * The comments
     */
    public void setComments(Comments comments) {
        this.comments = comments;
    }

    /**
     *
     * @return
     * The caption
     */
    public Object getCaption() {
        return caption;
    }

    /**
     *
     * @param caption
     * The caption
     */
    public void setCaption(Object caption) {
        this.caption = caption;
    }

    /**
     *
     * @return
     * The likes
     */
    public Likes getLikes() {
        return likes;
    }

    /**
     *
     * @param likes
     * The likes
     */
    public void setLikes(Likes likes) {
        this.likes = likes;
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
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The usersInPhoto
     */
    public Object getUsersInPhoto() {
        return usersInPhoto;
    }

    /**
     *
     * @param usersInPhoto
     * The users_in_photo
     */
    public void setUsersInPhoto(Object usersInPhoto) {
        this.usersInPhoto = usersInPhoto;
    }

    /**
     *
     * @return
     * The filter
     */
    public String getFilter() {
        return filter;
    }

    /**
     *
     * @param filter
     * The filter
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     *
     * @return
     * The tags
     */
    public List<Object> getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     * The tags
     */
    public void setTags(List<Object> tags) {
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
     * The user
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user
     * The user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @return
     * The location
     */
    public Object getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    public void setLocation(Object location) {
        this.location = location;
    }

}