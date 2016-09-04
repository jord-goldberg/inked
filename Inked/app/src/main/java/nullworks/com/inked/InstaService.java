package nullworks.com.inked;

import nullworks.com.inked.models.AccessToken;
import retrofit2.Call;;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by joshuagoldberg on 9/1/16.
 */
public interface InstaService {

    public static final String BASE_URL = "https://api.instagram.com";

    @FormUrlEncoded @POST("/oauth/access_token")
    Call<AccessToken> getAccessToken(@Field("client_id") String client_id,
                                     @Field("client_secret") String client_secret,
                                     @Field("redirect_uri") String redirect_uri,
                                     @Field("grant_type") String grant_type,
                                     @Field("code") String code);

}
