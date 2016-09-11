package nullworks.com.inked.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import nullworks.com.inked.models.Datum;
import nullworks.com.inked.models.custom.InkedDatum;

/**
 * Created by joshuagoldberg on 9/6/16.
 */
public class FbRecyclerAdapter extends FirebaseRecyclerAdapter<InkedDatum, MediaViewHolder> {

    private static final String TAG = "FbRecyclerAdapter";

    private MediaViewHolder mClickedViewHolder;
    private InkedDatum mClickedModel;

    private double mMeasuredWidth = 0.0;

    public FbRecyclerAdapter(Class<InkedDatum> modelClass, int modelLayout, Class<MediaViewHolder> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    public FbRecyclerAdapter(Class<InkedDatum> modelClass, int modelLayout, Class<MediaViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(final MediaViewHolder viewHolder, final InkedDatum model, int position) {

        // Find the measured width of the imageView; calculate the corresponding height & set it
        if (mMeasuredWidth == 0.0) {
            viewHolder.getMainImage().post(new Runnable() {
                @Override
                public void run() {
                    mMeasuredWidth = (double) viewHolder.getMainImage().getMeasuredWidth();
                }
            });
        } else {
            double dataWidth = (double) model.getImages().getStandardResolution().getWidth();
            double dataHeight = (double) model.getImages().getStandardResolution().getHeight();
            double height = (mMeasuredWidth / dataWidth) * dataHeight;
            viewHolder.getMainImage().setMinimumHeight((int) height);
            viewHolder.getMainImage().setMaxHeight((int) height);
        }

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

        Glide.with(viewHolder.getMainImage().getContext())
                .load(model.getImages().getStandardResolution().getUrl())
                .fitCenter()
                .into(viewHolder.getMainImage());

        viewHolder.getFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
