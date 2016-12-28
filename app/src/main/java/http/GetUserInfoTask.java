package http;

import com.google.gson.Gson;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import helper.ShipperApplication;
import model.AccessToken;
import model.UserInfo;


public class GetUserInfoTask extends AsyncTask<AccessToken, Void, String>{

	private Context context;
	
	public GetUserInfoTask(Context ctx) {
		context = ctx;
	}
	
	@Override
	protected String doInBackground(AccessToken... params) {
		
		String uId = params[0].getUserId();
		String accessToken  = params[0].getAccessToken();
		
//		ServerConnector.getInstance().getUserInfo(uId, accessToken);		
		return HTTPUtils.GET(HTTPUtils.URL_USER_INFO + uId+"?access_token="+accessToken );
	}

	@Override
	protected void onPostExecute(String result) {
		
		super.onPostExecute(result);
		Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
		UserInfo userInfo = new Gson().fromJson(result, UserInfo.class);
		ShipperApplication.get().setmUserInfo(userInfo);
	//	adapter.setUserInfo(userInfo);
	}
}
