package nullworks.com.inked.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nullworks.com.inked.R;
import nullworks.com.inked.models.Datum;

/**
 * Created by joshuagoldberg on 9/5/16.
 */
public class PortfolioRecyclerAdapter extends RecyclerView.Adapter<PortfolioRecyclerAdapter.PortfolioViewHolder> {

    private static final String TAG = "PortfolioRecyclerAdaper";

    private ArrayList<Datum> mData;

    private NewMediaClicked mNewMediaClicked;

    private double mMeasuredWidth = 0.0;

    public PortfolioRecyclerAdapter(ArrayList<Datum> data) {
        mData = data;
    }

    @Override
    public PortfolioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mNewMediaClicked = (NewMediaClicked) parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View parentView = inflater.inflate(R.layout.card_grid, parent, false);
        PortfolioViewHolder mainViewHolder = new PortfolioViewHolder(parentView);
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(final PortfolioViewHolder holder, final int position) {

        double dataWidth = (double) mData.get(position).getImages().getStandardResolution().getWidth();
        double dataHeight = (double) mData.get(position).getImages().getStandardResolution().getHeight();

        //TODO: Optimize this setting of measured width & in MainRecyclerAdapter
        synchronized (mData) {
            if (mMeasuredWidth == 0.0) {
                holder.getMainImage().post(new Runnable() {
                    @Override
                    public void run() {
                        mMeasuredWidth = (double) holder.mMainImage.getMeasuredWidth();
                    }
                });
            }
        }
        if (mMeasuredWidth != 0) {
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
                    holder.getMainImage().setImageAlpha(155);
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

    class PortfolioViewHolder extends RecyclerView.ViewHolder {

        private ImageView mMainImage;

        public PortfolioViewHolder(View itemView) {
            super(itemView);

            mMainImage = (ImageView) itemView.findViewById(R.id.card_grid_image);
        }

        public void setOnClickListener(View.OnClickListener onClickListener){
            itemView.setOnClickListener(onClickListener);
        }

        public ImageView getMainImage() {
            return mMainImage;
        }
    }
}
