package nullworks.com.inked;

import com.google.firebase.database.FirebaseDatabase;

import nullworks.com.inked.models.custom.InkedUser;

/**
 * Created by joshuagoldberg on 9/9/16.
 */
public class UserSingleton {

    private static UserSingleton sInstance = null;

    private InkedUser mUser;

    private UserSingleton() {}

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
}
