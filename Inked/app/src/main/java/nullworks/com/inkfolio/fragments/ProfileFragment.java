package nullworks.com.inkfolio.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import nullworks.com.inkfolio.R;
import nullworks.com.inkfolio.UserSingleton;
import nullworks.com.inkfolio.adapters.PortfolioPagerAdapter;
import nullworks.com.inkfolio.models.custom.InkUser;

/**
 * Created by joshuagoldberg on 9/7/16.
 */
public class ProfileFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = "LocationFragment";
    private static final String USER_FLAG = "userFlag";

    public static final String FRAGMENT_TITLE = "profile";

    private InkUser mUser;

    private int mUserFlag;

    private LinearLayout mSetLocationLayout;
    private LinearLayout mInstaLoginLayout;
    private LinearLayout mCustomizeProfileLayout;

    private FloatingActionButton mFab;

    CardView mSetLocationCard;
    CardView mInstaLoginCard;
    CardView mInstaUserNameCard;

    TextView mSetUpText;
    TextView mInstaUserNameText;
    TextView mProfileText;

    EditText mProfileEditText;

    GoogleMap mMap;
    MapFragment mMapFragment;

    private ProfileFragmentListener mListener;

    public static ProfileFragment newInstance(int userFlag) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(PortfolioPagerAdapter.FRAGMENT_TITLE, FRAGMENT_TITLE);
        args.putInt(USER_FLAG, userFlag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = UserSingleton.getInstance().getUser();
        if (getArguments() != null) {
            mUserFlag = getArguments().getInt(USER_FLAG, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_profile, container, false);
        mMapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mCustomizeProfileLayout = (LinearLayout) viewRoot.findViewById(R.id.customize_profile_layout);
        mProfileText = (TextView) viewRoot.findViewById(R.id.profile_text_textview);
        mProfileEditText = (EditText) viewRoot.findViewById(R.id.profile_text_edittext);
        mFab = (FloatingActionButton) viewRoot.findViewById(R.id.fab_profile);

        // Check to see if profile is complete
        if (mUserFlag != 8 || mUserFlag != 15) { // not complete
            mSetUpText = (TextView) viewRoot.findViewById(R.id.set_up_textview);
        }
        // Check to see if user is connected with Instagram
        if (mUserFlag == 3 || mUserFlag == 8 || mUserFlag == 10 || mUserFlag == 15) { // is connetcted
            mInstaUserNameCard = (CardView) viewRoot.findViewById(R.id.insta_username_card);
            mInstaUserNameText = (TextView) viewRoot.findViewById(R.id.insta_username_textview);
        } else { // not connected
            mInstaLoginLayout = (LinearLayout) viewRoot.findViewById(R.id.inst_login_layout);
            mInstaLoginCard = (CardView) viewRoot.findViewById(R.id.insta_login_button);
        }
        // Check to see if the user has a set location
        if (mUserFlag == 0 || mUserFlag == 3 || mUserFlag == 7 || mUserFlag == 10) { // no location
            mSetLocationLayout = (LinearLayout) viewRoot.findViewById(R.id.set_location_layout);
            mSetLocationCard = (CardView) viewRoot.findViewById(R.id.set_location_button);
        }
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check to see if profile is complete
        if (mUserFlag == 8 || mUserFlag == 15) { // not complete
            mSetUpText.setVisibility(View.GONE);
        }
        // Check to see if user is connected with Instagram
        if (mUserFlag == 3 || mUserFlag == 8 || mUserFlag == 10 || mUserFlag == 15) { // is connetcted
            mInstaUserNameCard.setVisibility(View.VISIBLE);
            mInstaUserNameText.setText("Follow " + mUser.getUser().getUsername() + " on");
        } else { // not connected
            mInstaLoginLayout.setVisibility(View.VISIBLE);
            mInstaLoginCard.setOnClickListener(this);
        }
        // Check to see if the user has a set location
        if (mUserFlag == 5 || mUserFlag == 8 || mUserFlag == 12 || mUserFlag == 15) { // has location
            mMapFragment.getView().setVisibility(View.VISIBLE);
            mMapFragment.getView().setClickable(false);
            mMapFragment.getMapAsync(this);
        } else { // no location
            mMapFragment.getView().setVisibility(View.GONE);
            mSetLocationLayout.setVisibility(View.VISIBLE);
            mSetLocationCard.setOnClickListener(this);
        }
        // Check to see if the user has a custom profile
        if (mUserFlag == 0 || mUserFlag == 3 || mUserFlag == 5 || mUserFlag == 8) { // no profile
            mCustomizeProfileLayout.setVisibility(View.VISIBLE);
        } else { // has profile
            mProfileText.setText(mUser.getProfile());
        }

        mFab.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileFragmentListener) {
            mListener = (ProfileFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ProfileFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onButtonPressed(int viewId) {
        if (mListener != null) {
            mListener.onProfileFragmentInteraction(viewId);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        double lat = mUser.getLocation().getLatitude();
        double lng = mUser.getLocation().getLongitude();
        LatLng userLocation = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 11));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_profile:
                if (mCustomizeProfileLayout.getVisibility() == View.VISIBLE) {
                    mCustomizeProfileLayout.setVisibility(View.GONE);
                }
                if (mProfileText.getVisibility() == View.VISIBLE) {
                    mProfileEditText.setText(mProfileText.getText());
                    mProfileText.setVisibility(View.GONE);
                    mProfileEditText.setVisibility(View.VISIBLE);
                    mFab.setImageResource(R.drawable.ic_save);
                } else {
                    mProfileText.setText(mProfileEditText.getText().toString());
                    mProfileText.setVisibility(View.VISIBLE);
                    mProfileEditText.setVisibility(View.GONE);
                    mFab.setImageResource(R.drawable.ic_edit);
                    mUser.setProfile(mProfileEditText.getText().toString());
                    if (mProfileEditText.getText().toString().trim().equals("")) {
                        mCustomizeProfileLayout.setVisibility(View.VISIBLE);
                    }
                }
                break;
            default:
                mListener.onProfileFragmentInteraction(view.getId());
        }
    }

    public interface ProfileFragmentListener {
        // TODO: Update argument type and name
        void onProfileFragmentInteraction(int viewId);
    }


}
