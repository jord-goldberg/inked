package nullworks.com.inked.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nullworks.com.inked.adapters.UnsharedRecyclerAdapter;
import nullworks.com.inked.R;
import nullworks.com.inked.UserSingleton;
import nullworks.com.inked.adapters.PortfolioPagerAdapter;
import nullworks.com.inked.interfaces.UnsharedClickListener;
import nullworks.com.inked.models.Datum;
import nullworks.com.inked.models.custom.InkedDatum;
import nullworks.com.inked.models.custom.InkedUser;

/**
 * Created by joshuagoldberg on 9/2/16.
 */
public class UnsharedFragment extends Fragment implements UnsharedClickListener {

    private static final String TAG = "UnsharedFragment";

    public static final String FRAGMENT_TITLE = "unshared";

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private UnsharedRecyclerAdapter mAdapter;

    private InkedUser mUser;

    private UnsharedFragmentListener mFragmentListener;


    public static UnsharedFragment newInstance() {
        UnsharedFragment fragment = new UnsharedFragment();
        Bundle args = new Bundle();
        args.putString(PortfolioPagerAdapter.FRAGMENT_TITLE, FRAGMENT_TITLE);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UnsharedFragmentListener) {
            mFragmentListener = (UnsharedFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ProfileFragmentListener");
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
        mAdapter = new UnsharedRecyclerAdapter(mUser.getUnshared(), this);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }

    public void onButtonPressed(InkedDatum inkedDatum) {
        if (mFragmentListener != null) {
            mFragmentListener.onUnsharedFragmentInteraction(inkedDatum);
        }
    }

    @Override
    public void onUnsharedClicked(InkedDatum inkedDatum) {
        onButtonPressed(inkedDatum);
    }

    public interface UnsharedFragmentListener {
        void onUnsharedFragmentInteraction(InkedDatum inkedDatum);
    }
}
