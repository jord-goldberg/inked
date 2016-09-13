package nullworks.com.inkfolio;

import android.app.FragmentTransaction;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import nullworks.com.inkfolio.fragments.ImageCardFragment;
import nullworks.com.inkfolio.fragments.ProfileFragment;
import nullworks.com.inkfolio.fragments.TagSearchFragment;
import nullworks.com.inkfolio.models.custom.InkDatum;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";

    public static final String INKED_DATUM = "inkedDatum";

    private FloatingActionButton mFab;

    private InkDatum mEditDatum;

    private boolean mShowingBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEditDatum = getIntent().getParcelableExtra(INKED_DATUM);

        mFab = (FloatingActionButton) findViewById(R.id.fab_flipcard);
        mFab.setVisibility(View.GONE);

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.flipcard_container, ImageCardFragment.newInstance(mEditDatum))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mShowingBack) {
                    getFragmentManager().popBackStack();
                    return;
                }

                // Flip to the back.

                mShowingBack = true;

                // Create and commit a new fragment transaction that adds the fragment for
                // the back of the card, uses custom animations, and is part of the fragment
                // manager's back stack.

                getFragmentManager()
                        .beginTransaction()

                        // Replace the default fragment animations with animator resources
                        // representing rotations when switching to the back of the card, as
                        // well as animator resources representing rotations when flipping
                        // back to the front (e.g. when the system Back button is pressed).
//                        .setCustomAnimations(
//                                R.animator.card_flip_right_in,
//                                R.animator.card_flip_right_out,
//                                R.animator.card_flip_left_in,
//                                R.animator.card_flip_left_out)

                        // Replace any fragments currently in the container view with a
                        // fragment representing the next page (indicated by the
                        // just-incremented currentPage variable).
                        .replace(R.id.flipcard_container, ImageCardFragment.newInstance(mEditDatum))

                        // Add this transaction to the back stack, allowing users to press
                        // Back to get to the front of the card.
                        .addToBackStack(null)

                        // Commit the transaction.
                        .commit();
            }
        });

    }
}
