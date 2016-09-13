package nullworks.com.inkfolio.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LowResolution implements Parcelable {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("width")
    @Expose
    private int width;
    @SerializedName("height")
    @Expose
    private int height;

    /**
     * No args constructor for use in serialization
     *
     */
    public LowResolution() {
    }

    /**
     *
     * @param height
     * @param width
     * @param url
     */
    public LowResolution(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    protected LowResolution(Parcel in) {
        url = in.readString();
    }

    public static final Creator<LowResolution> CREATOR = new Creator<LowResolution>() {
        @Override
        public LowResolution createFromParcel(Parcel in) {
            return new LowResolution(in);
        }

        @Override
        public LowResolution[] newArray(int size) {
            return new LowResolution[size];
        }
    };

    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     * The width
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @param width
     * The width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     *
     * @return
     * The height
     */
    public int getHeight() {
        return height;
    }

    /**
     *
     * @param height
     * The height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
    }
}