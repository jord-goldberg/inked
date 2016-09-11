package nullworks.com.inked.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Caption implements Parcelable {

    @SerializedName("created_time")
    @Expose
    private String createdTime;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("from")
    @Expose
    private From from;
    @SerializedName("id")
    @Expose
    private String id;

    /**
     * No args constructor for use in serialization
     *
     */
    public Caption() {
    }

    /**
     *
     * @param id
     * @param text
     * @param createdTime
     * @param from
     */
    public Caption(String createdTime, String text, From from, String id) {
        this.createdTime = createdTime;
        this.text = text;
        this.from = from;
        this.id = id;
    }

    protected Caption(Parcel in) {
        createdTime = in.readString();
        text = in.readString();
        from = in.readParcelable(From.class.getClassLoader());
        id = in.readString();
    }

    public static final Creator<Caption> CREATOR = new Creator<Caption>() {
        @Override
        public Caption createFromParcel(Parcel in) {
            return new Caption(in);
        }

        @Override
        public Caption[] newArray(int size) {
            return new Caption[size];
        }
    };

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
     * The text
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     * The text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     * The from
     */
    public From getFrom() {
        return from;
    }

    /**
     *
     * @param from
     * The from
     */
    public void setFrom(From from) {
        this.from = from;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(createdTime);
        parcel.writeString(text);
        parcel.writeParcelable(from, i);
        parcel.writeString(id);
    }
}