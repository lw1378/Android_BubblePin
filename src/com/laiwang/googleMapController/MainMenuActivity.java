package com.laiwang.googleMapController;

import com.example.android_login_googlemap.R;
import com.laiwang.userController.LoginActivity;
import com.parse.ParseUser;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainMenuActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		ParseUser user = ParseUser.getCurrentUser();
		if (user == null) {
			Toast.makeText(this, "Session Expired, login again", Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, LoginActivity.class));
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
//			startActivity(new Intent(MainMenuActivity.this, UpdateProfile.class));
			startActivity(new Intent(MainMenuActivity.this, GPSOperation.class));
		}
		return super.onOptionsItemSelected(item);
	}
}
