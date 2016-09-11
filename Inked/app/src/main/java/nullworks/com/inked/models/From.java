package nullworks.com.inked.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class From implements Parcelable {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;

    /**
     * No args constructor for use in serialization
     *
     */
    public From() {
    }

    /**
     *
     * @param id
     * @param username
     * @param fullName
     * @param type
     */
    public From(String username, String fullName, String type, String id) {
        this.username = username;
        this.fullName = fullName;
        this.type = type;
        this.id = id;
    }

    protected From(Parcel in) {
        username = in.readString();
        fullName = in.readString();
        type = in.readString();
        id = in.readString();
    }

    public static final Creator<From> CREATOR = new Creator<From>() {
        @Override
        public From createFromParcel(Parcel in) {
            return new From(in);
        }

        @Override
        public From[] newArray(int size) {
            return new From[size];
        }
    };

    /**
     *
     * @return
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     *
     * @param fullName
     * The full_name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
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
        parcel.writeString(username);
        parcel.writeString(fullName);
        parcel.writeString(type);
        parcel.writeString(id);
    }
}