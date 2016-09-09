package nullworks.com.inked.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import nullworks.com.inked.models.Datum;

/**
 * Created by joshuagoldberg on 9/8/16.
 */
public class UnsharedRecyclerAdapter extends FirebaseRecyclerAdapter<Datum, MediaViewHolder> {

    private static final String TAG = "FbRecyclerAdapter";

    private double mMeasuredWidth = 0.0;

    public UnsharedRecyclerAdapter(Class<Datum> modelClass, int modelLayout, Class<MediaViewHolder> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    public UnsharedRecyclerAdapter(Class<Datum> modelClass, int modelLayout, Class<MediaViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(final MediaViewHolder viewHolder, final Datum model, int position) {

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

        viewHolder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Add media to "media" in firebase
                FirebaseDatabase.getInstance().getReference().child("media").child(model.getId()).setValue(model);
                // Add media to shared list
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).child("shared").child(model.getId()).setValue(model);
                // Remove media from unshared list
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).child("unshared").child(model.getId()).setValue(null);
                return true;
            }
        });
    }
}
