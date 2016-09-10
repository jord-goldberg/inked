package nullworks.com.inked;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
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
import nullworks.com.inked.fragments.FbRecyclerFragment;
import nullworks.com.inked.fragments.FlipCardFragment;
import nullworks.com.inked.fragments.UnsharedFragment;
import nullworks.com.inked.transformers.ZoomOutPageTransformer;
import nullworks.com.inked.fragments.ProfileFragment;
import nullworks.com.inked.fragments.LoginDialogFragment;
import nullworks.com.inked.fragments.SuggestionFragment;
import nullworks.com.inked.interfaces.InstaService;
import nullworks.com.inked.models.AccessToken;
import nullworks.com.inked.models.Datum;
import nullworks.com.inked.models.Envelope;
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
        ProfileFragment.OnFragmentInteractionListener {

    private static final String TAG = "PortfolioActivity";

    public static final String SHARED_PREFS = "nullworks.com.inked";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String PROFILE_PIC = "profile_pic";

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 300;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private GoogleApiClient mGoogleApiClient;

    private ImageView mProfilePic;
    private TextView mFullNameText;
    private TextView mLocationText;
    private CardView mLocationView;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private PortfolioPagerAdapter mPagerAdapter;

    private InkedUser mUser;

    private ArrayList<Fragment> mFragments;

    private String mUserId;
    private String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUserId = mAuth.getCurrentUser().getUid();

        buildGoogleApiClient();

        mProfilePic = (ImageView) findViewById(R.id.profile_picture);
        mFullNameText = (TextView) findViewById(R.id.fullname_textview);
        mLocationView = (CardView) findViewById(R.id.user_location_card);
        mLocationText = (TextView) findViewById(R.id.user_location_textview);
        mViewPager = (ViewPager) findViewById(R.id.profile_container);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        mFragments = new ArrayList<>();

        mAccessToken = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(ACCESS_TOKEN, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);
                setLocation(place);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Store access token
        getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
                .edit()
                .putString(ACCESS_TOKEN, mAccessToken)
                .commit();

        // Set User data on firebase
        mRef.child("users").child(mUserId).setValue(mUser);

        // Check to see if the user is signed in; this may be after a sign-out
        if (mAuth.getCurrentUser() != null) {

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


    private synchronized void buildGoogleApiClient() {
        PlacesHelper helper = new PlacesHelper(mGoogleApiClient);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(helper)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, helper)
                .build();
    }

    // gets UserSingleton info from firebase
    public void getUserInfo() {

        mRef.child("users").child(mUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserSingleton.getInstance().setUser(dataSnapshot.getValue(InkedUser.class));
                mUser = UserSingleton.getInstance().getUser();

                if (mUser == null) {
                    mUser = new InkedUser();
                }
                if (mUser.getLocation() == null) {
                    mUser.setLocation(new Location());
                }
                if (mUser.getUser() == null) {
                    mUser.setUser(new User());
                }
                if (mUser.getUnshared() == null) {
                    mUser.setUnshared(new ArrayList<Datum>());
                }
                if (mUser.getShared() == null) {
                    mUser.setShared(new ArrayList<Datum>());
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

        if(!isDestroyed()) {

            mFragments.clear();

            int userFlag = 0;

            // get a unique user flag depending on user info
            if (user.getUser().getId() != null)
                userFlag += 3;
            if (user.getLocation().getId() != null)
                userFlag += 5;
            if (user.getProfile() != null)
                userFlag += 7;

            Log.d(TAG, "setLayout: userFlag: " + userFlag);

            // Set full name depending on data available
            if (user.getUser().getFullName() != null) {
                mFullNameText.setText(mUser.getUser().getFullName());
            } else {
                mFullNameText.setText(mAuth.getCurrentUser().getDisplayName());
            }

            // Set profile picture to instagram profile picture if available
            if (mUser.getUser().getProfilePicture() != null) {
                Glide.with(PortfolioActivity.this)
                        .load(mUser.getUser().getProfilePicture().replace("s150x150", ""))
                        .fitCenter()
                        .into(mProfilePic);
            } //TODO: GET A DEFAULT USER PHOTO FOR EVERYONE TO SEE

            // Add a suggestion fragment if user profile is not complete
            if (userFlag != 15) {
                mFragments.add(SuggestionFragment.newInstance(userFlag));
            }
            // Check to see if the user has instagram access token
            if (userFlag == 3 || userFlag == 8 || userFlag == 10 || userFlag == 15) { // has token
//                mFragments.add(FlipCardFragment.newInstance());
                mFragments.add(FbRecyclerFragment.newInstance());
                mFragments.add(UnsharedFragment.newInstance());
            }
            // Check to see if the user has a set location
            if (userFlag == 5 || userFlag == 8 || userFlag == 12 || userFlag == 15) { // has location
                mLocationView.setVisibility(View.VISIBLE);
                mLocationText.setText(mUser.getLocation().getAddress());
                mFragments.add(ProfileFragment.newInstance(userFlag));
            }
            // Check to see if user has a custom profile && no location
            if (userFlag == 7 || userFlag == 10) { // has profile
                mFragments.add(ProfileFragment.newInstance(userFlag));
            }

            mPagerAdapter = new PortfolioPagerAdapter(getFragmentManager(), mFragments);
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            mTabLayout.setupWithViewPager(mViewPager, true);
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
        dialogFragment.show(getFragmentManager(), "dialog");
        //TODO: Add ability to log out of Instagram with warning that all pictures will be unshared.
    }

    // Runs after a user signs in to Instagram
    @Override
    public void onAccessTokenReceived(AccessToken accessToken) {
        mAccessToken = accessToken.getAccessToken();
        mUser.setUser(accessToken.getUser());
        // TODO: Make this happen after the user agrees to save changes
        mRef.child("users").child(mUserId).child("user").setValue(mUser.getUser());
        if (mUser.getUnshared().isEmpty()) {
            instaMediaApiCall();
        }
    }

    public void instaMediaApiCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InstaService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InstaService service = retrofit.create(InstaService.class);

        Call<Envelope> call = service.getRecentMedia(mAccessToken);

        call.enqueue(new Callback<Envelope>() {
            @Override
            public void onResponse(Call<Envelope> call, Response<Envelope> response) {
                try {

                    for (int i = 0; i < response.body().getData().size(); i++) {
                        Datum datum = response.body().getData().get(i);
                        // Check to see if it's an image or video
                        if (datum.getType().equals("image")) {
                            mUser.getUnshared().add(datum);
                        }
                    }
                    mRef.child("users").child(mUserId).child("unshared")
                            .setValue(mUser.getUnshared());
                    Log.i(TAG, "onResponse: Instagram photos added");

                    if (response.body().getMeta().getCode() == 400) {
                        Log.d(TAG, "onResponse: Instagram code: " + response.body().getMeta().getCode());
                        // Access token has expired
                        mAccessToken = null;
                    }
                    //TODO: After Instagram approves this app use Pagination to get more data

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<Envelope> call, Throwable t) {
                Log.w(TAG, "onFailure: ", t);
            }
        });
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
            Log.e(TAG, "placesAutoComplete: ", e);
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
            Log.e(TAG, "placesAutoComplete: ", e);
        }
    }

    public void setLocation(Place place) {
        mUser.getLocation().setAddress(place.getAddress().toString());
        mUser.getLocation().setId(place.getId());
        mUser.getLocation().setLatitude(place.getLatLng().latitude);
        mUser.getLocation().setLongitude(place.getLatLng().longitude);
        mUser.getLocation().setName(place.getName().toString());
        mRef.child("users").child(mUserId).child("location").setValue(mUser.getLocation());
    }
}
