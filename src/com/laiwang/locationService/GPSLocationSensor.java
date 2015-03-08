package com.laiwang.locationService;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GPSLocationSensor {
	
	private Context mContext;
	
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	private GpsStatus.Listener mGpsStatusListener;
	
	private Location mLocation;
	private double mLongitude;
	private double mLatitude;
	private double mAltitude;
	private float mAccuracy;
	private float mBearing;
	private float mSpeed;
	private long mTime;
	private String mAddress;
	private int mSatellites;
	
	private static final long GPS_UPDATE_TIME_INTERVAL = 10000; // ms
	private static final long GPS_UPDATE_MINIMAL_DISTANCE = 30; // m
	
	public GPSLocationSensor(Context mContext) {
		this.mContext = mContext;
	}
	
	public void gps_Enable() {
		mLocationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				mLocation = location; // get current location
				mLongitude = location.getLongitude(); // Get the longitude by GPS
				mLatitude = location.getLatitude(); // Get the latitude by GPS
				mAltitude = location.getAltitude(); // Get the altitude by GPS
				mAccuracy = location.getAccuracy(); // The accuracy of current location
				mBearing = location.getBearing(); // Get the bearing in degrees
				mSpeed = location.getSpeed(); // Get current movement speed if it is available
				mTime = location.getTime(); // Get current time
				
				// Return current human readable address
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
					Log.d("error", e.getMessage());
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
		
		mGpsStatusListener = new Listener() {

			@Override
			public void onGpsStatusChanged(int event) {
				if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
					GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
					Iterable<GpsSatellite> gpsIt = gpsStatus.getSatellites();
					int numSatellite = 0;
					for (GpsSatellite satellite: gpsIt) {
						numSatellite += 1;
					}
					mSatellites = numSatellite;
				}
			}
			
		};
		
		mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_UPDATE_TIME_INTERVAL, 
				GPS_UPDATE_MINIMAL_DISTANCE, mLocationListener);
		mLocationManager.addGpsStatusListener(mGpsStatusListener);
	}
	
	public void gps_Disable() {
		if (mLocationManager != null) {
			mLocationManager.removeUpdates(mLocationListener);
		}
		if (mGpsStatusListener != null) {
			mLocationManager.removeGpsStatusListener(mGpsStatusListener);
		}
	}
	
	// public interfaces !
	// return all kinds of values
	
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
	
	public float getBearing() {
		return this.mBearing;
	}
	
	public float getSpeed() {
		return this.mSpeed;
	}
	
	public String getAddress() {
		return this.mAddress;
	}
	
	public int getSatelliteNumber() {
		return this.mSatellites;
	}
	
	public long getTimestamp() {
		return this.mTime;
	}
	
	/**
	 * check whether now GPS is available
	 * it is good to check this first before getting any values
	 */
	public boolean isAvailable() {
		return this.mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
}
