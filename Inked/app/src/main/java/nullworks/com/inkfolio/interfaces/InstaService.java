package nullworks.com.inkfolio.interfaces;

import nullworks.com.inkfolio.models.AccessToken;
import nullworks.com.inkfolio.models.Envelope;
import retrofit2.Call;;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @GET("/v1/users/self/media/recent/")
    Call<Envelope> getRecentMedia(@Query("access_token") String accessToken);

    @GET("/v1/users/self/media/recent/")
    Call<Envelope> getMedia(@Query("access_token") String accessToken,
                                @Query("max_id") String maxId);

}
