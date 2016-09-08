package nullworks.com.inked.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nullworks.com.inked.R;
import nullworks.com.inked.fragments.ProfileFragment;
import nullworks.com.inked.fragments.SuggestionFragment;
import nullworks.com.inked.models.custom.InkedUser;

/**
 * Created by joshuagoldberg on 9/4/16.
 */
public class PortfolioPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "PortfolioPagerAdapter";
    public static final String FRAGMENT_TITLE = "fragmentTitle";

    InkedUser mUser;
    int mUserFlag;

    public PortfolioPagerAdapter(FragmentManager fm, InkedUser user, int userFlag) {
        super(fm);
        mUser = user;
        mUserFlag = userFlag;
    }

    @Override
    public Fragment getItem(int position) {
        switch (mUserFlag) {
            case 3:
                // user has no set location, no custom profile, but IS connected to Instagram
                switch (position) {
                    case 0: break; // suggestion
                    case 1: break; // shared recycler
                    case 2: break; // unshared recycler
                }
                break;
            case 5:
                // user is not connected to Instagram, has no custom profile, but DOES have a set location
                switch (position) {
                    case 0: break; // suggestion
                    case 1: return ProfileFragment.newInstance(mUser, mUserFlag); // profile with map
                }
                break;
            case 7:
                // user is not connected to instagram, has no set location, but DOES have a custom profile
                switch (position) {
                    case 0: break; // suggestion
                    case 1: return ProfileFragment.newInstance(mUser, mUserFlag); // profile with text
                }
                break;
            case 8:
                // user has no custom profile, but DOES have a set location and is connected to Instagram
                switch (position) {
                    case 0: break; // suggestion
                    case 1: break; // shared recycler
                    case 2: break; // unshared recycler
                    case 3: return ProfileFragment.newInstance(mUser, mUserFlag); // profile with map
                }
                break;
            case 10:
                // user has no set location, but DOES have a custom profile and is connected to Instagram
                switch (position) {
                    case 0: break; // suggestion
                    case 1: break; // shared recycler
                    case 2: break; // unshared recycler
                    case 3: return ProfileFragment.newInstance(mUser, mUserFlag); // profile with text
                }
                break;
            case 12:
                // user is not connected to Instagram, but DOES have a set location and custom profile
                switch (position) {
                    case 0: break; // suggestion
                    case 1: return ProfileFragment.newInstance(mUser, mUserFlag); // profile with map & text
                }
                break;
            case 15:
                // user IS connected to Instagram and DOES have a set location and custom profile
                switch (position) {
                    case 0: break; // shared recycler
                    case 1: break; // unshared recycler
                    case 2: return ProfileFragment.newInstance(mUser, mUserFlag); // profile with map & text
                }
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

    @Override
    public CharSequence getPageTitle(int position) {
        String title = getItem(position).getArguments().getString(FRAGMENT_TITLE);
        switch (title) {
            case SuggestionFragment.FRAGMENT_TITLE:
                return title;
            case ProfileFragment.FRAGMENT_TITLE:
                return title;
            default:
                return super.getPageTitle(position);
        }
    }
}
