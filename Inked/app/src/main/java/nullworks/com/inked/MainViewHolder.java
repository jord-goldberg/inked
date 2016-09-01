package nullworks.com.inked;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by joshuagoldberg on 8/26/16.
 */
public class MainViewHolder extends RecyclerView.ViewHolder {

    private ImageView mMainImage;

    public MainViewHolder(View itemView) {
        super(itemView);

        mMainImage = (ImageView) itemView.findViewById(R.id.card_grid_image);
    }

    public ImageView getMainImage() {
        return mMainImage;
    }
}
