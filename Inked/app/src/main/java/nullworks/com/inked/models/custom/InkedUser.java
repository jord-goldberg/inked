package nullworks.com.inked.models.custom;

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
    @SerializedName("shared")
    @Expose
    private ArrayList<Datum> shared;
    @SerializedName("unshared")
    @Expose
    private ArrayList<Datum> unshared;

    public InkedUser() {
    }

    public InkedUser(User user, Location location, String profile, ArrayList<Datum> shared, ArrayList<Datum> unshared) {
        this.location = location;
        this.profile = profile;
        this.shared = shared;
        this.unshared = unshared;
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

    public ArrayList<Datum> getShared() {
        return shared;
    }

    public void setShared(ArrayList<Datum> shared) {
        this.shared = shared;
    }

    public ArrayList<Datum> getUnshared() {
        return unshared;
    }

    public void setUnshared(ArrayList<Datum> unshared) {
        this.unshared = unshared;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
