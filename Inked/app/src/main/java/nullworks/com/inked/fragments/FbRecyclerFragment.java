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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import nullworks.com.inked.R;
import nullworks.com.inked.adapters.FbRecyclerAdapter;
import nullworks.com.inked.adapters.MediaViewHolder;
import nullworks.com.inked.adapters.PortfolioPagerAdapter;
import nullworks.com.inked.models.Datum;

/**
 * Created by joshuagoldberg on 9/2/16.
 */
public class FbRecyclerFragment extends Fragment {

    private static final String TAG = "FbRecyclerFragment";

    public static final String FRAGMENT_TITLE = "shared";

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private FbRecyclerAdapter mAdapter;

    private DatabaseReference mRef;

    public static FbRecyclerFragment newInstance() {
        FbRecyclerFragment fragment = new FbRecyclerFragment();
        Bundle args = new Bundle();
        args.putString(PortfolioPagerAdapter.FRAGMENT_TITLE, FRAGMENT_TITLE);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRef = FirebaseDatabase.getInstance().getReference().child("media");
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
        mAdapter = new FbRecyclerAdapter(Datum.class, R.layout.card_grid, MediaViewHolder.class, mRef);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
