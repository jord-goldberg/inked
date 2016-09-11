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
    private ArrayList<InkedDatum> shared;
    @SerializedName("unshared")
    @Expose
    private ArrayList<InkedDatum> unshared;

    public InkedUser() {
    }

    public InkedUser(User user, Location location, String profile, ArrayList<InkedDatum> shared, ArrayList<InkedDatum> unshared) {
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

    public ArrayList<InkedDatum> getShared() {
        return shared;
    }

    public void setShared(ArrayList<InkedDatum> shared) {
        this.shared = shared;
    }

    public ArrayList<InkedDatum> getUnshared() {
        return unshared;
    }

    public void setUnshared(ArrayList<InkedDatum> unshared) {
        this.unshared = unshared;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
