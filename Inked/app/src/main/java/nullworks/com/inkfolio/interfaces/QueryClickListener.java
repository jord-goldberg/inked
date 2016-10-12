package nullworks.com.inkfolio.interfaces;

import android.view.View;

import java.util.ArrayList;

import nullworks.com.inkfolio.models.custom.InkDatum;

/**
 * Created by joshuagoldberg on 9/14/16.
 */
public interface QueryClickListener {
    void onQueryItemClicked(View view, InkDatum inkDatum);
}
