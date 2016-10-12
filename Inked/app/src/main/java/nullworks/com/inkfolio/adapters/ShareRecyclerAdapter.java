package nullworks.com.inkfolio.adapters;

import android.view.View;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import nullworks.com.inkfolio.UserSingleton;
import nullworks.com.inkfolio.interfaces.UnsharedClickListener;
import nullworks.com.inkfolio.models.custom.InkDatum;

/**
 * Created by joshuagoldberg on 9/19/16.
 */

public class ShareRecyclerAdapter extends FirebaseRecyclerAdapter<InkDatum, MediaViewHolder> {

    private double mMeasuredWidth = 0.0;

    private UnsharedClickListener mListener;

    public ShareRecyclerAdapter(Class<InkDatum> modelClass, int modelLayout, Class<MediaViewHolder> viewHolderClass,
                                DatabaseReference ref, UnsharedClickListener listener) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mListener = listener;
    }

    public ShareRecyclerAdapter(Class<InkDatum> modelClass, int modelLayout, Class<MediaViewHolder> viewHolderClass,
                                Query ref, UnsharedClickListener listener) {
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

        // If this image has been clicked, decrease alpha and show the FAB
        if (UserSingleton.getInstance().getDataToShare().contains(model)) {
            viewHolder.getMainImage().setImageAlpha(125);
            viewHolder.getFab().setVisibility(View.VISIBLE);
        } else {
            viewHolder.getMainImage().setImageAlpha(255);
            viewHolder.getFab().setVisibility(View.GONE);
        }

        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserSingleton.getInstance().getDataToShare().contains(model)) {
                    UserSingleton.getInstance().getDataToShare().add(model);
                    viewHolder.getMainImage().setImageAlpha(125);
                    viewHolder.getFab().setVisibility(View.VISIBLE);
                } else {
                    UserSingleton.getInstance().getDataToShare().remove(model);
                    viewHolder.getMainImage().setImageAlpha(255);
                    viewHolder.getFab().setVisibility(View.GONE);
                }
                mListener.onUnsharedClicked(model);
            }
        });

    }
}
