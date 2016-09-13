package nullworks.com.inkfolio.adapters;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import nullworks.com.inkfolio.R;

/**
 * Created by joshuagoldberg on 9/6/16.
 */
public class MediaViewHolder extends RecyclerView.ViewHolder {

    private FloatingActionButton mFab;
    private ImageView mMainImage;

    private boolean mClicked;

    public MediaViewHolder(View itemView) {
        super(itemView);

        mFab = (FloatingActionButton) itemView.findViewById(R.id.fab_gridcard);
        mMainImage = (ImageView) itemView.findViewById(R.id.card_grid_image);
        mClicked = false;
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        itemView.setOnClickListener(onClickListener);
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener){
        itemView.setOnLongClickListener(onLongClickListener);
    }

    public ImageView getMainImage() {
        return mMainImage;
    }

    public FloatingActionButton getFab() {
        return mFab;
    }

    public boolean isClicked() {
        return mClicked;
    }

    public void setClicked(boolean mClicked) {
        this.mClicked = mClicked;
    }
}