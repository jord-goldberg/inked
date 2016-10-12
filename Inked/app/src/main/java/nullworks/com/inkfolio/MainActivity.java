package nullworks.com.inkfolio;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

import nullworks.com.inkfolio.adapters.MainPagerAdapter;
import nullworks.com.inkfolio.interfaces.QueryFragmentListener;
import nullworks.com.inkfolio.models.custom.InkDatum;
import nullworks.com.inkfolio.transformers.DepthPageTransformer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, QueryFragmentListener{

    private static final String TAG = "MainActivity";

    public static final int GOOGLE_SIGN_IN = 200;
    public static final int DETAIL_REQUEST = 110;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private GoogleApiClient mGoogleApiClient;

    private NavigationView mNavigationView;
    private Menu mCategorySubMenu;
    private ImageView mProfilePic;
    private TextView mDisplayName;
    private TextView mEmailAddress;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MainPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        buildGoogleApiClient();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        View headerView = mNavigationView.inflateHeaderView(R.layout.nav_header_main);
        mProfilePic = (ImageView) headerView.findViewById(R.id.nav_imageView);
        mDisplayName = (TextView) headerView.findViewById(R.id.nav_fullName);
        mEmailAddress = (TextView) headerView.findViewById(R.id.nav_userName);

        mCategorySubMenu = mNavigationView.getMenu().getItem(2).getSubMenu();
        mCategorySubMenu.getItem(0).setChecked(true);

        mViewPager = (ViewPager) findViewById(R.id.main_container);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mPagerAdapter = new MainPagerAdapter(getFragmentManager(), mCategorySubMenu);

        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager, true);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setOffscreenPageLimit(4);

        // Add a listener to check Navigation Drawer items as we scroll through them
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            MenuItem currentItem = mCategorySubMenu.getItem(mViewPager.getCurrentItem());
            @Override
            public void onPageSelected(int position) {
                currentItem.setChecked(false);
                currentItem = mCategorySubMenu.getItem(position);
                currentItem.setChecked(true);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkForAuthorizedUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
                startActivity(new Intent(this, PortfolioActivity.class));
            } else {
                // user is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message
                Toast.makeText(mViewPager.getContext(), "Sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_login:
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                                .setProviders(AuthUI.GOOGLE_PROVIDER)
                                .setTheme(R.style.AppTheme_AppBarOverlay)
                                .build(),
                        GOOGLE_SIGN_IN);
                break;
            case R.id.nav_portfolio:
                Intent intent = new Intent(MainActivity.this, PortfolioActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_home:
                mViewPager.setCurrentItem(0, true);
                break;
            case R.id.nav_trad_amer:
                mViewPager.setCurrentItem(1, true);
                break;
            case R.id.nav_trad_jap:
                mViewPager.setCurrentItem(2, true);
                break;
            case R.id.nav_surreal:
                mViewPager.setCurrentItem(3, true);
                break;
            case R.id.nav_tribal:
                mViewPager.setCurrentItem(4, true);
                break;
            case R.id.nav_geometric:
                mViewPager.setCurrentItem(5, true);
                break;
            case R.id.nav_watercolor:
                mViewPager.setCurrentItem(6, true);
                break;
            case R.id.nav_realistic:
                mViewPager.setCurrentItem(7, true);
                break;
            case R.id.nav_lettering:
                mViewPager.setCurrentItem(8, true);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private synchronized void buildGoogleApiClient() {
        PlacesHelper helper = new PlacesHelper(mGoogleApiClient);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(helper)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, helper)
                .build();
    }

    public void checkForAuthorizedUser() {
        if (mAuth.getCurrentUser() != null) { // User is signed in
            // Show portfolio option in Nav Drawer
            mNavigationView.getMenu().getItem(0).setVisible(false);
            mNavigationView.getMenu().getItem(1).setVisible(true);
            // Make Nav header views visible
            mProfilePic.setVisibility(View.VISIBLE);
            mDisplayName.setVisibility(View.VISIBLE);
            mEmailAddress.setVisibility(View.VISIBLE);
            // Display user data from mAuth
            Glide.with(this)
                    .load(mAuth.getCurrentUser().getPhotoUrl())
                    .dontTransform()
                    .into(mProfilePic);
            mDisplayName.setText(mAuth.getCurrentUser().getDisplayName());
            mEmailAddress.setText(mAuth.getCurrentUser().getEmail());

        } else { // User is not signed in
            // Show sign in option in Nav Drawer
            mNavigationView.getMenu().getItem(0).setVisible(true);
            mNavigationView.getMenu().getItem(1).setVisible(false);
            // Make Nav header views gone
            mProfilePic.setVisibility(View.GONE);
            mDisplayName.setVisibility(View.GONE);
            mEmailAddress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onQueryFragmentInteration() {

    }
}
