package nullworks.com.inked.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

import nullworks.com.inked.R;
import nullworks.com.inked.adapters.PortfolioPagerAdapter;
import nullworks.com.inked.models.custom.InkedUser;

/**
 * Created by joshuagoldberg on 9/7/16.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "LocationFragment";
    private static final String USER_FLAG = "userFlag";
    private static final String INKED_USER = "inkedUser";

    public static final String FRAGMENT_TITLE = "profile";

    private InkedUser mUser;

    private int mUserFlag;

    CardView mInstaUserNameCard;
    CardView mProfileTextCard;

    TextView mInstaUserNameText;
    TextView mProfileText;

    MapView mMap;

    private OnFragmentInteractionListener mListener;

    public static ProfileFragment newInstance(InkedUser user, int userFlag) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(PortfolioPagerAdapter.FRAGMENT_TITLE, FRAGMENT_TITLE);
        args.putSerializable(INKED_USER, user);
        args.putInt(USER_FLAG, userFlag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (InkedUser) getArguments().getSerializable(INKED_USER);
            mUserFlag = getArguments().getInt(USER_FLAG, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_profile, container, false);

        // Check to see if user is connected with Instagram
        if (mUserFlag == 8 || mUserFlag == 10 || mUserFlag == 15) { // is connected
            mInstaUserNameCard = (CardView) viewRoot.findViewById(R.id.insta_username_card);
            mInstaUserNameText = (TextView) viewRoot.findViewById(R.id.insta_username_textview);
        }
        // Check to see if the user has a set location
        if (mUserFlag == 5 || mUserFlag == 8 || mUserFlag == 12 || mUserFlag == 15) { // has location
            mMap = (MapView) viewRoot.findViewById(R.id.map);
        }
        // Check to see if the user has a custom profile
        if (mUserFlag == 7 || mUserFlag == 10 || mUserFlag == 12 || mUserFlag == 15) { // has profile
            mProfileTextCard = (CardView) viewRoot.findViewById(R.id.profile_text_card);
            mProfileText = (TextView) viewRoot.findViewById(R.id.profile_text_textview);
        }
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check to see if user is connected with Instagram
        if (mUserFlag == 8 || mUserFlag == 10 || mUserFlag == 15) { // is connected
            mInstaUserNameText.setText("Follow " + mUser.getUser().getUsername() + " on ");
        }
        // Check to see if the user has a set location
        if (mUserFlag == 5 || mUserFlag == 8 || mUserFlag == 12 || mUserFlag == 15) { // has location

        }
        // Check to see if the user has a custom profile
        if (mUserFlag == 7 || mUserFlag == 10 || mUserFlag == 12 || mUserFlag == 15) { // has profile

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }


}
