package nullworks.com.inked.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nullworks.com.inked.R;
import nullworks.com.inked.adapters.MainRecyclerAdaper;

/**
 * Created by joshuagoldberg on 9/2/16.
 */
public class RecyclerFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private MainRecyclerAdaper mAdapter;

    private ArrayList<String> strings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        strings = new ArrayList<>();
        strings.add("http://i.imgur.com/rFI2n7F.jpg");
        strings.add("https://s-media-cache-ak0.pinimg.com/736x/3c/ba/a4/3cbaa48313203a3d1f894cbe33bc6191.jpg");
        strings.add("http://media.tumblr.com/tumblr_m344o1hCLA1qm4rc3.jpg");
        strings.add("https://s-media-cache-ak0.pinimg.com/564x/2f/d4/88/2fd4887b9c2e667acc349a35fb3a622c.jpg");
        strings.add("https://www-media.fanlala.com/sites/default/files/content/03/25/14/justin-bieber-instagram-tattoo-2.png");
        strings.add("http://www.galleryoftattoosnow.com/images/ckuploads/images/424658_3926264429080_520095477_n.jpg");
        strings.add("http://i.imgur.com/rFI2n7F.jpg");
        strings.add("https://s-media-cache-ak0.pinimg.com/736x/3c/ba/a4/3cbaa48313203a3d1f894cbe33bc6191.jpg");
        strings.add("http://media.tumblr.com/tumblr_m344o1hCLA1qm4rc3.jpg");
        strings.add("https://s-media-cache-ak0.pinimg.com/564x/2f/d4/88/2fd4887b9c2e667acc349a35fb3a622c.jpg");
        strings.add("https://www-media.fanlala.com/sites/default/files/content/03/25/14/justin-bieber-instagram-tattoo-2.png");
        strings.add("http://www.galleryoftattoosnow.com/images/ckuploads/images/424658_3926264429080_520095477_n.jpg");
        strings.add("http://i.imgur.com/rFI2n7F.jpg");
        strings.add("https://s-media-cache-ak0.pinimg.com/736x/3c/ba/a4/3cbaa48313203a3d1f894cbe33bc6191.jpg");
        strings.add("http://media.tumblr.com/tumblr_m344o1hCLA1qm4rc3.jpg");
        strings.add("https://s-media-cache-ak0.pinimg.com/564x/2f/d4/88/2fd4887b9c2e667acc349a35fb3a622c.jpg");
        strings.add("https://www-media.fanlala.com/sites/default/files/content/03/25/14/justin-bieber-instagram-tattoo-2.png");
        strings.add("http://www.galleryoftattoosnow.com/images/ckuploads/images/424658_3926264429080_520095477_n.jpg");
        strings.add("http://i.imgur.com/rFI2n7F.jpg");
        strings.add("https://s-media-cache-ak0.pinimg.com/736x/3c/ba/a4/3cbaa48313203a3d1f894cbe33bc6191.jpg");
        strings.add("http://media.tumblr.com/tumblr_m344o1hCLA1qm4rc3.jpg");
        strings.add("https://s-media-cache-ak0.pinimg.com/564x/2f/d4/88/2fd4887b9c2e667acc349a35fb3a622c.jpg");
        strings.add("https://www-media.fanlala.com/sites/default/files/content/03/25/14/justin-bieber-instagram-tattoo-2.png");
        strings.add("http://www.galleryoftattoosnow.com/images/ckuploads/images/424658_3926264429080_520095477_n.jpg");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_recycler, container, false);
        mRecyclerView = (RecyclerView) viewRoot.findViewById(R.id.main_recycler);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new MainRecyclerAdaper(strings);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
