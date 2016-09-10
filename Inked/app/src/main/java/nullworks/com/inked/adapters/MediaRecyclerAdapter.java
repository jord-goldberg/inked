package nullworks.com.inked.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nullworks.com.inked.R;
import nullworks.com.inked.interfaces.UnsharedClickListener;
import nullworks.com.inked.models.Datum;

/**
 * Created by joshuagoldberg on 9/5/16.
 */
public class MediaRecyclerAdapter extends RecyclerView.Adapter<MediaViewHolder> {

    private static final String TAG = "PortfolioRecyclerAdaper";

    private UnsharedClickListener mListener;

    private ArrayList<Datum> mData;

    private double mMeasuredWidth = 0.0;

    private boolean mIsClickable;

    public MediaRecyclerAdapter(ArrayList<Datum> data, boolean isClickable, UnsharedClickListener listener) {
        mData = data;
        mIsClickable = isClickable;
        mListener = listener;
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

        if (mIsClickable) {
            holder.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mListener.onUnsharedClicked(mData.get(position));
                    notifyDataSetChanged();
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
