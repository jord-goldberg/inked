package nullworks.com.inked.adapters;


import android.app.Fragment;

import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.Menu;

import nullworks.com.inked.fragments.FbRecyclerFragment;

/**
 * Created by joshuagoldberg on 9/2/16.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private Menu mCategoryMenu;

    public MainPagerAdapter(FragmentManager fm, Menu menu) {
        super(fm);
        mCategoryMenu = menu;
    }

    @Override
    public Fragment getItem(int position) {
        return FbRecyclerFragment.newInstance();
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
