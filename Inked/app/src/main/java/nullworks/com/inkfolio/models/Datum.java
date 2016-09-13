package nullworks.com.inkfolio.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("comments")
    @Expose
    private Comments comments;
    @SerializedName("caption")
    @Expose
    private Caption caption;
    @SerializedName("likes")
    @Expose
    private Likes likes;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("user")
    @Expose
    private User user;
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
    private List<Object> usersInPhoto = new ArrayList<Object>();
    @SerializedName("filter")
    @Expose
    private String filter;
    @SerializedName("tags")
    @Expose
    private List<String> tags = new ArrayList<String>();
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("videos")
    @Expose
    private Videos videos;

    /**
     * No args constructor for use in serialization
     *
     */
    public Datum() {
    }

    /**
     *
     * @param tags
     * @param location
     * @param createdTime
     * @param videos
     * @param link
     * @param usersInPhoto
     * @param caption
     * @param type
     * @param id
     * @param likes
     * @param images
     * @param user
     * @param comments
     * @param filter
     */
    public Datum(Comments comments, Caption caption, Likes likes, String link, User user, String createdTime, Images images, String type, List<Object> usersInPhoto, String filter, List<String> tags, String id, Location location, Videos videos) {
        this.comments = comments;
        this.caption = caption;
        this.likes = likes;
        this.link = link;
        this.user = user;
        this.createdTime = createdTime;
        this.images = images;
        this.type = type;
        this.usersInPhoto = usersInPhoto;
        this.filter = filter;
        this.tags = tags;
        this.id = id;
        this.location = location;
        this.videos = videos;
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
    public Caption getCaption() {
        return caption;
    }

    /**
     *
     * @param caption
     * The caption
     */
    public void setCaption(Caption caption) {
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
    public List<Object> getUsersInPhoto() {
        return usersInPhoto;
    }

    /**
     *
     * @param usersInPhoto
     * The users_in_photo
     */
    public void setUsersInPhoto(List<Object> usersInPhoto) {
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
     * The location
     */
    public Location getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     *
     * @return
     * The videos
     */
    public Videos getVideos() {
        return videos;
    }

    /**
     *
     * @param videos
     * The videos
     */
    public void setVideos(Videos videos) {
        this.videos = videos;
    }

}
