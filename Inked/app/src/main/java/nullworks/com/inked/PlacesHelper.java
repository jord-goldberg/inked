package nullworks.com.inked;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

/**
 * Created by joshuagoldberg on 9/7/16.
 */
public class PlacesHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "PlacesHelper";

    private GoogleApiClient mGoogleApiClient;

    public PlacesHelper(GoogleApiClient googleApiClient) {
        mGoogleApiClient = googleApiClient;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected: ");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "Connection failed: " + connectionResult.getErrorCode() + ": " + connectionResult.getErrorMessage());
    }
}
