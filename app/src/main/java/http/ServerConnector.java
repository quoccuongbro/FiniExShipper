package http;

import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;

import model.AccessToken;
import model.Order;
import model.Payment;
import model.StatusOrder;
import model.Task;
import model.UserInfo;
import model.UserLocation;
import models.ImageResponse;
import models.ImageUpload;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerConnector {

    private final String BASE_URL = "http://api.finiex.vn/api/";
    private final String BASE_IMGUR_URL = "https://api.imgur.com/";
    private Retrofit retrofit;
    private ShiperServerAPI service;

    private AccessToken accessToken;

    private static ServerConnector instance;
    private static ServerConnector instanceImgur;

    public synchronized static ServerConnector getInstance() {
        if (instance == null)
            instance = new ServerConnector(false);
        return instance;
    }

    public synchronized static ServerConnector getInstance(boolean isImgur) {
        if (!isImgur) return getInstance();
        else if (instanceImgur == null)
            instanceImgur = new ServerConnector(true);
        return instanceImgur;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    private ServerConnector(boolean isImgur) {

        OkHttpClient httpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)).build();
        if (isImgur) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_IMGUR_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        service = retrofit.create(ShiperServerAPI.class);
    }

    public void getUserInfo(String userId, String token, Callback<UserInfo> listener) {
        Call<UserInfo> call = service.getUserInfo(userId, token);
        call.enqueue(listener);

    }

    public void getAdminInfo(Callback<UserInfo> listener) {
        Call<UserInfo> call = service.getAdminInfo(accessToken.getAccessToken());
        call.enqueue(listener);

    }

    public void updateUserLocation(String uId, String accessToken, UserLocation data) {
        Call<Object> call = service.updateLocation(uId, accessToken, data);
        call.enqueue(new Callback<Object>() {

            @Override
            public void onFailure(Call<Object> arg0, Throwable arg1) {
                arg1.printStackTrace();

            }

            @Override
            public void onResponse(Call<Object> arg0, Response<Object> arg1) {
                Log.d("UPDATE_LOCATION", arg1.toString());
            }
        });
    }

    public Object updateOrder(String orderId, StatusOrder data) {
        Call<Object> call = service.updateOrder(orderId, accessToken.getAccessToken(), data);
        try {
            Response<Object> ro = call.execute();
            return ro.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object payment(Payment data) {
        Call<Object> call = service.updatePayment(accessToken.getAccessToken(), data);
        try {
            Response<Object> ro = call.execute();
            return ro.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Task> getAllTask() {
        if (accessToken == null) return null;
        try {
            Response<List<Task>> taskList = service.getAllTask(accessToken.getUserId(), accessToken.getAccessToken()).execute();
            List<Task> lt = taskList.body();
            if (lt == null) return null;
            Collections.reverse(lt);
            for (Task task : lt) {
                try {
                    task.getOrderOnline();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return lt;
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
    }

    public List<Order> getOrderOfTask(int id) {
        if (accessToken == null) return null;
        try {
            Response<List<Order>> ordersOfTask = service.getOrderTask(id + "", accessToken.getAccessToken()).execute();
            return ordersOfTask.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void uploadImage(final String path, final Callback<ImageResponse> cbIR) {
        Call<ImageResponse> ci = null;
        try {
            String image64 = encodeFileToBase64Binary(path);
            if(!TextUtils.isEmpty(image64)) {
                ci = service.postImage("Client-ID f9ef7568e231ee5", image64);
                ci.enqueue(cbIR);
            }else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Call<ImageResponse> ci = null;
                        String image64 = null;
                        try {
                            image64 = encodeFileToBase64Binary(path);
                            if(!TextUtils.isEmpty(image64)) {
                                ci = service.postImage("Client-ID f9ef7568e231ee5", image64);
                                ci.enqueue(cbIR);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }, 10000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Object uploadImageFini(ImageUpload iu, Callback<Object> co) {
        Call<Object> ro = service.uploadImageFini(accessToken.getAccessToken(), iu);
        ro.enqueue(co);
        return null;
    }

    private String encodeFileToBase64Binary(String fileName)
            throws IOException {

        File file = new File(fileName);
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        byte[] bytes = loadFile(file);
        String encodedString = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        return encodedString;
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }


}
