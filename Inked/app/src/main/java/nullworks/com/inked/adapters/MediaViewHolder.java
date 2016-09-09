package nullworks.com.inked.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import nullworks.com.inked.R;

/**
 * Created by joshuagoldberg on 9/6/16.
 */
public class MediaViewHolder extends RecyclerView.ViewHolder {

    private ImageView mMainImage;

    public MediaViewHolder(View itemView) {
        super(itemView);

        mMainImage = (ImageView) itemView.findViewById(R.id.card_grid_image);
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
}