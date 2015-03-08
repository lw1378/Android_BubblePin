package com.laiwang.locationService;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class NetworkLocationSensor {

	private Context mContext;
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	
	private Location mLocation;
	private double mLongitude;
	private double mLatitude;
	private double mAltitude;
	private float mAccuracy;
	private float mSpeed;
	private String mAddress;
	
	private long mTime;
	
	public NetworkLocationSensor(Context mContext) {
		this.mContext = mContext;
	}
	
	public void networkLocation_Enable() {
		mLocationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				mLocation = location;
				mLongitude = location.getLongitude();
				mLatitude = location.getLatitude();
				mAltitude = location.getAltitude();
				mAccuracy = location.getAccuracy();
				mSpeed = location.getSpeed();
				mTime = location.getTime();
				
				// Get current human readable address
				Geocoder myLoc = new Geocoder(mContext.getApplicationContext(), Locale.getDefault());
				List<Address> mList = null;
				try {
					mList = myLoc.getFromLocation(mLatitude, mLongitude, 1);
					if (mList != null && mList.size() > 0) {
						Address loc = mList.get(0);
						String addressText = String.format("%s, %s, %s",
								loc.getMaxAddressLineIndex() > 0? loc.getAddressLine(0):"",
										loc.getLocality(), loc.getCountryName());
						mAddress = addressText;
					} else {
						mAddress = "n/a";
					}
				} catch (Exception e) {
					Log.d("Error", e.getMessage());
				}
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
	}
	
	public void networkLocation_Disable() {
		if (mLocationManager != null) {
			mLocationManager.removeUpdates(mLocationListener);
		}
	}
	
	// public interfaces !
	// return all values
	
	public Location getLocation() {
		return this.mLocation;
	}
	
	public double getLongitude() {
		return this.mLongitude;
	}
	
	public double getLatitude() {
		return this.mLatitude;
	}
	
	public double getAltitude() {
		return this.mAltitude;
	}
	
	public float getAccuracy() {
		return this.mAccuracy;
	}
	
	public float getSpeed() {
		return this.mSpeed;
	}
	
	public String getAddress() {
		return this.mAddress;
	}
	
	public long getTimestamp() {
		return this.mTime;
	}
	
	/**
	 * check whether network is now available
	 * it it good to check this before getting any values
	 */
	public boolean isAvailable() {
		return this.mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}
}
