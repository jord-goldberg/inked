package nullworks.com.inked.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nullworks.com.inked.R;
import nullworks.com.inked.adapters.PortfolioPagerAdapter;

/**
 * Created by joshuagoldberg on 9/9/16.
 */
public class FlipCardFragment extends Fragment {

    public static final String FRAGMENT_TITLE = "flipcard";

    private FloatingActionButton fab;

    private boolean mShowingBack = false;

    public static FlipCardFragment newInstance() {
        FlipCardFragment fragment = new FlipCardFragment();
        Bundle args =  new Bundle();
        args.putString(PortfolioPagerAdapter.FRAGMENT_TITLE, FRAGMENT_TITLE);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_flipcard, container, false);
        fab = (FloatingActionButton) viewRoot.findViewById(R.id.fab_flipcard);
        float scale = viewRoot.getContext().getResources().getDisplayMetrics().density;
        viewRoot.setCameraDistance(100000 * scale);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.flipcard_container, UnsharedFragment.newInstance())
                    .commit();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mShowingBack) {
                    getChildFragmentManager().popBackStack();
                    return;
                }

                // Flip to the back.

                mShowingBack = true;

                // Create and commit a new fragment transaction that adds the fragment for
                // the back of the card, uses custom animations, and is part of the fragment
                // manager's back stack.

                getChildFragmentManager()
                        .beginTransaction()

                        // Replace the default fragment animations with animator resources
                        // representing rotations when switching to the back of the card, as
                        // well as animator resources representing rotations when flipping
                        // back to the front (e.g. when the system Back button is pressed).
                        .setCustomAnimations(
                                R.animator.card_flip_right_in,
                                R.animator.card_flip_right_out,
                                R.animator.card_flip_left_in,
                                R.animator.card_flip_left_out)

                        // Replace any fragments currently in the container view with a
                        // fragment representing the next page (indicated by the
                        // just-incremented currentPage variable).
                        .replace(R.id.flipcard_container, FbRecyclerFragment.newInstance())

                        // Add this transaction to the back stack, allowing users to press
                        // Back to get to the front of the card.
                        .addToBackStack(null)

                        // Commit the transaction.
                        .commit();
            }
        });

    }
}
