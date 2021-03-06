package nullworks.com.inkfolio.fragments;

import android.app.Fragment;
import android.content.Intent;
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

import nullworks.com.inkfolio.EditActivity;
import nullworks.com.inkfolio.MainActivity;
import nullworks.com.inkfolio.R;
import nullworks.com.inkfolio.adapters.QueryRecyclerAdapter;
import nullworks.com.inkfolio.adapters.MediaViewHolder;
import nullworks.com.inkfolio.adapters.PortfolioPagerAdapter;
import nullworks.com.inkfolio.interfaces.SharedClickListener;
import nullworks.com.inkfolio.models.custom.InkDatum;

/**
 * Created by joshuagoldberg on 9/2/16.
 */
public class QueryFragment extends Fragment implements SharedClickListener {

    private static final String TAG = "QueryFragment";

    public static final String FRAGMENT_TITLE = "recent";

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private QueryRecyclerAdapter mAdapter;

    private DatabaseReference mRef;
    private Query mQuery;

    public static QueryFragment newInstance() {
        QueryFragment fragment = new QueryFragment();
        Bundle args = new Bundle();
        args.putString(PortfolioPagerAdapter.FRAGMENT_TITLE, FRAGMENT_TITLE);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRef = FirebaseDatabase.getInstance().getReference();
        mQuery = mRef.child("media").orderByChild("createdTime").limitToFirst(1000);


//        Query query = mRef.child("media").orderByChild("longitude").startAt(-400.0).endAt(100.0);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
//                    InkDatum datum = snapshot.getValue(InkDatum.class);
//                    if (30.0 < datum.getLatitude() && 50.0 > datum.getLatitude()) {
//
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_recycler, container, false);
        mRecyclerView = (RecyclerView) viewRoot.findViewById(R.id.recycler_media);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mAdapter = new QueryRecyclerAdapter(InkDatum.class, R.layout.card_grid, MediaViewHolder.class, mQuery, this);
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

    @Override
    public void onSharedClicked(InkDatum inkDatum) {
        Intent intent = new Intent(getActivity(), EditActivity.class);
        intent.putExtra(EditActivity.INKED_DATUM, inkDatum);
        startActivity(intent);
    }
}
