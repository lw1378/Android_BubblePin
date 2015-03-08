package com.laiwang.applicationInitialize;

import com.example.android_login_googlemap.R;
import com.laiwang.locationService.LocationService;
import com.parse.Parse;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class InitializeApplication extends Application {

	private static final String TAG = "APPLCATION_INITIALIZE";
	
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, getString(R.string.parse_applicationId), getString(R.string.parse_clientId));
		if (!LocationService.isRunning()) {
			Log.i(TAG, "Location service is now enabled.");
			startService(new Intent(this, LocationService.class));
		}
	}
}
