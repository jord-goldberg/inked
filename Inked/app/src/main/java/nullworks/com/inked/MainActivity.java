package nullworks.com.inked;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import nullworks.com.inked.adapters.MainPagerAdapter;
import nullworks.com.inked.fragments.LoginDialogFragment;
import nullworks.com.inked.models.AccessToken;
import nullworks.com.inked.models.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoginDialogFragment.AccessTokenReceived {

    private static final String TAG = "MainActivity";

    public static final String SHARED_PREFS = "nullworks.com.inked";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String USER_NAME = "username";
    public static final String PROFILE_PIC_URL = "profile_picture";
    public static final String FULL_NAME = "full_name";
    public static final String USER_ID = "id";

    private NavigationView mNavigationView;
    private Menu mCategorySubMenu;
    private ImageView mNavImageView;
    private TextView mNavFullNameTV;
    private TextView mNavUserNameTV;

    private ViewPager mViewPager;
    private MainPagerAdapter mPagerAdapter;

    private String mAccessToken;
    private User mInstaUser;

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

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        View headerView = mNavigationView.inflateHeaderView(R.layout.nav_header_main);
        mNavImageView = (ImageView) headerView.findViewById(R.id.nav_imageView);
        mNavFullNameTV = (TextView) headerView.findViewById(R.id.nav_fullName);
        mNavUserNameTV = (TextView) headerView.findViewById(R.id.nav_userName);

        mCategorySubMenu = mNavigationView.getMenu().getItem(3).getSubMenu();
        mCategorySubMenu.getItem(0).setChecked(true);

        mViewPager = (ViewPager) findViewById(R.id.main_container);
        mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), mCategorySubMenu);
        mViewPager.setAdapter(mPagerAdapter);
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
    protected void onResume() {
        super.onResume();
//        if (getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(ACCESS_TOKEN, null) != null) {
//            Menu menu = mNavigationView.getMenu();
//            menu.getItem(0).setVisible(false);
//            menu.getItem(1).setVisible(true);
//            menu.getItem(2).setVisible(true);
//
//        }
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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
                DialogFragment dialogFragment = new LoginDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "dialog");
                break;
            case R.id.nav_account:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra(ACCESS_TOKEN, mAccessToken);
                startActivity(intent);
                break;
            case R.id.nav_gallery:
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

    // Runs after a user logs in. Stores the access token and changes the Nav Menu items
    @Override
    public void onAccessTokenReceived(AccessToken accessToken) {

        mAccessToken = accessToken.getAccessToken();
        mInstaUser = accessToken.getUser();

        getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
                .edit()
                .putString(ACCESS_TOKEN, mAccessToken)
                .putString(USER_ID, mInstaUser.getId())
                .commit();

//        getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
//                .edit()
//                .putString(ACCESS_TOKEN, accessToken)
//                .putString(USER_NAME, userName)
//                .putString(PROFILE_PIC_URL, profilePicUrl)
//                .putString(FULL_NAME, fullName)
//                .putString(USER_ID, userId)
//                .commit();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Menu menu = mNavigationView.getMenu();
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(true);
                menu.getItem(2).setVisible(true);

                Glide.with(MainActivity.this)
                        .load(mInstaUser.getProfilePicture())
                        .fitCenter()
                        .into(mNavImageView);
                mNavFullNameTV.setText(mInstaUser.getFullName());
                mNavUserNameTV.setText(mInstaUser.getUsername());
            }
        });
    }
}
