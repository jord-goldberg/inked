package nullworks.com.inked.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.Menu;

import nullworks.com.inked.fragments.RecyclerFragment;

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
//        mCategoryMenu.getItem(position).setChecked(true);
//        if (position-1 < 0) {
//            mCategoryMenu.getItem(position+1).setChecked(false);
//        } else if (position+1 == mCategoryMenu.size()) {
//            mCategoryMenu.getItem(position-1).setChecked(false);
//        } else {
//            mCategoryMenu.getItem(position+1).setChecked(false);
//            mCategoryMenu.getItem(position-1).setChecked(false);
//        }
        return new RecyclerFragment();
    }

    @Override
    public int getCount() {
        return mCategoryMenu.size();
    }


}
