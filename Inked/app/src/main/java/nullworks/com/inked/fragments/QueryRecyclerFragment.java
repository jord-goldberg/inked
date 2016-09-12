package nullworks.com.inked.fragments;

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

import nullworks.com.inked.R;
import nullworks.com.inked.adapters.QueryRecyclerAdapter;
import nullworks.com.inked.adapters.MediaViewHolder;
import nullworks.com.inked.adapters.PortfolioPagerAdapter;
import nullworks.com.inked.models.custom.InkedDatum;

/**
 * Created by joshuagoldberg on 9/2/16.
 */
public class QueryRecyclerFragment extends Fragment {

    private static final String TAG = "QueryRecyclerFragment";

    public static final String FRAGMENT_TITLE = "recent";

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private QueryRecyclerAdapter mAdapter;

    private DatabaseReference mRef;
    private static Query sQuery;

    public static QueryRecyclerFragment newInstance(Query query) {
        QueryRecyclerFragment fragment = new QueryRecyclerFragment();
        sQuery = query;
        Bundle args = new Bundle();
        args.putString(PortfolioPagerAdapter.FRAGMENT_TITLE, FRAGMENT_TITLE);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRef = FirebaseDatabase.getInstance().getReference().child("media");
        Query query = mRef.orderByChild("longitude").startAt(-400.0).endAt(100.0);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    InkedDatum datum = snapshot.getValue(InkedDatum.class);
                    if (30.0 < datum.getLatitude() && 50.0 > datum.getLatitude()) {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_recycler, container, false);
        mRecyclerView = (RecyclerView) viewRoot.findViewById(R.id.recycler_media);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mAdapter = new QueryRecyclerAdapter(InkedDatum.class, R.layout.card_grid, MediaViewHolder.class, sQuery);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
