package nullworks.com.inkfolio.adapters;

import android.view.View;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import nullworks.com.inkfolio.interfaces.SharedClickListener;
import nullworks.com.inkfolio.models.custom.InkDatum;

/**
 * Created by joshuagoldberg on 9/6/16.
 */
public class QueryRecyclerAdapter extends FirebaseRecyclerAdapter<InkDatum, MediaViewHolder> {

    private static final String TAG = "QueryRecyclerAdapter";

    private MediaViewHolder mClickedViewHolder;
    private InkDatum mClickedModel;

    private double mMeasuredWidth = 0.0;

    private SharedClickListener mListener;

    public QueryRecyclerAdapter(Class<InkDatum> modelClass, int modelLayout, Class<MediaViewHolder> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    public QueryRecyclerAdapter(Class<InkDatum> modelClass, int modelLayout, Class<MediaViewHolder> viewHolderClass,
                                Query ref, SharedClickListener listener) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mListener = listener;
    }

    @Override
    protected void populateViewHolder(final MediaViewHolder viewHolder, final InkDatum model, int position) {

        // Find the measured width of the imageView; calculate the corresponding height & set it
        if (mMeasuredWidth == 0.0) {
            viewHolder.getMainImage().post(new Runnable() {
                @Override
                public void run() {
                    mMeasuredWidth = (double) viewHolder.getMainImage().getMeasuredWidth();
                }
            });
        } else {
            double dataWidth = (double) model.getImages().getLowResolution().getWidth();
            double dataHeight = (double) model.getImages().getLowResolution().getHeight();
            double height = (mMeasuredWidth / dataWidth) * dataHeight;
            viewHolder.getMainImage().setMinimumHeight((int) height);
            viewHolder.getMainImage().setMaxHeight((int) height);
        }

        Glide.with(viewHolder.getMainImage().getContext())
                .load(model.getImages().getLowResolution().getUrl())
                .fitCenter()
                .into(viewHolder.getMainImage());

        if (model.equals(mClickedModel)) {
            viewHolder.getMainImage().setImageAlpha(125);
            viewHolder.getFab().setVisibility(View.VISIBLE);
        } else {
            viewHolder.getMainImage().setImageAlpha(255);
            viewHolder.getFab().setVisibility(View.GONE);
        }

        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mClickedViewHolder != viewHolder) {
                    if (mClickedViewHolder == null) {
                        mClickedViewHolder = viewHolder;
                    } else {
                        mClickedViewHolder.getMainImage().setImageAlpha(255);
                        mClickedViewHolder.getFab().setVisibility(View.GONE);
                        mClickedViewHolder.setClicked(false);
                        mClickedViewHolder = viewHolder;
                    }
                }

                if (!model.equals(mClickedModel)) {
                    viewHolder.getMainImage().setImageAlpha(125);
                    viewHolder.getFab().setVisibility(View.VISIBLE);
                    mClickedModel = model;
                } else {
                    viewHolder.getMainImage().setImageAlpha(255);
                    viewHolder.getFab().setVisibility(View.GONE);
                    mClickedModel = null;
                }
            }
        });

        viewHolder.getFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSharedClicked(model);
            }
        });
    }
}
