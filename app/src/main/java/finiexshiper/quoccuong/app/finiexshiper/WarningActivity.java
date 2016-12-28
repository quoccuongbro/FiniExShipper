package finiexshiper.quoccuong.app.finiexshiper;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import helper.ShipperApplication;

public class WarningActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_warning);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(mToolbar);
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if(canGetLocation())
			finish();
		
	}
	
	public void turnOnLocation(View v){
		Intent settingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		settingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(settingIntent);
	}
	
	public void logOut(View v){
		((ShipperApplication)getApplication()).logout();
		
		Intent mStartActivity = new Intent(this, MainActivity.class);
		int mPendingIntentId = 123456;
		PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
		System.exit(0);
		
	}
	
		
	public boolean canGetLocation() {
	    boolean result = true;
	    LocationManager lm = null;
	    boolean gps_enabled = false;
	    boolean network_enabled = false;
	    if (lm == null)

	        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

	    // exceptions will be thrown if provider is not permitted.
	    try {
	        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    } catch (Exception ex) {

	    }
	    try {
	        network_enabled = lm
	                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	    } catch (Exception ex) {
	    }
	    if (gps_enabled == false || network_enabled == false) {
	        result = false;
	    } else {
	        result = true;
	    }

	    return result;
	}
	
	
}
