package nullworks.com.inked;

import android.app.Activity;
import android.os.Bundle;

public class ProfileActivity extends Activity {

    private String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAccessToken = getIntent().getStringExtra(MainActivity.ACCESS_TOKEN);
    }
}
