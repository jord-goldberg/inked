package nullworks.com.inked.models.custom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import nullworks.com.inked.models.User;

/**
 * Created by joshuagoldberg on 9/7/16.
 */
public class Location implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;

    public Location() {
    }

    public Location(String address, String id, double latitude, double longitude, String name) {
        this.address = address;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
