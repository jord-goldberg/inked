package nullworks.com.inkfolio.models.custom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import nullworks.com.inkfolio.models.User;

/**
 * Created by joshuagoldberg on 9/7/16.
 */
public class InkUser {

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
    private ArrayList<InkDatum> shared;
    @SerializedName("unshared")
    @Expose
    private ArrayList<InkDatum> unshared;

    public InkUser() {
    }

    public InkUser(User user, Location location, String profile, ArrayList<InkDatum> shared, ArrayList<InkDatum> unshared) {
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

    public ArrayList<InkDatum> getShared() {
        return shared;
    }

    public void setShared(ArrayList<InkDatum> shared) {
        this.shared = shared;
    }

    public ArrayList<InkDatum> getUnshared() {
        return unshared;
    }

    public void setUnshared(ArrayList<InkDatum> unshared) {
        this.unshared = unshared;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
