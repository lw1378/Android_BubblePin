package com.laiwang.util;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

public class LocationUtil {

	/**
	 * Public API used to parse the given latitude and longitude into a human read address
	 * @param context current context
	 * @param latitude parse latitude
	 * @param longitude parse longitude
	 * @return human read address
	 * @throws IOException
	 */
	public String getAddress(Context context, double latitude, double longitude)
			throws IOException {
		Geocoder myLocation = new Geocoder(context.getApplicationContext(), Locale.getDefault());
		List<Address> list = myLocation.getFromLocation(latitude, longitude, 1);
		if (list != null && list.size() > 0) {
			Address address = list.get(0);
			String addressText = String.format("%s, %s, %s", address
					.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0)
					: "", address.getLocality(), address.getCountryName());
			return addressText;
		}
		return "n/a";
	}

	/**
	 * Public API used to parse the given latitude and longitude into a human read address
	 * @param context current context
	 * @param location current location information
	 * @return human read address
	 * @throws IOException
	 */
	public String getAddress(Context context, Location location)
			throws IOException {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		return getAddress(context, latitude, longitude);
	}
}
