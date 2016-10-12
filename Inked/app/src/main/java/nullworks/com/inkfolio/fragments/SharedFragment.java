package nullworks.com.inkfolio.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nullworks.com.inkfolio.adapters.SharedRecyclerAdapter;
import nullworks.com.inkfolio.R;
import nullworks.com.inkfolio.UserSingleton;
import nullworks.com.inkfolio.adapters.PortfolioPagerAdapter;
import nullworks.com.inkfolio.interfaces.SharedClickListener;
import nullworks.com.inkfolio.models.custom.InkDatum;
import nullworks.com.inkfolio.models.custom.InkUser;

/**
 * Created by joshuagoldberg on 9/2/16.
 */
public class SharedFragment extends Fragment implements SharedClickListener {

    private static final String TAG = "UnsharedFragment";

    public static final String FRAGMENT_TITLE = "shared";

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private SharedRecyclerAdapter mAdapter;

    private InkUser mUser;

    private Parcelable mLayoutState;

    private SharedFragmentListener mFragmentListener;

    public static SharedFragment newInstance() {
        SharedFragment fragment = new SharedFragment();
        Bundle args = new Bundle();
        args.putString(PortfolioPagerAdapter.FRAGMENT_TITLE, FRAGMENT_TITLE);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SharedFragmentListener) {
            mFragmentListener = (SharedFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SharedFragmentListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = UserSingleton.getInstance().getUser();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_recycler, container, false);
        mRecyclerView = (RecyclerView) viewRoot.findViewById(R.id.recycler_media);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mAdapter = new SharedRecyclerAdapter(mUser.getShared(), this);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        if(mLayoutState != null) {
            Log.d(TAG, "onViewCreated: ");
            mLayoutManager.onRestoreInstanceState(mLayoutState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        mLayoutState = mLayoutManager.onSaveInstanceState();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }

    public void onButtonPressed(InkDatum inkDatum) {
        if (mFragmentListener != null) {
            mFragmentListener.onSharedFragmentInteraction(inkDatum);
        }
    }

    @Override
    public void onSharedClicked(InkDatum inkDatum) {
        onButtonPressed(inkDatum);
    }

    public void addDatum(InkDatum inkDatum) {
        for (int i = 0; i < mUser.getShared().size(); i++) {
            if (inkDatum.getCreatedTime() < mUser.getShared().get(i).getCreatedTime()) {
                mUser.getShared().add(i, inkDatum);
                mAdapter.notifyItemInserted(i);
                return;
            }
        }
        mUser.getShared().add(inkDatum);
        mAdapter.notifyItemInserted(mUser.getShared().size()-1);
    }

    public void removeDatum(InkDatum inkDatum) {
        int position = mUser.getShared().indexOf(inkDatum);
        mUser.getShared().remove(inkDatum);
        mAdapter.notifyItemRemoved(position);
    }

    public interface SharedFragmentListener {
        void onSharedFragmentInteraction(InkDatum inkDatum);
    }
}

