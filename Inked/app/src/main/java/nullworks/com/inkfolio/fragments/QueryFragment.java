package nullworks.com.inkfolio.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import nullworks.com.inkfolio.R;
import nullworks.com.inkfolio.adapters.QueryRecyclerAdapter;
import nullworks.com.inkfolio.adapters.MediaViewHolder;
import nullworks.com.inkfolio.adapters.PortfolioPagerAdapter;
import nullworks.com.inkfolio.interfaces.QueryClickListener;
import nullworks.com.inkfolio.interfaces.QueryFragmentListener;
import nullworks.com.inkfolio.models.custom.InkDatum;

/**
 * Created by joshuagoldberg on 9/2/16.
 */
public class QueryFragment extends Fragment implements QueryClickListener {

    private static final String TAG = "QueryFragment";
    private static final String LAYOUT_STATE = "layoutState";

    public static final String FRAGMENT_TITLE = "recent";

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private QueryRecyclerAdapter mAdapter;

    private NestedScrollView mDetailLayout;
    private ImageView mDetailImage;

    private Query mQuery;

    private Animator mCurrentAnimator;

    private int mShortAnimationDuration;

    private QueryFragmentListener mFragmentListener;

    public static QueryFragment newInstance() {
        QueryFragment fragment = new QueryFragment();
        Bundle args = new Bundle();
        args.putString(PortfolioPagerAdapter.FRAGMENT_TITLE, FRAGMENT_TITLE);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof QueryFragmentListener) {
            mFragmentListener = (QueryFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SharedFragmentListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQuery = FirebaseDatabase.getInstance().getReference()
                .child("media")
                .orderByChild("createdTime")
                .limitToFirst(1000);

        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_query, container, false);

        mRecyclerView = (RecyclerView) viewRoot.findViewById(R.id.recycler_query);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mAdapter = new QueryRecyclerAdapter(InkDatum.class, R.layout.card_grid, MediaViewHolder.class, mQuery, this);

        mDetailLayout = (NestedScrollView) viewRoot.findViewById(R.id.detail_layout);
        mDetailImage = (ImageView) viewRoot.findViewById(R.id.detail_image);

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (mRecyclerView != null && mDetailLayout != null) {
                mRecyclerView.setAlpha(1);
                mRecyclerView.setVisibility(View.VISIBLE);
                mDetailLayout.setVisibility(View.GONE);
            }
        }
    }

    public void onButtonPressed() {
        if (mFragmentListener != null) {
            mFragmentListener.onQueryFragmentInteration();
        }
    }

    @Override
    public void onQueryItemClicked(final View view, InkDatum inkDatum) {

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                zoomImageFromThumb(view, resource);
            }
        };

        Glide.with(getActivity())
                .load(inkDatum.getImages().getStandardResolution().getUrl())
                .asBitmap()
                .into(target);
    }

    private void zoomImageFromThumb(final View thumbView, Bitmap resource) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
            mRecyclerView.animate().cancel();
            mDetailLayout.animate().cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        mDetailImage.setImageBitmap(resource);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        mDetailLayout.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        mRecyclerView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mRecyclerView.setVisibility(View.INVISIBLE);
                    }
                });

        mDetailLayout.setAlpha(1);
        mDetailLayout.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        mDetailImage.setPivotX(0f);
        mDetailImage.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(mDetailImage, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(mDetailImage, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(mDetailImage, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(mDetailImage,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        mDetailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                    mRecyclerView.animate().cancel();
                    mDetailLayout.animate().cancel();
                }

                mRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerView.animate()
                        .alpha(1f)
                        .setDuration(mShortAnimationDuration)
                        .setListener(null);
                mDetailLayout.animate()
                        .alpha(0f)
                        .setStartDelay(mShortAnimationDuration/3)
                        .setDuration(mShortAnimationDuration*2/3);

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(mDetailImage, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(mDetailImage, View.Y,startBounds.top))
                        .with(ObjectAnimator.ofFloat(mDetailImage, View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator.ofFloat(mDetailImage, View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mDetailLayout.setVisibility(View.INVISIBLE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        mDetailLayout.setVisibility(View.INVISIBLE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
}
