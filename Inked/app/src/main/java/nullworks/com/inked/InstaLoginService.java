package nullworks.com.inked;

import nullworks.com.inked.models.LocalUser;
import retrofit2.Call;;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by joshuagoldberg on 9/1/16.
 */
public interface InstaLoginService {
    @Multipart
    @POST("/access_token")
    Call<LocalUser> getUser(@Part("client_id") String clientID, @Part("client_secret")
            String clientSecret, @Part("redirect_uri") String redirectUri, @Part("code") String code,
                            @Part("grant_type") String authorizationCode);
}
