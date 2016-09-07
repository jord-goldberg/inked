package nullworks.com.inked.models.custom;

import com.google.android.gms.location.places.Place;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import nullworks.com.inked.models.Datum;
import nullworks.com.inked.models.User;

/**
 * Created by joshuagoldberg on 9/7/16.
 */
public class InkedUser {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("shared_media")
    @Expose
    private ArrayList<Datum> sharedMedia;
    @SerializedName("unshared_media")
    @Expose
    private ArrayList<Datum> unsharedMedia;

    public InkedUser() {
    }

    public InkedUser(Location location, String profile, ArrayList<Datum> sharedMedia, ArrayList<Datum> unsharedMedia, User user) {
        this.location = location;
        this.profile = profile;
        this.sharedMedia = sharedMedia;
        this.unsharedMedia = unsharedMedia;
        this.user = user;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public ArrayList<Datum> getSharedMedia() {
        return sharedMedia;
    }

    public void setSharedMedia(ArrayList<Datum> sharedMedia) {
        this.sharedMedia = sharedMedia;
    }

    public ArrayList<Datum> getUnsharedMedia() {
        return unsharedMedia;
    }

    public void setUnsharedMedia(ArrayList<Datum> unsharedMedia) {
        this.unsharedMedia = unsharedMedia;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
