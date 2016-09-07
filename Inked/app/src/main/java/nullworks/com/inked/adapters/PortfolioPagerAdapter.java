package nullworks.com.inked.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nullworks.com.inked.fragments.SuggestionFragment;

/**
 * Created by joshuagoldberg on 9/4/16.
 */
public class PortfolioPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "PortfolioPagerAdapter";

    int mUserFlag;

    public PortfolioPagerAdapter(FragmentManager fm, int userFlag) {
        super(fm);
        mUserFlag = userFlag;
    }

    @Override
    public Fragment getItem(int position) {
        switch (mUserFlag) {
            case 3:
                // user has no set location, no custom profile, but IS connected to Instagram
                break;
            case 5:
                // user is not connected to Instagram, has no custom profile, but DOES have a set location
                break;
            case 7:
                // user is not connected to instagram, has no set location, but DOES have a custom profile
                break;
            case 8:
                // user has no custom profile, but DOES have a set location and is connected to Instagram
                break;
            case 10:
                // user has no set location, but DOES have a custom profile and is connected to Instagram
                break;
            case 12:
                // user is not connected to Instagram, but DOES have a set location and custom profile
                break;
            case 15:
                // user IS connected to Instagram and DOES have a set location and custom profile
                break;
            default:
                // user is not connected to Instagram, has no set location, and no custom profile
                return SuggestionFragment.newInstance(mUserFlag);
        }
        return SuggestionFragment.newInstance(mUserFlag);
    }

    @Override
    public int getCount() {
        if (mUserFlag == 5 || mUserFlag == 7 || mUserFlag == 12) {
            return 2;
        } else if (mUserFlag == 3 || mUserFlag == 15) {
            return 3;
        } else if (mUserFlag == 8 || mUserFlag == 10) {
            return 4;
        } else {
            return 1;
        }
    }
}
