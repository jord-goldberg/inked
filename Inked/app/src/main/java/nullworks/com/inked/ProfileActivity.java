package nullworks.com.inked;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import nullworks.com.inked.adapters.ProfilePagerAdapter;
import nullworks.com.inked.models.Datum;
import nullworks.com.inked.models.Media;
import nullworks.com.inked.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    public static final int RC_SIGN_IN = 200;

    private String mAccessToken;

    private ImageView mProfilePic;

    private ViewPager mViewPager;
    private ProfilePagerAdapter mPagerAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private User mInstaUser;
    private ArrayList<Datum> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAccessToken = getIntent().getStringExtra(MainActivity.ACCESS_TOKEN);
        mInstaUser = (User) getIntent().getSerializableExtra(MainActivity.INSTA_USER);

        mData = new ArrayList<>();

        mProfilePic = (ImageView) findViewById(R.id.profile_picture);
        mViewPager = (ViewPager) findViewById(R.id.profile_container);


        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            // already signed in
            Toast.makeText(ProfileActivity.this, "Already signed in", Toast.LENGTH_SHORT).show();
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .setProviders(AuthUI.GOOGLE_PROVIDER)
                            .build(),
                    RC_SIGN_IN);
        }
        //TODO: Don't forget to make sure user is able to sign out of google account

        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("users").child(mAuth.getCurrentUser().getUid()).setValue(mInstaUser);
    }

    @Override
    protected void onStart() {
        super.onStart();

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
                    mPagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager(), mData);
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

        Glide.with(ProfileActivity.this)
                .load(mInstaUser.getProfilePicture())
                .dontTransform()
                .into(mProfilePic);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logOut:
                break;
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: ");
                onNavigateUp();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
                Toast.makeText(ProfileActivity.this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else {
                // user is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message
                Toast.makeText(ProfileActivity.this, "Sign in Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
