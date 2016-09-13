package nullworks.com.inkfolio.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import nullworks.com.inkfolio.R;
import nullworks.com.inkfolio.adapters.MediaViewHolder;
import nullworks.com.inkfolio.adapters.PortfolioPagerAdapter;
import nullworks.com.inkfolio.adapters.QueryRecyclerAdapter;
import nullworks.com.inkfolio.models.custom.InkDatum;

/**
 * Created by joshuagoldberg on 9/12/16.
 */
public class TagQueryFragment extends Fragment {

    private static final String TAG = "TagQueryFragment";
    private static final String CHILD_TAG = "childTag";

    public static final String FRAGMENT_TITLE = "recent";

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private QueryRecyclerAdapter mAdapter;

    private DatabaseReference mRef;
    private Query mQuery;
    private String mChildTag;

    public static TagQueryFragment newInstance(String tag) {
        TagQueryFragment fragment = new TagQueryFragment();
        Bundle args = new Bundle();
        args.putString(PortfolioPagerAdapter.FRAGMENT_TITLE, FRAGMENT_TITLE);
        args.putString(CHILD_TAG, tag.toLowerCase());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mChildTag = getArguments().getString(CHILD_TAG);
        }
        mRef = FirebaseDatabase.getInstance().getReference();
        mQuery = mRef.child("tags").child(mChildTag).orderByChild("createdTime").limitToFirst(1000);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_recycler, container, false);
        mRecyclerView = (RecyclerView) viewRoot.findViewById(R.id.recycler_media);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mAdapter = new QueryRecyclerAdapter(InkDatum.class, R.layout.card_grid, MediaViewHolder.class, mQuery);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter.cleanup();
    }
}
