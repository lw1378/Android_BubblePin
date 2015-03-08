package com.laiwang.googleMapController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.android_login_googlemap.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.laiwang.bean.Memory;
import com.laiwang.locationService.LocationService;
import com.laiwang.userController.LoginActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GPSOperation extends ActionBarActivity implements OnClickListener {

	private static final String TAG = "GPSOPERATION";

	private GoogleMap googleMap;

	private Button record; // use to record current record (picture, video)
	private Button clear; // clear current record
	private Button listRecord; // list all memory record
	private Button returnToMain; // return to the main menu page

	private final List<Memory> memories = new ArrayList<Memory>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isValidGooglePlayService()) {
			setContentView(R.layout.activity_gpsoperation);

			ParseUser user = ParseUser.getCurrentUser();
			if (user == null) {
				Toast.makeText(this, "Session Expired, login again",
						Toast.LENGTH_LONG).show();
				startActivity(new Intent(this, LoginActivity.class));
			}

			createMap();
			record = (Button) this.findViewById(R.id.gps_record_current);
			clear = (Button) this.findViewById(R.id.gps_clear_current);
			listRecord = (Button) this.findViewById(R.id.gps_record_list);
			returnToMain = (Button) this.findViewById(R.id.gps_return);

			record.setOnClickListener(this);
			clear.setOnClickListener(this);
			listRecord.setOnClickListener(this);
			returnToMain.setOnClickListener(this);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gpsoperation, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.start_location_service) {
			startLocationService();
		} else if (id == R.id.stop_location_service) {
			stopLocationService();
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressLint("NewApi")
	private void createMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			if (googleMap != null) {
				googleMap.setMyLocationEnabled(true);
				UiSettings uiSettings = googleMap.getUiSettings();
				uiSettings.setZoomControlsEnabled(true);
				uiSettings.setCompassEnabled(true);
			}
		}
	}

	private boolean isValidGooglePlayService() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (status == ConnectionResult.SUCCESS) {
			return true;
		} else {
			Log.i(TAG, "Google Play Service is not available.");
			GooglePlayServicesUtil.getErrorDialog(status, this, 10).show();
			return false;
		}
	}

	private void startLocationService() {
		if (!LocationService.isRunning()) {
			startService(new Intent(this, LocationService.class));
		} else {
			Toast.makeText(this, "Service is already running on background.",
					Toast.LENGTH_LONG).show();
		}
	}

	private void stopLocationService() {
		if (LocationService.isRunning()) {
			stopService(new Intent(this, LocationService.class));
		} else {
			Toast.makeText(this, "Service has already stopped.",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onClick(View v) {
		int action = v.getId();
		if (action == R.id.gps_record_current) {

		} else if (action == R.id.gps_clear_current) {

		} else if (action == R.id.gps_record_list) {

		} else if (action == R.id.gps_return) {

		}
	}

	private void initMemories() {
		ParseUser user = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Memory");
		query.whereEqualTo("userObjectId", user.getObjectId());
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (objects != null) {
					for (ParseObject obj : objects) {
						String title = obj.getString("title");
						String introduction = obj.getString("introduction");
						String dataUri = obj.getString("dataUri");
						ParseGeoPoint geoPoint = obj
								.getParseGeoPoint("location");
						Date createdAt = obj.getCreatedAt();
						Date updatedAt = obj.getUpdatedAt();
						Memory memory = new Memory(title, introduction,
								dataUri, geoPoint.getLatitude(), geoPoint
										.getLongitude(), createdAt, updatedAt);
						memories.add(memory);
					}
				} else {
					Log.d(TAG, e.getMessage());
				}
			}
		});
	}

	private void saveCurrentRecord() {
		final Location location = LocationService.getCurrentLocation();
		if (location != null) {
			LayoutInflater li = LayoutInflater.from(this);
			View prompView = li.inflate(R.layout.gpslocation_new_record, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setView(prompView);
			final EditText titleText = (EditText) prompView
					.findViewById(R.id.new_record_title);
			final EditText introductionText = (EditText) prompView
					.findViewById(R.id.new_record_introduction);
			alertDialogBuilder
					.setTitle("New Memory Record")
					.setCancelable(false)
					.setPositiveButton("Okay",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String title = titleText.getText()
											.toString().trim();
									String introduction = introductionText
											.getText().toString().trim();
									ParseGeoPoint point = generateParseGeoPoint(location);
									ParseUser user = ParseUser.getCurrentUser();
									ParseObject object = new ParseObject(
											"Memory");
									object.put("userObjectId", user.getObjectId());
									object.put("location", point);
									object.put("title", title);
									object.put("introduction", introduction);
									object.put("dataUri",
											"http://www.google.com"); // *** here we suppose data is fixed
									object.saveInBackground(new SaveCallback() {

										@Override
										public void done(ParseException e) {
											if (e == null) {
												Toast.makeText(
														GPSOperation.this,
														"New memory added successfully.",
														Toast.LENGTH_LONG)
														.show();
												Log.i(TAG,
														"New memory added successfully.");
											} else {
												Log.d(TAG, e.getMessage());
											}
										}
									});
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
	}

	private void clearRecordByCondition(String title, String introduction, ParseGeoPoint point) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Memory");
		if (title != null) query.whereEqualTo("title", title);
		if (introduction != null) query.whereEqualTo("introduction", introduction);
		if (point != null) query.whereEqualTo("location", point);
		return;
	}
	
	private ParseGeoPoint generateParseGeoPoint(Location location) {
		return new ParseGeoPoint(location.getLatitude(),
				location.getLongitude());
	}
}
