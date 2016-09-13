package nullworks.com.inkfolio.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import nullworks.com.inkfolio.R;
import nullworks.com.inkfolio.models.custom.InkDatum;

/**
 * Created by joshuagoldberg on 9/12/16.
 */
public class ImageCardFragment extends Fragment {

    private static final String INK_DATUM = "inkDatum";

    private ImageView mImageView;

    private InkDatum mInkDatum;

    public static ImageCardFragment newInstance(InkDatum inkDatum) {
        ImageCardFragment fragment = new ImageCardFragment();
        Bundle args = new Bundle();
        args.putParcelable(INK_DATUM, inkDatum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mInkDatum = getArguments().getParcelable(INK_DATUM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_imagecard, container, false);
        mImageView = (ImageView) viewRoot.findViewById(R.id.card_image);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(view.getContext())
                .load(mInkDatum.getImages().getStandardResolution().getUrl())
                .fitCenter()
                .into(mImageView);
    }
}
