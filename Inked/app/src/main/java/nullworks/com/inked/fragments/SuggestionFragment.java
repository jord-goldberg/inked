package nullworks.com.inked.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import nullworks.com.inked.R;
import nullworks.com.inked.adapters.PortfolioPagerAdapter;

/**
 * Created by joshuagoldberg on 9/7/16.
 */
public class SuggestionFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SuggestionFragment";
    private static final String USER_FLAG = "userFlag";

    public static final String FRAGMENT_TITLE = "suggestion";

    private int mUserFlag;

    private LinearLayout mInstaLoginLayout;
    private LinearLayout mSetLocationLayout;
    private LinearLayout mCustomizeProfileLayout;

    private CardView mInstaLoginButton;
    private CardView mSetLocationButton;
    private CardView mCustomizeProfileButton;

    private SuggestionFragmentListener mListener;

    public static SuggestionFragment newInstance(int userFlag) {
        SuggestionFragment fragment = new SuggestionFragment();
        Bundle args =  new Bundle();
        args.putString(PortfolioPagerAdapter.FRAGMENT_TITLE, FRAGMENT_TITLE);
        args.putInt(USER_FLAG, userFlag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserFlag = getArguments().getInt(USER_FLAG, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_suggestion, container, false);

        // Check to see if the user is connected with Instagram
        if (mUserFlag == 0 || mUserFlag == 5 || mUserFlag == 7 || mUserFlag == 12) { // not connected
            mInstaLoginLayout = (LinearLayout) viewRoot.findViewById(R.id.inst_login_layout);
            mInstaLoginButton = (CardView) viewRoot.findViewById(R.id.insta_login_button);
        }
        // Check to see if the user has a set location
        if (mUserFlag == 0 || mUserFlag == 3 || mUserFlag == 7 || mUserFlag == 10) { // no location
            mSetLocationLayout = (LinearLayout) viewRoot.findViewById(R.id.set_location_layout);
            mSetLocationButton = (CardView) viewRoot.findViewById(R.id.set_location_button);
        }
        // Check to see if the user has a custom profile
        if (mUserFlag == 0 || mUserFlag == 3 || mUserFlag == 5 || mUserFlag == 8) { // no profile
            mCustomizeProfileLayout = (LinearLayout) viewRoot.findViewById(R.id.customize_profile_layout);
            mCustomizeProfileButton = (CardView) viewRoot.findViewById(R.id.customize_profile_button);
        }
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check to see if the user is connected with Instagram
        if (mUserFlag == 0 || mUserFlag == 5 || mUserFlag == 7 || mUserFlag == 12) { // not connected
            mInstaLoginLayout.setVisibility(View.VISIBLE);
            mInstaLoginButton.setOnClickListener(this);
        }
        // Check to see if the user has a set location
        if (mUserFlag == 0 || mUserFlag == 3 || mUserFlag == 7 || mUserFlag == 10) { // no location
            mSetLocationLayout.setVisibility(View.VISIBLE);
            mSetLocationButton.setOnClickListener(this);
        }
        // Check to see if the user has a custom profile
        if (mUserFlag == 0 || mUserFlag == 3 || mUserFlag == 5 || mUserFlag == 8) { // no profile
            mCustomizeProfileLayout.setVisibility(View.VISIBLE);
            mCustomizeProfileButton.setOnClickListener(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SuggestionFragmentListener) {
            mListener = (SuggestionFragmentListener) context;
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

    public void onButtonPressed(int viewId) {
        if (mListener != null) {
            mListener.onSuggestionFragmentInteraction(viewId);
        }
    }

    public interface SuggestionFragmentListener {
        // TODO: Update argument type and name
        void onSuggestionFragmentInteraction(int viewId);
    }

    @Override
    public void onClick(View view) {
        onButtonPressed(view.getId());
    }
}
