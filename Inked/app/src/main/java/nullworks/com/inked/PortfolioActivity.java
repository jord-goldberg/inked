package nullworks.com.inked;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import nullworks.com.inked.adapters.PortfolioPagerAdapter;
import nullworks.com.inked.adapters.InstaRecyclerAdapter;
import nullworks.com.inked.fragments.ProfileFragment;
import nullworks.com.inked.fragments.LoginDialogFragment;
import nullworks.com.inked.fragments.SuggestionFragment;
import nullworks.com.inked.models.AccessToken;
import nullworks.com.inked.models.Datum;
import nullworks.com.inked.models.Media;
import nullworks.com.inked.models.User;
import nullworks.com.inked.models.custom.InkedUser;
import nullworks.com.inked.models.custom.Location;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PortfolioActivity extends AppCompatActivity
        implements LoginDialogFragment.AccessTokenReceived,
        SuggestionFragment.SuggestionFragmentListener,
        ProfileFragment.OnFragmentInteractionListener,
        InstaRecyclerAdapter.NewMediaClicked {

    private static final String TAG = "PortfolioActivity";

    public static final String SHARED_PREFS = "nullworks.com.inked";
    public static final String ACCESS_TOKEN = "access_token";

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 300;

    private String mUserId;
    private String mAccessToken;

    private ImageView mProfilePic;
    private TextView mFullNameText;
    private TextView mLocationText;
    private CardView mLocationView;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private PortfolioPagerAdapter mPagerAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private GoogleApiClient mGoogleApiClient;

    private InkedUser mUser;

    private ArrayList<Datum> mData;
    private ArrayList<Datum> mNewMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mData = new ArrayList<>();
        mNewMedia = new ArrayList<>();

        mProfilePic = (ImageView) findViewById(R.id.profile_picture);
        mFullNameText = (TextView) findViewById(R.id.fullname_textview);
        mLocationView = (CardView) findViewById(R.id.user_location_card);
        mLocationText = (TextView) findViewById(R.id.user_location_textview);
        mViewPager = (ViewPager) findViewById(R.id.profile_container);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        mAccessToken = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(ACCESS_TOKEN, null);

        mRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUserId = mAuth.getCurrentUser().getUid();

        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mAccessToken != null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(InstaService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            InstaService service = retrofit.create(InstaService.class);

            Call<Media> call = service.getRecentMedia(mAccessToken);

            call.enqueue(new Callback<Media>() {
                @Override
                public void onResponse(Call<Media> call, Response<Media> response) {
                    try {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            if (response.body().getData().get(i).getType().equals("image")) {
                                mData.add(response.body().getData().get(i));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<Media> call, Throwable t) {
                    Log.d(TAG, "onFailure: ", t);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                mUser.getLocation().setAddress(place.getAddress().toString());
                mUser.getLocation().setId(place.getId());
                mUser.getLocation().setLatitude(place.getLatLng().latitude);
                mUser.getLocation().setLongitude(place.getLatLng().longitude);
                mUser.getLocation().setName(place.getName().toString());
                mRef.child("users").child(mAuth.getCurrentUser().getUid()).setValue(mUser);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        startActivity(new Intent(this, PortfolioActivity.class));
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Store access token
        getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
                .edit()
                .putString(ACCESS_TOKEN, mAccessToken)
                .commit();
        // Check to see if the user is signed in; this may be after a sign-out
        if (mAuth.getCurrentUser() != null) {
            for (int i = 0; i < mNewMedia.size(); i++)
                mRef.child("media").child(mNewMedia.get(i).getId()).setValue(mNewMedia.get(i));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.portfolio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signOut:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                            }
                        });
                return true;
            case android.R.id.home:
                onNavigateUp();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction() {
        placesAutoComplete();
    }

    @Override
    public void onNewMediaClicked(Datum datum) {
        if (!mNewMedia.contains(datum))
            mNewMedia.add(datum);
        else
            mNewMedia.remove(datum);

    }

    private synchronized void buildGoogleApiClient() {
        PlacesHelper helper = new PlacesHelper();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(helper)
                .addOnConnectionFailedListener(helper)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, helper)
                .build();
    }

    // sets user info from firebase (one-time event)
    public void setUserInfo() {
        mRef.child("users").child(mUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(InkedUser.class);
                if (mUser == null) {
                    mUser = new InkedUser(new User(), new Location(), null, null, null);
                }
                if (mUser.getLocation() == null) {
                    mUser.setLocation(new Location());
                }
                if (mUser.getUser() == null) {
                    mUser.setUser(new User());
                }
                setLayout(mUser);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled: " + databaseError.getMessage(), databaseError.toException());
            }
        });
    }

    public void setLayout(InkedUser user) {
        int userFlag = 0;

        // get a unique user flag depending on user info
        if (user.getUser().getId() != null)
            userFlag += 3;
        if (user.getLocation().getId() != null)
            userFlag += 5;
        if (user.getProfile() != null)
            userFlag += 7;

        Log.d(TAG, "setLayout: userFlag: " + userFlag);

        // Check to see if the user is connected with instagram
        if (userFlag == 0 || userFlag == 5 || userFlag == 7 || userFlag == 12) { // not connected
            Log.d(TAG, "setLayout: " + mAuth.getCurrentUser().getPhotoUrl());
            mFullNameText.setText(mAuth.getCurrentUser().getDisplayName());
            //TODO: GET A DEFAULT USER PHOTO FOR EVERYONE TO SEE
            Glide.with(PortfolioActivity.this)
                    .load(mAuth.getCurrentUser().getPhotoUrl())
                    .fitCenter()
                    .into(mProfilePic);
            //TODO: GET A DEFAULT USER PHOTO FOR EVERYONE TO SEE
        } else { // connected
            mFullNameText.setText(mUser.getUser().getFullName());
            Glide.with(PortfolioActivity.this)
                    .load(mUser.getUser().getProfilePicture().replace("s150x150", ""))
                    .fitCenter()
                    .into(mProfilePic);
            if (mAccessToken == null) { // connected, but no access token
                userFlag -= 3;
            }
        }

        // Check to see if the user has a set location
        if (userFlag == 0 || userFlag == 3 || userFlag == 7 || userFlag == 10) { // no location
            mLocationView.setVisibility(View.GONE);
        } else { // location set
            mLocationText.setText(mUser.getLocation().getAddress());
        }

        mPagerAdapter = new PortfolioPagerAdapter(getSupportFragmentManager(), mUser, userFlag);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mTabLayout.setupWithViewPager(mViewPager, true);
        if (userFlag == 0) {
            mTabLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuggestionFragmentInteraction(int viewId) {
        switch (viewId) {
            default:
            case R.id.insta_login_button:
                instagramSignIn();
                break;
            case R.id.set_location_button:
                placesAutoComplete();
                break;
            case R.id.customize_profile_button:
                break;
        }
    }

    public void instagramSignIn() {
        DialogFragment dialogFragment = new LoginDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "dialog");
        //TODO: Add ability to log out of Instagram with warning that all pictures will be unshared.
    }

    // Runs after a user signs in to Instagram
    @Override
    public void onAccessTokenReceived(AccessToken accessToken) {
        mAccessToken = accessToken.getAccessToken();
        mUser.setUser(accessToken.getUser());
        // TODO: Make this happen after the user agrees to save changes
        mRef.child("users").child(mAuth.getCurrentUser().getUid()).setValue(mUser);
        startActivity(new Intent(this, PortfolioActivity.class));
        finish();
    }

    public void placesAutoComplete() {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(typeFilter)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }
}
