package nullworks.com.inked.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nullworks.com.inked.R;

/**
 * Created by joshuagoldberg on 8/26/16.
 */
public class MainRecyclerAdaper extends RecyclerView.Adapter<MainRecyclerAdaper.MainViewHolder> {

    private static final String TAG = "MainRecyclerAdaper";

    private ArrayList<String> mStrings;

    public MainRecyclerAdaper(ArrayList<String> list) {
        mStrings = list;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View parentView = inflater.inflate(R.layout.card_grid, parent, false);
        MainViewHolder mainViewHolder = new MainViewHolder(parentView);
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        Glide.with(holder.getMainImage().getContext())
                .load(mStrings.get(position))
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
        return mStrings.size();
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
