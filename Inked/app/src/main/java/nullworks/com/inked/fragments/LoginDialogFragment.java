package nullworks.com.inked.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import nullworks.com.inked.interfaces.InstaService;
import nullworks.com.inked.R;
import nullworks.com.inked.models.AccessToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by joshuagoldberg on 9/1/16.
 */
public class LoginDialogFragment extends DialogFragment {

    private static final String TAG = "LoginDialogFragment";

    private String mClientId;
    private String mClientSecret;
    private String mRedirectUri;
    private String mAuthorizationUrl;

    private WebView mWebView;

    private AccessTokenReceived mAccessTokenReceived;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccessTokenReceived = (AccessTokenReceived) getContext();

        mClientId = getString(R.string.insta_client_id);
        mClientSecret = getString(R.string.insta_client_secret);
        mRedirectUri = getString(R.string.insta_redirect_uri);

        mAuthorizationUrl = new StringBuilder("https://api.instagram.com/oauth/authorize/")
                .append("?client_id=").append(mClientId)
                .append("&redirect_uri=").append(mRedirectUri)
                .append("&response_type=code")
                .toString();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_dialog_login, container, false);
        mWebView = (WebView) viewRoot.findViewById(R.id.login_webview);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("code=")) { //CHECKING TO SEE IF THE URL WE HAVE IS THE ONE WE WANT

                    int index = url.indexOf("=");

                    //STRIPPING AWAY THE URL AND ONLY KEEPING THE "CODE"
                    String code = url.substring(index + 1);
                    getAccessToken(code);
                    return true;
                } else {
                    return false;
                }
            }
        });
        mWebView.loadUrl(mAuthorizationUrl);
    }

    private void getAccessToken(String code) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InstaService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InstaService service = retrofit.create(InstaService.class);

        Call<AccessToken> call = service.getAccessToken(
                mClientId,
                mClientSecret,
                mRedirectUri,
                "authorization_code",
                code);

        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                Log.d(TAG, "onResponse: " + response.body().getAccessToken());
                try {
                    mAccessTokenReceived.onAccessTokenReceived(response.body());
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.e(TAG, "onFailure: request failed", t);
            }
        });
    }

    public interface AccessTokenReceived {
        void onAccessTokenReceived(AccessToken accessToken);
    }
}
