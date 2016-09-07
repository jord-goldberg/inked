package nullworks.com.inked.adapters;

import android.util.Log;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import nullworks.com.inked.models.Datum;

/**
 * Created by joshuagoldberg on 9/6/16.
 */
public class FbRecyclerAdapter extends FirebaseRecyclerAdapter<Datum, MediaViewHolder> {

    private static final String TAG = "FbRecyclerAdapter";

    private Object mLock = new Object();
    private double mMeasuredWidth = 0.0;

    public FbRecyclerAdapter(Class<Datum> modelClass, int modelLayout, Class<MediaViewHolder> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    public FbRecyclerAdapter(Class<Datum> modelClass, int modelLayout, Class<MediaViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(final MediaViewHolder viewHolder, Datum model, int position) {

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

        Glide.with(viewHolder.getMainImage().getContext())
                .load(model.getImages().getStandardResolution().getUrl())
                .fitCenter()
                .into(viewHolder.getMainImage());
    }
}
