package nullworks.com.inkfolio.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

import nullworks.com.inkfolio.adapters.UnsharedRecyclerAdapter;
import nullworks.com.inkfolio.R;
import nullworks.com.inkfolio.UserSingleton;
import nullworks.com.inkfolio.adapters.PortfolioPagerAdapter;
import nullworks.com.inkfolio.interfaces.UnsharedClickListener;
import nullworks.com.inkfolio.models.custom.InkDatum;
import nullworks.com.inkfolio.models.custom.InkUser;

/**
 * Created by joshuagoldberg on 9/2/16.
 */
public class UnsharedFragment extends Fragment implements UnsharedClickListener {

    private static final String TAG = "UnsharedFragment";

    public static final String FRAGMENT_TITLE = "unshared";

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private UnsharedRecyclerAdapter mAdapter;

    private InkUser mUser;

    private Query mQuery;

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
                    + " must implement UnsharedFragmentListener");
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

    public void onButtonPressed(InkDatum inkDatum) {
        if (mFragmentListener != null) {
            mFragmentListener.onUnsharedFragmentInteraction(inkDatum);
        }
    }

    public void addDatum(InkDatum inkDatum) {
        for (int i = 0; i < mUser.getUnshared().size(); i++) {
            if (inkDatum.getCreatedTime() > mUser.getUnshared().get(i).getCreatedTime()) {
                Log.d(TAG, "addDatum: position:" + i);
                mUser.getUnshared().add(i, inkDatum);
                mAdapter.notifyItemInserted(i);
                return;
            }
        }
        Log.d(TAG, "addDatum: position:" + (mUser.getUnshared().size()-1));
        mUser.getUnshared().add(inkDatum);
        mAdapter.notifyItemInserted(mUser.getUnshared().size()-1);
    }

    public void removeDatum(InkDatum inkDatum) {
        int position = mUser.getUnshared().indexOf(inkDatum);
        mUser.getUnshared().remove(inkDatum);
        mAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onUnsharedClicked(InkDatum inkDatum) {
        onButtonPressed(inkDatum);
    }

    public interface UnsharedFragmentListener {
        void onUnsharedFragmentInteraction(InkDatum inkDatum);
    }
}
