package nullworks.com.inkfolio.adapters;


import android.app.Fragment;

import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.Menu;

import nullworks.com.inkfolio.fragments.QueryFragment;
import nullworks.com.inkfolio.fragments.TagQueryFragment;

/**
 * Created by joshuagoldberg on 9/2/16.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private Menu mCategoryMenu;

    public MainPagerAdapter(FragmentManager fm, Menu menu) {
        super(fm);
        mCategoryMenu = menu;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return QueryFragment.newInstance();
            case 1: return TagQueryFragment.newInstance("nofilter");
            case 2: return TagQueryFragment.newInstance("antigua");
            case 3: return TagQueryFragment.newInstance("generalassembly");
            case 4: return TagQueryFragment.newInstance("manhattan");
            default:
                return TagQueryFragment.newInstance(mCategoryMenu.getItem(position).getTitle().toString());
        }
    }

    @Override
    public int getCount() {
        return mCategoryMenu.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCategoryMenu.getItem(position).getTitle();
    }
}
