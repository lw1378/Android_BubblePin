package com.laiwang.locationService;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service {

	private static final String TAG = "LOCATION_SERVICE";

	private GPSLocationSensor mGPSLocationSensor;
	private NetworkLocationSensor mNetworkLocationSensor;

	private Handler handler = new Handler();
	private final int SCAN_INTERVAL = 1000 * 5;

	private long lastTimeStamp = 0;
	private static boolean serviceIsRunning = false;

	private static Location loc;

	@Override
	public void onCreate() {
		super.onCreate();
		mGPSLocationSensor = new GPSLocationSensor(this);
		mGPSLocationSensor.gps_Enable();
		mNetworkLocationSensor = new NetworkLocationSensor(this);
		mNetworkLocationSensor.networkLocation_Enable();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "Location update service starts.");
		serviceIsRunning = true;
		handler.postDelayed(getLocationRunnable, 0);
	};

	@Override
	public void onDestroy() {
		Log.i(TAG, "Location update service destorys.");
		serviceIsRunning = false;
		mGPSLocationSensor.gps_Disable();
		mNetworkLocationSensor.networkLocation_Disable();
	};

	/**
	 * public API to check whether now service is running
	 * 
	 * @return true if service is running, false if not
	 */
	public static boolean isRunning() {
		return serviceIsRunning;
	}

	/**
	 * public API to return current updated location
	 * 
	 * @return current Location
	 */
	public static Location getCurrentLocation() {
		return loc;
	}

	/**
	 * a new thread used to update current location information first try to use
	 * gps to get current position, if gps is currently unavailable, then we'll
	 * use network instead
	 */
	Runnable getLocationRunnable = new Runnable() {

		@Override
		public void run() {
			if (mGPSLocationSensor.isAvailable()
					&& mGPSLocationSensor.getTimestamp() != lastTimeStamp) {
				loc = mGPSLocationSensor.getLocation();
				double latitude = mGPSLocationSensor.getLatitude();
				double longitude = mGPSLocationSensor.getLongitude();
				long timeStamp = mGPSLocationSensor.getTimestamp();
				lastTimeStamp = timeStamp;
				Log.i(TAG, "Update Location by GPS: latitude: " + latitude
						+ ", longitude: " + longitude);
			} else if (mNetworkLocationSensor.isAvailable()
					&& mNetworkLocationSensor.getTimestamp() != lastTimeStamp) {
				// GPS seems doesn't work, let's try our network location
				loc = mNetworkLocationSensor.getLocation();
				double latitude = mNetworkLocationSensor.getLatitude();
				double longitude = mNetworkLocationSensor.getLongitude();
				long timeStamp = mNetworkLocationSensor.getTimestamp();
				lastTimeStamp = timeStamp;
				Log.i(TAG, "update Location by Network: latitude: " + latitude
						+ ", longitude: " + longitude);
			} else {
				Log.i(TAG,
						"Location is not changed or no GPS or network are available.");
			}

			if (serviceIsRunning)
				handler.postDelayed(this, SCAN_INTERVAL);
		}

	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
