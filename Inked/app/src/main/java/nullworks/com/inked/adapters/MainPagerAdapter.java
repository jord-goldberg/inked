package nullworks.com.inked.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.Menu;

import java.util.ArrayList;

import nullworks.com.inked.fragments.RecyclerFragment;
import nullworks.com.inked.models.Datum;

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
        return RecyclerFragment.newInstance(new ArrayList<Datum>());
    }

    @Override
    public int getCount() {
        return mCategoryMenu.size();
    }


}
