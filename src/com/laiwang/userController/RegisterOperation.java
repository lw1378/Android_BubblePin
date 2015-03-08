package com.laiwang.userController;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.example.android_login_googlemap.R;
import com.laiwang.util.ValidateUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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

public class RegisterOperation extends ActionBarActivity implements OnClickListener {

	private final static String TAG = "REGISTER";
	
	private TextView error_msg;
	private EditText emailText;
	private EditText usernameText;
	private EditText passwordText;
	private EditText confirmPasswordText;
	
	private Button register;
	private Button reset;
	
//	private SQLiteOpenUtil db = new SQLiteOpenUtil(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_operation);
		error_msg = (TextView)this.findViewById(R.id.register_error_msg);
		emailText = (EditText)this.findViewById(R.id.register_email);
		usernameText = (EditText)this.findViewById(R.id.register_username);
		passwordText = (EditText)this.findViewById(R.id.register_password);
		confirmPasswordText = (EditText)this.findViewById(R.id.confirm_register_password);
		register = (Button)this.findViewById(R.id.register_register);
		reset = (Button)this.findViewById(R.id.register_reset);
		
		register.setOnClickListener(this);
		reset.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_operation, menu);
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
		int action = v.getId();
		if (action == R.id.register_register) {
			String email = emailText.getText().toString().trim();
			String username = usernameText.getText().toString().trim();
			String password = passwordText.getText().toString().trim();
			String confirmPassword = confirmPasswordText.getText().toString().trim();
			if (!ValidateUtil.isValidEmail(email)) {
				emailText.requestFocus();
				emailText.setError("Invalid input email.");
			} else if (username.length() == 0) {
				usernameText.requestFocus();
				usernameText.setError("Username must not be null.");
			} else if (password.length() < 4) {
				passwordText.requestFocus();
				passwordText.setError("Password length must be greater than 4.");
			} else if (!password.equals(confirmPassword)) {
				confirmPasswordText.requestFocus();
				confirmPasswordText.setError("Confirm password is different from password.");
			} else {
//				if (db.validateRegister(email)) {
//					try {
//						db.insert(email, ValidateUtil.getEncryptPassword(password));
//						Intent i = new Intent(RegisterOperation.this, GPSOperation.class);
//						startActivity(i);
//					} catch (NoSuchAlgorithmException e) {
//						Log.d(TAG, e.getMessage());
//					}
//				} else {
//					error_msg.setText("User " + email + " has already exist in databse.");
//				}
				try {
					password = ValidateUtil.getEncryptPassword(password);
					signUp(email, username, password);
				} catch (NoSuchAlgorithmException e) {
					Log.d(TAG, e.getMessage());
				}
			}
		} else if (action == R.id.register_reset) {
			error_msg.setText("");
			emailText.setText("");
			passwordText.setText("");
		}
	}
	
	private void signUp(final String email, final String username, final String password) {
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", username);
		query.findInBackground(new FindCallback<ParseUser>() {
			
			@Override
			public void done(List<ParseUser> objects, ParseException e) {
				if (objects != null) {
					if (objects.size() != 0) {
						usernameText.requestFocus();
						usernameText.setError("Username has already exists in system.");
					} else {
						validateEmail(email, username, password);
					}
				} else {
					Log.d(TAG, e.getMessage());
				}
			}
		});
	}
	
	private void validateEmail(final String email, final String username, final String password) {
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("email", email);
		query.findInBackground(new FindCallback<ParseUser>() {

			@Override
			public void done(List<ParseUser> objects, ParseException e) {
				if (objects != null) {
					if (objects.size() != 0) {
						emailText.requestFocus();
						emailText.setError("Email has already exisits in system.");
					} else {
						signUpFinally(email, username, password);
					}
				} else {
					Log.d(TAG, e.getMessage());
				}
			}
		});
	}
	
	private void signUpFinally(final String email, final String username, final String password) {
		ParseUser parseUser = new ParseUser();
		parseUser.setUsername(username);
		parseUser.setEmail(email);
		parseUser.setPassword(password);
		
		parseUser.signUpInBackground(new SignUpCallback() {
			
			@Override
			public void done(ParseException e) {
				Log.i(TAG, "Sign up complete.");
				ParseUser curParseUser = ParseUser.getCurrentUser();
				if (curParseUser != null) {
					Intent i = new Intent();
					i.putExtra("username", username);
					i.putExtra("password", password);
					setResult(RegisterOperation.RESULT_OK, i);
					finish();
				} else {
					Log.d(TAG, e.getMessage());
				}
			}
		});
	}
}
