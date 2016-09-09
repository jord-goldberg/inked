package nullworks.com.inked.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import nullworks.com.inked.fragments.FbRecyclerFragment;
import nullworks.com.inked.fragments.ProfileFragment;
import nullworks.com.inked.fragments.SuggestionFragment;
import nullworks.com.inked.fragments.UnsharedFragment;

/**
 * Created by joshuagoldberg on 9/4/16.
 */
public class PortfolioPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "PortfolioPagerAdapter";
    public static final String FRAGMENT_TITLE = "fragmentTitle";

    ArrayList<Fragment> mFragments;

    int mUserFlag;

    public PortfolioPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = mFragments.get(position).getArguments().getString(FRAGMENT_TITLE);
        switch (title) {
            case SuggestionFragment.FRAGMENT_TITLE:
                return title;
            case ProfileFragment.FRAGMENT_TITLE:
                return title;
            case UnsharedFragment.FRAGMENT_TITLE:
                return title;
            case FbRecyclerFragment.FRAGMENT_TITLE:
                return title;
            default:
                return super.getPageTitle(position);
        }
    }
}