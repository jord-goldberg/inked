package nullworks.com.inkfolio;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
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

import nullworks.com.inkfolio.adapters.PortfolioPagerAdapter;
import nullworks.com.inkfolio.fragments.SharedFragment;
import nullworks.com.inkfolio.fragments.UnsharedFragment;
import nullworks.com.inkfolio.models.custom.InkDatum;
import nullworks.com.inkfolio.transformers.ZoomOutPageTransformer;
import nullworks.com.inkfolio.fragments.ProfileFragment;
import nullworks.com.inkfolio.fragments.LoginDialogFragment;
import nullworks.com.inkfolio.interfaces.InstaService;
import nullworks.com.inkfolio.models.AccessToken;
import nullworks.com.inkfolio.models.Datum;
import nullworks.com.inkfolio.models.Envelope;
import nullworks.com.inkfolio.models.User;
import nullworks.com.inkfolio.models.custom.InkUser;
import nullworks.com.inkfolio.models.custom.Location;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PortfolioActivity extends AppCompatActivity
        implements LoginDialogFragment.AccessTokenReceived,
        ProfileFragment.ProfileFragmentListener,
        UnsharedFragment.UnsharedFragmentListener,
        SharedFragment.SharedFragmentListener {

    private static final String TAG = "PortfolioActivity";

    public static final String SHARED_PREFS = "nullworks.com.inked";
    public static final String ACCESS_TOKEN = "access_token";

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 300;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private ValueEventListener mValueListener;

    private CoordinatorLayout mCoordinatorLayout;
    private ImageView mProfilePic;
    private TextView mFullNameText;
    private TextView mLocationText;
    private CardView mLocationView;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private PortfolioPagerAdapter mPagerAdapter;

    private FloatingActionButton mShareUnsharedFab;

    private InkUser mUser;

    private SharedFragment mSharedFragment;
    private UnsharedFragment mUnsharedFragment;

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

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mProfilePic = (ImageView) findViewById(R.id.profile_picture);
        mFullNameText = (TextView) findViewById(R.id.fullname_textview);
        mLocationView = (CardView) findViewById(R.id.user_location_card);
        mLocationText = (TextView) findViewById(R.id.user_location_textview);
        mViewPager = (ViewPager) findViewById(R.id.profile_container);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mShareUnsharedFab = (FloatingActionButton) findViewById(R.id.fab_share_media);

        mFragments = new ArrayList<>();

        mAccessToken = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(ACCESS_TOKEN, null);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mPagerAdapter.getItem(position).getArguments()
                        .getString(PortfolioPagerAdapter.FRAGMENT_TITLE, "")
                        .equals(UnsharedFragment.FRAGMENT_TITLE)
                        && !UserSingleton.getInstance().getDataToShare().isEmpty()) {
                    mShareUnsharedFab.setVisibility(View.VISIBLE);
                } else {
                    mShareUnsharedFab.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Adjust shared and unshared lists; clear the dataToShare list
        mShareUnsharedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareUnsharedFab.setVisibility(View.GONE);

                for (int i = 0; i < UserSingleton.getInstance().getDataToShare().size(); i++) {
                    mUser.getShared().add(UserSingleton.getInstance().getDataToShare().get(i));
                    mUser.getUnshared().remove(UserSingleton.getInstance().getDataToShare().get(i));
                }
                mUnsharedFragment.notifyDataSetChanged();
                mSharedFragment.notifyDataSetChanged();

                Snackbar.make(mCoordinatorLayout, UserSingleton.getInstance()
                        .getDataToShare().size() + " photos shared", Snackbar.LENGTH_SHORT)

                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mViewPager.getCurrentItem() == 1) {
                                    mShareUnsharedFab.setVisibility(View.VISIBLE);
                                }
                                for (int i = 0; i < UserSingleton.getInstance().getDataToShare().size(); i++) {
                                    mUser.getUnshared().add(0, UserSingleton.getInstance().getDataToShare().get(i));
                                    mUser.getShared().remove(UserSingleton.getInstance().getDataToShare().get(i));
                                }
                                mUnsharedFragment.notifyDataSetChanged();
                                mSharedFragment.notifyDataSetChanged();
                            }
                        })

                        .setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                if (event == DISMISS_EVENT_TIMEOUT) {
                                    UserSingleton.getInstance().getDataToShare().clear();
                                }
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
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
    protected void onPause() {
        super.onPause();
        mRef.child("users").child(mUserId).removeEventListener(mValueListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Store access token
        getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
                .edit()
                .putString(ACCESS_TOKEN, mAccessToken)
                .commit();

        // Clear pending dataToBeShared that hasn't been confirmed && make shareUnsharedFab gone
        UserSingleton.getInstance().getDataToShare().clear();
        mShareUnsharedFab.setVisibility(View.GONE);

        // Set User data on firebase
        mRef.child("users").child(mUserId).setValue(mUser);

        // Save shared photos to media and to tag nodes in firebase
        for (int i = 0; i < mUser.getShared().size(); i++) {
            InkDatum inkDatum = mUser.getShared().get(i);

            mRef.child("media")
                    .child(inkDatum.getId())
                    .setValue(inkDatum);

            for (int j = 0; j < inkDatum.getTags().size(); j++) {
                mRef.child("tags")
                        .child(inkDatum.getTags().get(j))
                        .child(inkDatum.getId())
                        .setValue(inkDatum);
            }
        }

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
    public void onProfileFragmentInteraction(int viewId) {
        switch (viewId) {
            default:
            case R.id.insta_login_button:
                instagramSignIn();
                break;
            case R.id.set_location_button:
                placesAutoComplete();
                break;
        }
    }

    // gets UserSingleton info from firebase
    public void getUserInfo() {

        mValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserSingleton.getInstance().setUser(dataSnapshot.getValue(InkUser.class));
                mUser = UserSingleton.getInstance().getUser();

                if (mUser == null) {
                    mUser = new InkUser();
                }
                if (mUser.getLocation() == null) {
                    mUser.setLocation(new Location());
                }
                if (mUser.getUser() == null) {
                    mUser.setUser(new User());
                }
                if (mUser.getUnshared() == null) {
                    mUser.setUnshared(new ArrayList<InkDatum>());
                }
                if (mUser.getShared() == null) {
                    mUser.setShared(new ArrayList<InkDatum>());
                }
                setLayout(mUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled: " + databaseError.getMessage(), databaseError.toException());
            }
        };

        mRef.child("users").child(mUserId).addValueEventListener(mValueListener);
    }

    public void setLayout(InkUser user) {

        if (!isDestroyed()) {

            mFragments.clear();

            int userFlag = 0;

            // get a unique user flag depending on user info
            if (user.getUser().getId() != null)
                userFlag += 3;
            if (user.getLocation().getId() != null)
                userFlag += 5;
            if (user.getProfile() != null && !user.getProfile().isEmpty())
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
            }

            // Check to see if profile is able to share media
            if (userFlag == 8 || userFlag == 15) { // is able
                // If there is nothing to share, don't add UnsharedFragment
                if (mUser.getUnshared().isEmpty()) {
                    mFragments.add(mSharedFragment = SharedFragment.newInstance());
                } else {
                    mFragments.add(mSharedFragment = SharedFragment.newInstance());
                    mFragments.add(mUnsharedFragment = UnsharedFragment.newInstance());
                }

            }
            // Check to see if the user has a set location
            if (userFlag == 5 || userFlag == 8 || userFlag == 12 || userFlag == 15) { // has location
                mLocationView.setVisibility(View.VISIBLE);
                mLocationText.setText(mUser.getLocation().getAddress());
            }
            mFragments.add(ProfileFragment.newInstance(userFlag));

            mPagerAdapter = new PortfolioPagerAdapter(getFragmentManager(), mFragments);
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            mTabLayout.setupWithViewPager(mViewPager, true);
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
                            mUser.getUnshared().add(instagramDatumToInked(datum));
                        }
                    }
                    mRef.child("users").child(mUserId).child("unshared")
                            .setValue(mUser.getUnshared());
                    Log.i(TAG, "onResponse: Instagram photos added to firebase");

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

    // Launch the places autocomplete widget so user can set location
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

        // Update all existing media if the new location is different from the last one or there was no last location
        if ((mUser.getLocation().getId() != null && !mUser.getLocation().getId().equals(place.getId())) ||
                mUser.getLocation().getId() == null) {
            for (int i = 0; i < mUser.getShared().size(); i++) {
                mUser.getShared().get(i).setLatitude(place.getLatLng().latitude);
                mUser.getShared().get(i).setLongitude(place.getLatLng().longitude);
                mRef.child("media").child(mUser.getShared().get(i).getId()).setValue(mUser.getShared().get(i));
            }
            for (int i = 0; i < mUser.getUnshared().size(); i++) {
                mUser.getUnshared().get(i).setLatitude(place.getLatLng().latitude);
                mUser.getUnshared().get(i).setLongitude(place.getLatLng().longitude);
            }
        }

        // Set user location
        mUser.getLocation().setAddress(place.getAddress().toString());
        mUser.getLocation().setId(place.getId());
        mUser.getLocation().setLatitude(place.getLatLng().latitude);
        mUser.getLocation().setLongitude(place.getLatLng().longitude);
        mUser.getLocation().setName(place.getName().toString());
        mRef.child("users").child(mUserId).setValue(mUser);
    }

    // Turn an instagram datum into an inked datum
    public InkDatum instagramDatumToInked(Datum datum) {
        InkDatum inkDatum = new InkDatum();
        inkDatum.setCaption(datum.getCaption().getText());
        inkDatum.setLink(datum.getLink());
        inkDatum.setUserId(mUserId);
        inkDatum.setImages(datum.getImages());
        inkDatum.setTags(datum.getTags());
        inkDatum.setId(datum.getId());
        inkDatum.setCreatedTime(-Long.parseLong(datum.getCreatedTime()));
        if (mUser.getLocation().getId() != null) {
            inkDatum.setLatitude(mUser.getLocation().getLatitude());
            inkDatum.setLongitude(mUser.getLocation().getLongitude());
        }
        return inkDatum;
    }

    @Override
    public void onUnsharedFragmentInteraction(InkDatum inkDatum) {

        // Make the share button visible if there's anything to share
        if (!UserSingleton.getInstance().getDataToShare().isEmpty()) {
            mShareUnsharedFab.setVisibility(View.VISIBLE);
        } else {
            mShareUnsharedFab.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSharedFragmentInteraction(InkDatum inkDatum) {
        Intent intent = new Intent(PortfolioActivity.this, EditActivity.class);
        intent.putExtra(EditActivity.INKED_DATUM, inkDatum);
        startActivity(intent);
    }
}
