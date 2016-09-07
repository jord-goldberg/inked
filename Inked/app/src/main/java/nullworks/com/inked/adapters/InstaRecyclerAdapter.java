package nullworks.com.inked.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nullworks.com.inked.R;
import nullworks.com.inked.models.Datum;

/**
 * Created by joshuagoldberg on 9/5/16.
 */
public class InstaRecyclerAdapter extends RecyclerView.Adapter<MediaViewHolder> {

    private static final String TAG = "PortfolioRecyclerAdaper";

    private ArrayList<Datum> mData;

    private NewMediaClicked mNewMediaClicked;

    private double mMeasuredWidth = 0.0;

    public InstaRecyclerAdapter(ArrayList<Datum> data) {
        mData = data;
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mNewMediaClicked = (NewMediaClicked) parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View parentView = inflater.inflate(R.layout.card_grid, parent, false);
        MediaViewHolder mainViewHolder = new MediaViewHolder(parentView);
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(final MediaViewHolder holder, final int position) {

        if (mMeasuredWidth == 0.0) {
            holder.getMainImage().post(new Runnable() {
                @Override
                public void run() {
                    mMeasuredWidth = (double) holder.getMainImage().getMeasuredWidth();
                }
            });
        } else {
            double dataWidth = (double) mData.get(position).getImages().getStandardResolution().getWidth();
            double dataHeight = (double) mData.get(position).getImages().getStandardResolution().getHeight();
            double height = (mMeasuredWidth / dataWidth) * dataHeight;
            holder.getMainImage().setMinimumHeight((int) height);
            holder.getMainImage().setMaxHeight((int) height);
        }

        Glide.with(holder.getMainImage().getContext())
                .load(mData.get(position).getImages().getStandardResolution().getUrl())
                .fitCenter()
                .into(holder.getMainImage());

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.getMainImage().getImageAlpha() == 255)
                    holder.getMainImage().setImageAlpha(125);
                else
                    holder.getMainImage().setImageAlpha(255);
                mNewMediaClicked.onNewMediaClicked(mData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface NewMediaClicked {
        void onNewMediaClicked(Datum datum);
    }
}
