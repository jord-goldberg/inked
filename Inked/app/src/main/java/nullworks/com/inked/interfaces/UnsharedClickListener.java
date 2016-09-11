package nullworks.com.inked.interfaces;

import nullworks.com.inked.adapters.MediaViewHolder;
import nullworks.com.inked.models.Datum;
import nullworks.com.inked.models.custom.InkedDatum;

/**
 * Created by joshuagoldberg on 9/9/16.
 */
public interface UnsharedClickListener {
    void onUnsharedClicked(InkedDatum inkedDatum);
}
