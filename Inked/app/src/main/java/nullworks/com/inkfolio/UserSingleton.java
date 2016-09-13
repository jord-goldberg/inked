package nullworks.com.inkfolio;

import java.util.ArrayList;

import nullworks.com.inkfolio.models.custom.InkDatum;
import nullworks.com.inkfolio.models.custom.InkUser;

/**
 * Created by joshuagoldberg on 9/9/16.
 */
public class UserSingleton {

    private static UserSingleton sInstance = null;

    private InkUser mUser;

    private ArrayList<InkDatum> mDataToShare;
    private ArrayList<InkDatum> mMainQueryResult;

    private UserSingleton() {
        mUser = new InkUser();
        mDataToShare = new ArrayList<>();
        mMainQueryResult = new ArrayList<>();
    }

    public static UserSingleton getInstance() {
        if (sInstance == null) {
            sInstance = new UserSingleton();
        }
        return sInstance;
    }

    public InkUser getUser() {
        return mUser;
    }

    public void setUser(InkUser user) {
        this.mUser = user;
    }

    public ArrayList<InkDatum> getDataToShare() {
        return mDataToShare;
    }

    public ArrayList<InkDatum> getMainQueryResult() {
        return mMainQueryResult;
    }
}
