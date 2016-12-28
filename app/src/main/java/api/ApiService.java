package api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by vinh on 7/3/16.
 */

public interface ApiService {
    String API_LOGIN = "api/login";
    String API_GET_USER_INFO = "api/user/{id}";
    String API_GET_LIST_ORDER = "api/order/{id}";

    @GET(API_LOGIN)
    Call<ResponseBody> login(@Query("phone") String phone, @Query("password") String password);

    @GET(API_GET_USER_INFO)
    Call<ResponseBody> getUserInfo(@Path("id") int id, @Query("access_token") String accessToken);

    @GET(API_GET_LIST_ORDER)
    Call<ResponseBody> getListOrder(@Path("id") int id, @Query("access_token") String accessToken);
}
