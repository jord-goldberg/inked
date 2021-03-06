package nullworks.com.inkfolio.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nullworks.com.inkfolio.R;
import nullworks.com.inkfolio.interfaces.SharedClickListener;
import nullworks.com.inkfolio.models.custom.InkDatum;

/**
 * Created by joshuagoldberg on 9/11/16.
 */
public class SharedRecyclerAdapter extends RecyclerView.Adapter<MediaViewHolder> {

    private static final String TAG = "SharedRecyclerAdaper";

    private SharedClickListener mListener;

    private ArrayList<InkDatum> mData;

    private MediaViewHolder mClickedViewHolder;
    private InkDatum mClickedModel;

    private double mMeasuredWidth = 0.0;

    public SharedRecyclerAdapter(ArrayList<InkDatum> data, SharedClickListener listener) {
        mData = data;
        mListener = listener;
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View parentView = inflater.inflate(R.layout.card_grid, parent, false);
        MediaViewHolder mainViewHolder = new MediaViewHolder(parentView);
        mainViewHolder.getFab().setImageResource(R.drawable.ic_unshare);
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(final MediaViewHolder holder, final int position) {

        // Find the measured width of the imageView; calculate the corresponding height & set it
        if (mMeasuredWidth == 0.0) {
            holder.getMainImage().post(new Runnable() {
                @Override
                public void run() {
                    mMeasuredWidth = (double) holder.getMainImage().getMeasuredWidth();
                }
            });
        } else {
            double dataWidth = (double) mData.get(position).getImages().getLowResolution().getWidth();
            double dataHeight = (double) mData.get(position).getImages().getLowResolution().getHeight();
            double height = (mMeasuredWidth / dataWidth) * dataHeight;
            holder.getMainImage().setMinimumHeight((int) height);
            holder.getMainImage().setMaxHeight((int) height);
        }

        Glide.with(holder.getMainImage().getContext())
                .load(mData.get(position).getImages().getLowResolution().getUrl())
                .fitCenter()
                .into(holder.getMainImage());

        // If this image has been clicked, decrease alpha and show the FAB
        if (mData.get(position).equals(mClickedModel)) {
            holder.getMainImage().setImageAlpha(125);
            holder.getFab().setVisibility(View.VISIBLE);
        } else {
            holder.getMainImage().setImageAlpha(255);
            holder.getFab().setVisibility(View.GONE);
        }

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mClickedViewHolder != holder) {
                    if (mClickedViewHolder == null) {
                        mClickedViewHolder = holder;
                    } else {
                        mClickedViewHolder.getMainImage().setImageAlpha(255);
                        mClickedViewHolder.getFab().setVisibility(View.GONE);
                        mClickedViewHolder.setClicked(false);
                        mClickedViewHolder = holder;
                    }
                }
                if (!mData.get(position).equals(mClickedModel)) {
                    holder.getMainImage().setImageAlpha(125);
                    holder.getFab().setVisibility(View.VISIBLE);
                    mClickedModel = mData.get(position);
                } else {
                    holder.getMainImage().setImageAlpha(255);
                    holder.getFab().setVisibility(View.GONE);
                    mClickedModel = null;
                }
            }
        });

        holder.getFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSharedClicked(mData.get(position));
                mClickedModel = null;
                mClickedViewHolder = null;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}