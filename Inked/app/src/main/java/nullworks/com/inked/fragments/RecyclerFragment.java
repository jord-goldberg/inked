package nullworks.com.inked.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nullworks.com.inked.R;
import nullworks.com.inked.adapters.MainRecyclerAdaper;
import nullworks.com.inked.adapters.PortfolioRecyclerAdapter;
import nullworks.com.inked.models.Datum;

/**
 * Created by joshuagoldberg on 9/2/16.
 */
public class RecyclerFragment extends Fragment {

    private static final String TAG = "RecyclerFragment";

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private PortfolioRecyclerAdapter mAdapter;

    private ArrayList<String> strings;
    private static ArrayList<Datum> mData;

    public static RecyclerFragment newInstance(ArrayList<Datum> data) {
        mData = data;
        return new RecyclerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_recycler, container, false);
        mRecyclerView = (RecyclerView) viewRoot.findViewById(R.id.recycler_media);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        } else {
            mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        }
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mAdapter = new PortfolioRecyclerAdapter(mData);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
