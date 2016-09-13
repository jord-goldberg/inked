package nullworks.com.inkfolio.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nullworks.com.inkfolio.R;
import nullworks.com.inkfolio.UserSingleton;
import nullworks.com.inkfolio.interfaces.UnsharedClickListener;
import nullworks.com.inkfolio.models.custom.InkDatum;

/**
 * Created by joshuagoldberg on 9/5/16.
 */
public class UnsharedRecyclerAdapter extends RecyclerView.Adapter<MediaViewHolder> {

    private static final String TAG = "UnsharedRecyclerAdaper";

    private UnsharedClickListener mListener;

    private ArrayList<InkDatum> mData;

    private double mMeasuredWidth = 0.0;

    public UnsharedRecyclerAdapter(ArrayList<InkDatum> data, UnsharedClickListener listener) {
        mData = data;
        mListener = listener;
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View parentView = inflater.inflate(R.layout.card_grid, parent, false);
        MediaViewHolder mainViewHolder = new MediaViewHolder(parentView);
        mainViewHolder.getFab().setImageResource(R.drawable.ic_checked);
        mainViewHolder.getFab().setClickable(false);
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
        if (UserSingleton.getInstance().getDataToShare().contains(mData.get(position))) {
            holder.getMainImage().setImageAlpha(125);
            holder.getFab().setVisibility(View.VISIBLE);
        } else {
            holder.getMainImage().setImageAlpha(255);
            holder.getFab().setVisibility(View.GONE);
        }


        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!UserSingleton.getInstance().getDataToShare().contains(mData.get(position))) {
                    holder.getMainImage().setImageAlpha(125);
                    holder.getFab().setVisibility(View.VISIBLE);
                    UserSingleton.getInstance().getDataToShare().add(mData.get(position));
                } else {
                    holder.getMainImage().setImageAlpha(255);
                    holder.getFab().setVisibility(View.GONE);
                    UserSingleton.getInstance().getDataToShare().remove(mData.get(position));
                }

                mListener.onUnsharedClicked(mData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
