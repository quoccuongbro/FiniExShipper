package http;

import java.util.List;
import java.util.Map;

import model.Order;
import model.Payment;
import model.StatusOrder;
import model.Task;
import model.UserInfo;
import model.UserLocation;
import models.ImageResponse;
import models.ImageUpload;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

interface ShiperServerAPI {

	@GET("user/{id}")
	Call<UserInfo> getUserInfo(@Path("id") String uId, @Query("access_token") String token);
	
    
	@GET("admin")
	Call<UserInfo> getAdminInfo(@Query("access_token") String token);
	
    @Headers("Content-Type:application/json;charset=utf-8")
    @PUT("user/{id}")
    Call<Object> updateLocation(@Path("id") String uId,
                                @Query("access_token") String token,
                                @Body UserLocation object);
    @Headers("Content-Type:application/json;charset=utf-8")
    @PUT("order/{id}")
    Call<Object> updateOrder(@Path("id") String order,
                             @Query("access_token") String token,
                             @Body StatusOrder object);

    @Headers("Content-Type:application/json;charset=utf-8")
    @POST("pmoney")
    Call<Object> updatePayment(@Query("access_token") String token,
                               @Body Payment object);

    @GET("task/{uid}")
    Call<List<Task>> getAllTask(@Path("uid") String uId,
                                @Query("access_token") String token);

    @GET("task/{uid}")
    Call<List<Task>> getAllTask(@Query("access_token") String token);

    @GET("order/{uid}")
    Call<List<Task>> getAllOrder(@Path("uid") String uId,
                                 @Query("access_token") String token);
    
    @GET("ordertask/{taskid}")
    Call<List<Order>> getOrderTask(@Path("taskid") String taskId,
                                   @Query("access_token") String token);
    
    @Multipart
    @POST("image")
    Call<Object> updatePicture(@Query("access_token") String token,
                               @Part("myFile") RequestBody file);
    
   
    
   
    @POST
    Call<ResponseBody> postObject(@Url String path,
                                  @QueryMap Map<String, String> options);
    @FormUrlEncoded
    @POST
    Call<ResponseBody> postObjectFormed(@Url String path,
                                        @QueryMap Map<String, String> options,
                                        @FieldMap Map<String, String> fields);
    @Multipart
    @POST
    Call<ResponseBody> postObjectMultipart(@Url String path,
                                           @QueryMap Map<String, String> options,
                                           @PartMap Map<String, String> parts);


    @Multipart
    @Streaming
    @POST
    Call<ResponseBody> postUpload(@Url String path,
                                  @QueryMap Map<String, String> options,
                                  @PartMap Map<String, String> parts,
                                  @PartMap Map<String, RequestBody> files);
    @Multipart
    @Streaming
    @POST
    Call<ResponseBody> postUpload(@Url String path,
                                  @QueryMap Map<String, String> options,
                                  @PartMap Map<String, RequestBody> files);

    @Headers("Content-Type:application/json")
    @PUT
    Call<ResponseBody> putObject(@Url String path,
                                 @QueryMap Map<String, String> options,
                                 @Body String object);
    @PUT
    Call<ResponseBody> putObject(@Url String path,
                                 @QueryMap Map<String, String> options);
    @FormUrlEncoded
    @PUT
    Call<ResponseBody> putObjectFormed(@Url String path,
                                       @QueryMap Map<String, String> options,
                                       @FieldMap Map<String, String> fields);
    @Multipart
    @PUT
    Call<ResponseBody> putObjectMultipart(@Url String path,
                                          @QueryMap Map<String, String> options,
                                          @PartMap Map<String, String> parts);


    @Multipart
    @Streaming
    @PUT
    Call<ResponseBody> putUpload(@Url String path,
                                 @QueryMap Map<String, String> options,
                                 @PartMap Map<String, String> parts,
                                 @PartMap Map<String, RequestBody> files);
    @Multipart
    @Streaming
    @PUT
    Call<ResponseBody> putUpload(@Url String path,
                                 @QueryMap Map<String, String> options,
                                 @PartMap Map<String, RequestBody> files);


    @DELETE
    Call<ResponseBody> deleteObject(@Url String path,
                                    @QueryMap Map<String, String> options);

    /****************************************
     * Upload
     * Image upload API
     */

    /**
     * @param auth        #Type of authorization for upload
     * @param file        image
     */
    @FormUrlEncoded
    @POST("/3/image")
    Call<ImageResponse> postImage(
            @Header("Authorization") String auth,
            @Field("image") String file
    );

    @Headers("Content-Type:application/json;charset=utf-8")
    @POST("images")
    Call<Object> uploadImageFini(@Query("access_token") String token,
                                 @Body ImageUpload object);
}
