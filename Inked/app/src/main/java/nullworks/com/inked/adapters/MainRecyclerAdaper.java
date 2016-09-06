package nullworks.com.inked.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nullworks.com.inked.R;
import nullworks.com.inked.models.Datum;

/**
 * Created by joshuagoldberg on 8/26/16.
 */
public class MainRecyclerAdaper extends RecyclerView.Adapter<MainRecyclerAdaper.MainViewHolder> {

    private static final String TAG = "MainRecyclerAdaper";

    private ArrayList<Datum> mData;

    private double mMeasuredWidth = 0.0;

    public MainRecyclerAdaper(ArrayList<Datum> data) {
        mData = data;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View parentView = inflater.inflate(R.layout.card_grid, parent, false);
        MainViewHolder mainViewHolder = new MainViewHolder(parentView);
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, int position) {

        double dataWidth = (double) mData.get(position).getImages().getStandardResolution().getWidth();
        double dataHeight = (double) mData.get(position).getImages().getStandardResolution().getHeight();

        //TODO: Optimize this setting of measured width & in PortfolioRecyclerAdapter
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
                
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {

        private ImageView mMainImage;

        public MainViewHolder(View itemView) {
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
