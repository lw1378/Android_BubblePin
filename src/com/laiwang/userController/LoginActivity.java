package com.laiwang.userController;

import java.security.NoSuchAlgorithmException;

import com.example.android_login_googlemap.R;
import com.laiwang.googleMapController.MainMenuActivity;
import com.laiwang.util.ValidateUtil;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity implements OnClickListener {

	private final static String TAG = "LOGIN";
//	private Animation shakeAction;
	
	private TextView errorText;
	private EditText usernameText;
	private EditText passwordText;
	
	private Button login;
	private Button register;
	
	private static final int REGISTER_REQUESTCODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		shakeAction = new TranslateAnimation(-3, 3, 0, 0);
//		shakeAction.setDuration(100);
//		shakeAction.setRepeatCount(5);
		errorText = (TextView)this.findViewById(R.id.error_msg);
		usernameText = (EditText)this.findViewById(R.id.login_username);
		passwordText = (EditText)this.findViewById(R.id.password);
		login = (Button)this.findViewById(R.id.login);
		register = (Button)this.findViewById(R.id.register);
		
		login.setOnClickListener(this);
		register.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.login) {
			String username = usernameText.getText().toString();
			String password = passwordText.getText().toString();
			if (username.length() == 0) {
				usernameText.requestFocus();
				usernameText.setError("Input is not a valid username.");
			} else if (password.length() < 4) {
				passwordText.requestFocus();
				passwordText.setError("Password length must be greater than 4.");
			} else {
				try {
					password = ValidateUtil.getEncryptPassword(password);
//					SQLiteOpenUtil db = new SQLiteOpenUtil(this);
//					boolean isValid = db.validateLogin(email, password);
//					if (isValid) {
//						Intent i = new Intent(MainActivity.this, GPSOperation.class);
//						startActivity(i);
//					} else {
//						errorText.setText("Incorrect email or password.");
//					}
					login(username, password);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					Log.d(TAG, "Error when encrypting password.");
					errorText.setText("Error when encrypting password.");
				}
			}
			
		} else if (id == R.id.register) {
			Intent i = new Intent(LoginActivity.this, RegisterOperation.class);
			startActivityForResult(i, REGISTER_REQUESTCODE);
		}
	}
	
	private void login(final String username, final String password) {
		ParseUser.logInInBackground(username, password, new LogInCallback() {
			
			@Override
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					Log.i(TAG, "login success");
					Toast.makeText(LoginActivity.this, "Welcome back, "+username, Toast.LENGTH_LONG).show();
					startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
				} else {
					Log.i(TAG, "login failed, incorrect username or password.");
					errorText.setText("login failed, incorrect username or password.");
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Log.i(TAG, "return back to mainActivity from register.");
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == REGISTER_REQUESTCODE) {
			if (resultCode == RESULT_OK) {
				String username = intent.getExtras().getString("username");
				String password = intent.getExtras().getString("password");
				login(username, password);
			}
		}
	}
}
