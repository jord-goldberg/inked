package nullworks.com.inkfolio.adapters;

import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import nullworks.com.inkfolio.R;
import nullworks.com.inkfolio.interfaces.QueryClickListener;
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

    private QueryClickListener mListener;

    public QueryRecyclerAdapter(Class<InkDatum> modelClass, int modelLayout, Class<MediaViewHolder> viewHolderClass,
                                DatabaseReference ref, QueryClickListener listener) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mListener = listener;
    }

    public QueryRecyclerAdapter(Class<InkDatum> modelClass, int modelLayout, Class<MediaViewHolder> viewHolderClass,
                                Query ref, QueryClickListener listener) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mListener = listener;
    }

    @Override
    protected void populateViewHolder(final MediaViewHolder viewHolder, final InkDatum model, final int position) {

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

        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onQueryItemClicked(viewHolder.getMainImage(), model);
            }
        });
    }
}
