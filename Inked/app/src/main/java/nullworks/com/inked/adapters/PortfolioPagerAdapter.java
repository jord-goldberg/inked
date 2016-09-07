package nullworks.com.inked.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import nullworks.com.inked.fragments.InstaRecyclerFragment;
import nullworks.com.inked.models.Datum;

/**
 * Created by joshuagoldberg on 9/4/16.
 */
public class PortfolioPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "PortfolioPagerAdapter";

    private ArrayList<Datum> mData;

    public PortfolioPagerAdapter(FragmentManager fm, ArrayList<Datum> data) {
        super(fm);
        mData = data;
    }

    @Override
    public Fragment getItem(int position) {
        return InstaRecyclerFragment.newInstance(mData);
    }

    @Override
    public int getCount() {
        return 4;
    }
}
