package nullworks.com.inked;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by joshuagoldberg on 9/1/16.
 */
public class LoginDialogFragment extends DialogFragment {

    private static final String TAG = "LoginDialogFragment";

    private static final String BASE_URL = "https://api.instagram.com/oauth/";

    private String YOUR_AUTHORIZATION_URL;

    private WebView mWebView;

    private AccessTokenReceived mAccessTokenReceived;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YOUR_AUTHORIZATION_URL =
                new StringBuilder("https://api.instagram.com/oauth/authorize/?")
                        .append("client_id=").append(InstaAppData.CLIENT_ID)
                        .append("&redirect_uri=" ).append(InstaAppData.CALLBACK_URL).append("&response_type=code")
                        .toString();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.dialog_login, container, false);
        mWebView = (WebView) viewRoot.findViewById(R.id.login_webview);
        mAccessTokenReceived = (AccessTokenReceived) getContext();
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("code=")){ //CHECKING TO SEE IF THE URL WE HAVE IS THE ONE WE WANT
                    Log.i(TAG, "shouldOverrideUrlLoading: " + url);

                    int index = url.indexOf("=");
                    Log.i(TAG, url.substring(index+1));

                    //STRIPPING AWAY THE URL AND ONLY KEEPING THE "CODE"
                    String code = url.substring(index+1);
                    getAccessToken(code);
                    return true;
                }
                else {
                    return false;
                }
            }
        });
        mWebView.loadUrl(YOUR_AUTHORIZATION_URL);
    }

    private void getAccessToken(String code){

        //WE'LL WORK ON THIS TOGETHER
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("client_id", InstaAppData.CLIENT_ID)
                .add("client_secret", InstaAppData.CLIENT_SECRET)
                .add("redirect_uri", InstaAppData.CALLBACK_URL)
                .add("code", code)
                .add("grant_type", "authorization_code")
                .build();

        Request request = new Request.Builder()
                .url("https://api.instagram.com/oauth/access_token")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: request failed", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    throw new IOException("Unexpected code " + response);
                }

                String responseString = response.body().string();
                Log.i(TAG, "onResponse: " + responseString);

                try {
                    JSONObject result = new JSONObject(responseString);
                    String accessToken = result.getString("access_token");

                    JSONObject user = result.getJSONObject("user");
                    String userName = user.getString("username");
                    String profilePicUrl = user.getString("profile_picture");
                    String fullName = user.getString("full_name");
                    String userId = user.getString("id");


                    Log.i(TAG, "onResponse: access token - " + accessToken);

                    mAccessTokenReceived.onAccessTokenReceived(accessToken, userName, profilePicUrl,
                            fullName, userId);
                    dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    interface AccessTokenReceived {
        void onAccessTokenReceived(String accessToken, String userName, String profilePicUrl,
                                   String fullName, String userId);
    }
}
