package helper;

import java.util.List;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import finiexshiper.quoccuong.app.finiexshiper.NotifiActivity;
import finiexshiper.quoccuong.app.finiexshiper.R;
import finiexshiper.quoccuong.app.finiexshiper.WarningActivity;
import http.ServerConnector;
import model.AccessToken;
import model.Task;
import model.UserLocation;

public class TrackerService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    public static final String TAG = TrackerService.class.getName();

    private static final int UPDATE_TIME_INTERVAL = 2000;
    private static final int UPDATE_DISTANCE_INTERVAL = 5;
    public static final String UPDATE_TASK = "vn.finiex.shipperapp.UPDATE_TASK";
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 2000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private Location bestLocation = null;

    private LocationManager mLocationManager;

    private List<String> allProvider;

    private ServerConnector connector;

    CountDownTimer countDownTimer;

    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private int numberOrderInFirstTask = 0;
    private List<Task> mListTask;

    public TrackerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ShipperApplication.mService = this;
        connector = ServerConnector.getInstance();
        startRequestTask();
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ShipperApplication.mService = this;
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void startService() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            createLocationRequest();
        }
    }

    public void requestTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mListTask = connector.getAllTask();
                sendBroadcast(new Intent(UPDATE_TASK));
            }
        }).start();
    }

    private boolean isRing = false;

    public void startRequestTask() {
        requestTask();
        countDownTimer = new CountDownTimer(60 * 1000, 1 * 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        List<Task> listTask = connector.getAllTask();
                        if (listTask != null) {
                            sendBroadcast(new Intent(UPDATE_TASK));
                            System.out.println("***** TASK******");
                            try {
                                if (listTask.size() > 0 && (mListTask == null || mListTask.size() == 0)) {
                                    showNotifi();
                                } else if (listTask.size() > 0 && mListTask != null && mListTask.size() > 0) {
                                    if (listTask.get(0).get_LadingID() != mListTask.get(0).get_LadingID()) {
                                        showNotifi();
                                    } else if (listTask.get(0).getOrder() != null &&
                                            mListTask.get(0).getOrder() != null) {
                                        if (listTask.get(0).getOrder().size() > 0 &&
                                                listTask.get(0).getOrder().size() > mListTask.get(0).getOrder().size()) {
                                            showNotifi();
                                        } else if (listTask.get(0).getOrder() != null &&
                                                mListTask.get(0).getOrder() != null &&
                                                !listTask.get(0).get_Notes().equals(mListTask.get(0).get_Notes())) {
                                            showNotifi();
                                        } else if (listTask.get(0).getOrder() != null &&
                                                mListTask.get(0).getOrder() != null &&
                                                !listTask.get(0).get_DateUpdate().equals(mListTask.get(0).get_DateUpdate())) {
                                            showNotifi();
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            mListTask = listTask;
                            for (Task task : mListTask) {
                                System.out.println(task.toString());
                            }
                        } else
                            System.out.println("ERROR");
                        countDownTimer.start();
                    }
                }).start();
            }
        };
        countDownTimer.start();
    }

    private void showNotifi() {
        if (!NotifiActivity.active) {
            Intent intent = new Intent(TrackerService.this, NotifiActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public List<Task> getListTask() {
        return mListTask;
    }

    public void setListTask(List<Task> mListTask) {
        this.mListTask = mListTask;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null)
            countDownTimer.cancel();
        stopService();
        ShipperApplication.mService = null;
    }

    public void stopService() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleApiClient.disconnect();
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, provider + " is enabled");
            if (!allProvider.contains(provider)) {
                allProvider.add(provider);
            } else {
                Log.i(TAG, provider + " has already existed");
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, provider + " is disabled");
            for (String item : allProvider) {
                if (provider.equals(item)) {
                    allProvider.remove(item);
                    break;
                }
            }
            Log.i(TAG, "allProvider.size() = " + allProvider.size());
            if (allProvider.size() == 1) {
                Toast.makeText(getApplicationContext(), R.string.please_turn_on_gps, Toast.LENGTH_LONG).show();
                Intent warningIntent = new Intent(getApplicationContext(), WarningActivity.class);
                warningIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(warningIntent);
            }
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "Location change");
            if (bestLocation == null || isBetterLocation(location, bestLocation)) {
                bestLocation = location;
                Log.d(TAG, "*****Location change*****");
//				AccessToken accessToken = ((ShipperApplication)getApplicationContext()).getAccessToken();
//				connector.updateUserLocation(accessToken.getUserId(), accessToken.getAccessToken(), new UserLocation(location.getLatitude(), location.getLongitude()));
            }
        }
    };

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > UPDATE_INTERVAL_IN_MILLISECONDS;
        boolean isSignificantlyOlder = timeDelta < -UPDATE_INTERVAL_IN_MILLISECONDS;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use
        // the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be
            // worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        if (mLastLocation != null) {
            Log.d("Location", mLastLocation.toString());
        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
            Log.d("Location", getString(R.string.no_location_detected));
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(50f);
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null) return;
        bestLocation = location;
        Log.d("Location Changed", location.toString());
        AccessToken accessToken = ((ShipperApplication) getApplicationContext()).getAccessToken();
        connector.updateUserLocation(accessToken.getUserId(), accessToken.getAccessToken(), new UserLocation(location.getLatitude(), location.getLongitude()));
        if (location != null)
            Log.d("Location", location.toString());
        else {
            Log.d("Location", "NULL");

        }
    }

    public int getNumberOrderInFirstTask() {
        return numberOrderInFirstTask;
    }

    public void setNumberOrderInFirstTask(int numberOrderInFirstTask) {
        this.numberOrderInFirstTask = numberOrderInFirstTask;
    }

    public void checkNotifi() {
        if (mListTask != null && mListTask.size() > 0 && mListTask.get(0).getOrder() != null) {
            if (numberOrderInFirstTask > 0 && mListTask.get(0).getOrder().size() > numberOrderInFirstTask) {
                showNotifi();
            }
            numberOrderInFirstTask = mListTask.get(0).getOrder().size();
        }
    }
}
