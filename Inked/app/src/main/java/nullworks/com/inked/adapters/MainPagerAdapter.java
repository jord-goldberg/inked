package nullworks.com.inked.adapters;


import android.app.Fragment;

import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.Menu;

import com.google.firebase.database.Query;

import nullworks.com.inked.fragments.QueryRecyclerFragment;

/**
 * Created by joshuagoldberg on 9/2/16.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private Menu mCategoryMenu;
    private Query mQuery;

    public MainPagerAdapter(FragmentManager fm, Menu menu, Query query) {
        super(fm);
        mCategoryMenu = menu;
        mQuery = query;
    }

    @Override
    public Fragment getItem(int position) {
        return QueryRecyclerFragment.newInstance(mQuery);
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
