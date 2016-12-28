package helper;

import com.google.gson.Gson;

import android.R.bool;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import finiexshiper.quoccuong.app.finiexshiper.MainActivity;
import http.ServerConnector;
import model.AccessToken;
import model.UserInfo;

public class ShipperApplication extends Application {
	
	private final String PREFERENCE_NAME = "Prefer";
	private final String KEY_LOGIN = "isLogin";
	private final String KEY_PASS = "pass";
	private final String KEY_USERNAME = "name";
	private final String KEY_ACCESS_TOKEM = "Accesstoken";
	public static final String KEY_SAVED_GCM = "gcmSaved";

	public static TrackerService mService;
	private AccessToken accessToken;
//	private String uId;
	SharedPreferences preferences;

	static ShipperApplication instance;
	private UserInfo mUserInfo;

	public static ShipperApplication get(){
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		MultiDex.install(this);
		preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
		ServerConnector connector = ServerConnector.getInstance();
		if(isLogined()){
			String data = preferences.getString(KEY_ACCESS_TOKEM, null);
			if(data!=null){
				accessToken = new Gson().fromJson(data, AccessToken.class);
				connector.setAccessToken(accessToken);
			}
		}
	}

	public void login(String userName, String pass, AccessToken _accessToken){
		this.accessToken = _accessToken;
		Intent intentMain = new Intent(this, MainActivity.class);
		intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intentMain);

		SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
		Editor editor = preferences.edit();
		
		editor.putBoolean(KEY_LOGIN, true);
		editor.putString(KEY_USERNAME, userName);
		editor.putString(KEY_PASS, pass);
		editor.putString(KEY_ACCESS_TOKEM, new Gson().toJson(accessToken));
		editor.commit();
		ServerConnector.getInstance().setAccessToken(accessToken);
	}
	public void logout(){
		stopService(new Intent(this, TrackerService.class));
		Editor editor = preferences.edit();
		editor.putBoolean(KEY_LOGIN, false);
		editor.commit();
	}
	public boolean isLogined(){
		return preferences.getBoolean(KEY_LOGIN, false);
	}
	public AccessToken getAccessToken() {
		return accessToken;
	}

	public UserInfo getmUserInfo() {
		return mUserInfo;
	}

	public void setmUserInfo(UserInfo mUserInfo) {
		this.mUserInfo = mUserInfo;
	}
}
