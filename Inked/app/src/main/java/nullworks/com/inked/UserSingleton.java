package nullworks.com.inked;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import nullworks.com.inked.models.custom.InkedDatum;
import nullworks.com.inked.models.custom.InkedUser;

/**
 * Created by joshuagoldberg on 9/9/16.
 */
public class UserSingleton {

    private static UserSingleton sInstance = null;

    private InkedUser mUser;

    private ArrayList<InkedDatum> mDataToShare;

    private UserSingleton() {
        mUser = new InkedUser();
        mDataToShare = new ArrayList<>();
    }

    public static UserSingleton getInstance() {
        if (sInstance == null) {
            sInstance = new UserSingleton();
        }
        return sInstance;
    }

    public InkedUser getUser() {
        return mUser;
    }

    public void setUser(InkedUser user) {
        this.mUser = user;
    }

    public ArrayList<InkedDatum> getDataToShare() {
        return mDataToShare;
    }
}
