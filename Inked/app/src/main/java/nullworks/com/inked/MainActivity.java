package nullworks.com.inked;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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

import java.util.ArrayList;

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
    private ImageView mNavImageView;
    private TextView mNavTextView;

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private MainRecyclerAdaper mAdapter;

    private ArrayList<String> strings;

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

        strings = new ArrayList<>();
        strings.add("http://i.imgur.com/rFI2n7F.jpg");
        strings.add("https://s-media-cache-ak0.pinimg.com/736x/3c/ba/a4/3cbaa48313203a3d1f894cbe33bc6191.jpg");
        strings.add("http://media.tumblr.com/tumblr_m344o1hCLA1qm4rc3.jpg");
        strings.add("https://s-media-cache-ak0.pinimg.com/564x/2f/d4/88/2fd4887b9c2e667acc349a35fb3a622c.jpg");
        strings.add("https://www-media.fanlala.com/sites/default/files/content/03/25/14/justin-bieber-instagram-tattoo-2.png");
        strings.add("http://www.galleryoftattoosnow.com/images/ckuploads/images/424658_3926264429080_520095477_n.jpg");
        strings.add("http://i.imgur.com/rFI2n7F.jpg");
        strings.add("https://s-media-cache-ak0.pinimg.com/736x/3c/ba/a4/3cbaa48313203a3d1f894cbe33bc6191.jpg");
        strings.add("http://media.tumblr.com/tumblr_m344o1hCLA1qm4rc3.jpg");
        strings.add("https://s-media-cache-ak0.pinimg.com/564x/2f/d4/88/2fd4887b9c2e667acc349a35fb3a622c.jpg");
        strings.add("https://www-media.fanlala.com/sites/default/files/content/03/25/14/justin-bieber-instagram-tattoo-2.png");
        strings.add("http://www.galleryoftattoosnow.com/images/ckuploads/images/424658_3926264429080_520095477_n.jpg");
        strings.add("http://i.imgur.com/rFI2n7F.jpg");
        strings.add("https://s-media-cache-ak0.pinimg.com/736x/3c/ba/a4/3cbaa48313203a3d1f894cbe33bc6191.jpg");
        strings.add("http://media.tumblr.com/tumblr_m344o1hCLA1qm4rc3.jpg");
        strings.add("https://s-media-cache-ak0.pinimg.com/564x/2f/d4/88/2fd4887b9c2e667acc349a35fb3a622c.jpg");
        strings.add("https://www-media.fanlala.com/sites/default/files/content/03/25/14/justin-bieber-instagram-tattoo-2.png");
        strings.add("http://www.galleryoftattoosnow.com/images/ckuploads/images/424658_3926264429080_520095477_n.jpg");
        strings.add("http://i.imgur.com/rFI2n7F.jpg");
        strings.add("https://s-media-cache-ak0.pinimg.com/736x/3c/ba/a4/3cbaa48313203a3d1f894cbe33bc6191.jpg");
        strings.add("http://media.tumblr.com/tumblr_m344o1hCLA1qm4rc3.jpg");
        strings.add("https://s-media-cache-ak0.pinimg.com/564x/2f/d4/88/2fd4887b9c2e667acc349a35fb3a622c.jpg");
        strings.add("https://www-media.fanlala.com/sites/default/files/content/03/25/14/justin-bieber-instagram-tattoo-2.png");
        strings.add("http://www.galleryoftattoosnow.com/images/ckuploads/images/424658_3926264429080_520095477_n.jpg");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new MainRecyclerAdaper(strings);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mLayoutManager.setSpanCount(3);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
        }

        if (getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(ACCESS_TOKEN, null) != null) {
            Menu menu = mNavigationView.getMenu();
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);

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

                break;
            case R.id.nav_gallery:

                break;
        }

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Runs after a user logs in. Stores the access token and changes the Nav Menu items
    @Override
    public void onAccessTokenReceived(String accessToken, String userName, final String profilePicUrl,
                                      String fullName, String userId) {
        getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
                .edit()
                .putString(ACCESS_TOKEN, accessToken)
                .putString(USER_NAME, userName)
                .putString(PROFILE_PIC_URL, profilePicUrl)
                .putString(FULL_NAME, fullName)
                .putString(USER_ID, userId)
                .commit();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Menu menu = mNavigationView.getMenu();
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(true);
                menu.getItem(2).setVisible(true);

                Glide.with(MainActivity.this)
                        .load(profilePicUrl)
                        .fitCenter()
                        .into(mNavImageView);
            }
        });
    }
}
