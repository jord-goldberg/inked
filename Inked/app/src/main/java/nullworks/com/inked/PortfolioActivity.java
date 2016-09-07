package nullworks.com.inked;

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
import nullworks.com.inked.fragments.LoginDialogFragment;
import nullworks.com.inked.models.AccessToken;
import nullworks.com.inked.models.Datum;
import nullworks.com.inked.models.Media;
import nullworks.com.inked.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PortfolioActivity extends AppCompatActivity
        implements LoginDialogFragment.AccessTokenReceived,
        InstaRecyclerAdapter.NewMediaClicked,
        View.OnClickListener {

    private static final String TAG = "PortfolioActivity";

    public static final String SHARED_PREFS = "nullworks.com.inked";
    public static final String ACCESS_TOKEN = "access_token";

    private String mAccessToken;
    private String mClientId;
    private String mClientSecret;
    private String mRedirectUri;

    private ImageView mProfilePic;
    private TextView mFullNameView;
    private CardView mInstaLoginView;
    private CardView mLocationView;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private PortfolioPagerAdapter mPagerAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private String mUserId;
    private User mUser;
    private String mProfile;

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
        mInstaLoginView = (CardView) findViewById(R.id.insta_login_card);
        mFullNameView = (TextView) findViewById(R.id.fullname_textview);
        mLocationView = (CardView) findViewById(R.id.user_location_card);
        mViewPager = (ViewPager) findViewById(R.id.profile_container);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        mRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUserId = mAuth.getCurrentUser().getUid();

        mAccessToken = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(ACCESS_TOKEN, null);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mTabLayout.setupWithViewPager(mViewPager, true);
        getUserInfo(mUserId);

        // Check to see if user has Instagram access token
        if (mAccessToken == null) { // User must log in to get token; adjust views
            mLocationView.setVisibility(View.GONE);
            mInstaLoginView.setOnClickListener(this);
            getInstagramData(mRef.child("instagramData"));
        } else { // User is already logged into instagram; remove login view
            mInstaLoginView.setVisibility(View.GONE);
        }
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
                        mPagerAdapter = new PortfolioPagerAdapter(getSupportFragmentManager(), mData);
                        mViewPager.setAdapter(mPagerAdapter);
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
    protected void onStop() {
        super.onStop();
        // Store access token
        getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
                .edit()
                .putString(ACCESS_TOKEN, mAccessToken)
                .commit();
        // Check to see if the user is signed in; this may be after a sign-out
        if (mAuth.getCurrentUser() != null  && mUser != null) {
            // TODO: Make this happen after the user agrees to save changes
            mRef.child("users").child(mUserId).child("user").setValue(mUser);
            mRef.child("users").child(mUserId).child("profile").setValue(mProfile);
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

    // Runs after a user logs into Instagram
    @Override
    public void onAccessTokenReceived(AccessToken accessToken) {
        mAccessToken = accessToken.getAccessToken();
        if (mUser == null)
            mUser = accessToken.getUser();
        else
            //TODO: Update firebase data without overwriting blank fields - check database rules
        ;
    }

    @Override
    public void onNewMediaClicked(Datum datum) {
        if (!mNewMedia.contains(datum))
            mNewMedia.add(datum);
        else
            mNewMedia.remove(datum);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.insta_login_card:
                DialogFragment dialogFragment = LoginDialogFragment
                        .newInstance(mClientId, mClientSecret, mRedirectUri);
                dialogFragment.show(getSupportFragmentManager(), "dialog");
                //TODO: Add ability to log out of Instagram with warning that all pictures will be unshared.
                break;
        }
    }

    public void getInstagramData(DatabaseReference ref) {
        // Set one time listener to get the Instagram data for the login dialog
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mClientId = dataSnapshot.child("clientId").getValue(String.class);
                mClientSecret = dataSnapshot.child("clientSecret").getValue(String.class);
                mRedirectUri = dataSnapshot.child("redirectUri").getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled: " + databaseError.getMessage(), databaseError.toException());
            }
        });
    }

    public void getUserInfo(String userId) {
        // Set listener to get user info from firebase
        mRef.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.child("user").getValue(User.class);
                mProfile = dataSnapshot.child("profile").getValue(String.class);
                if (mUser != null) { // User has already stored info on our database; display it
                    mFullNameView.setText(mUser.getFullName());
                    Glide.with(PortfolioActivity.this)
                            .load(mUser.getProfilePicture().replace("s150x150", ""))
                            .fitCenter()
                            .into(mProfilePic);
                } else { // New user; do some other stuff
                    mFullNameView.setText(mAuth.getCurrentUser().getDisplayName());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled: " + databaseError.getMessage(), databaseError.toException());
            }
        });
    }
}
