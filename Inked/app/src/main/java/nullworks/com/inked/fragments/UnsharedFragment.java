package nullworks.com.inked.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import nullworks.com.inked.InstaService;
import nullworks.com.inked.PortfolioActivity;
import nullworks.com.inked.R;
import nullworks.com.inked.adapters.FbRecyclerAdapter;
import nullworks.com.inked.adapters.InstaRecyclerAdapter;
import nullworks.com.inked.adapters.MediaViewHolder;
import nullworks.com.inked.adapters.PortfolioPagerAdapter;
import nullworks.com.inked.adapters.UnsharedRecyclerAdapter;
import nullworks.com.inked.models.Datum;
import nullworks.com.inked.models.Envelope;
import nullworks.com.inked.models.custom.InkedUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by joshuagoldberg on 9/2/16.
 */
public class UnsharedFragment extends Fragment {

    private static final String TAG = "UnsharedFragment";
    private static final String INKED_USER = "inkedUser";
    private static final String USER_FLAG = "userFlag";

    public static final String FRAGMENT_TITLE = "unshared";

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private UnsharedRecyclerAdapter mAdapter;

    private DatabaseReference mRef;

    private InkedUser mUser;

    private ArrayList<Datum> mData;

    private String mUserId;
    private String mAccessToken;
    private String mNextMaxId;

    private UnsharedFragmentListener mListener;

    public static UnsharedFragment newInstance(InkedUser user) {
        UnsharedFragment fragment = new UnsharedFragment();
        Bundle args = new Bundle();
        args.putString(PortfolioPagerAdapter.FRAGMENT_TITLE, FRAGMENT_TITLE);
        args.putSerializable(INKED_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof UnsharedFragmentListener) {
//            mListener = (UnsharedFragmentListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
        mAccessToken = context.getSharedPreferences(PortfolioActivity.SHARED_PREFS,
                Context.MODE_PRIVATE).getString(PortfolioActivity.ACCESS_TOKEN, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (InkedUser) getArguments().getSerializable(INKED_USER);
        }
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference().child("users").child(mUserId).child("unshared");

        mData = new ArrayList<>();
        if (mUser.getUnsharedMedia() == null) {
            instaMediaApiCall();
        } else {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_recycler, container, false);
        mRecyclerView = (RecyclerView) viewRoot.findViewById(R.id.recycler_media);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mAdapter = new UnsharedRecyclerAdapter(Datum.class, R.layout.card_grid, MediaViewHolder.class, mRef);
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
        mListener = null;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onUnsharedFragmentInteraction();
        }
    }

    public interface UnsharedFragmentListener {
        void onUnsharedFragmentInteraction();
    }

    public void instaMediaApiCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InstaService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InstaService service = retrofit.create(InstaService.class);

        Call<Envelope> call = service.getMedia(mAccessToken, mNextMaxId);

        call.enqueue(new Callback<Envelope>() {
            @Override
            public void onResponse(Call<Envelope> call, Response<Envelope> response) {
                try {

                    for (int i = 0; i < response.body().getData().size(); i++) {
                        Datum datum = response.body().getData().get(i);
                        // Check to see if it's an image or video
                        if (datum.getType().equals("image")) {
                            mRef.child(datum.getId()).setValue(datum);
                        }
                    }

                    if (response.body().getMeta().getCode() == 400) {
                        // Access token has expired
                        nullAccessToken();
                    }

                    //TODO: After Instagram approves this app, handle more than 20 items and multiple Api calls
                    mNextMaxId = response.body().getPagination().getNextMaxId();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Envelope> call, Throwable t) {
                Log.d(TAG, "onFailure: ", t);
            }
        });

    }

    public void nullAccessToken() {
        getContext().getSharedPreferences(PortfolioActivity.SHARED_PREFS, Context.MODE_PRIVATE)
                .edit()
                .putString(PortfolioActivity.ACCESS_TOKEN, null)
                .commit();

        getActivity().startActivity(new Intent(getContext(), PortfolioActivity.class));
        getActivity().finish();
    }
}
