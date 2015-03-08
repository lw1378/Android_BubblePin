package com.laiwang.util;

import java.util.ArrayList;
import java.util.List;

import com.laiwang.bean.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteOpenUtil extends SQLiteOpenHelper {

	private static final String TAG = "SQLiteOpenUtil";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "UserInformation";
	
	private static final String TABLE_USER = "User";
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_EMAIL = "email";
	private static final String COLUMN_PASSWORD = "password";
	
	public SQLiteOpenUtil(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String create_table = "create table " + TABLE_USER + " ("
				+ COLUMN_ID + " integer primary key autoincrement, "
				+ COLUMN_EMAIL + " text, " + COLUMN_PASSWORD + " text)";
		db.execSQL(create_table);
		Log.i(TAG, "table now is created.");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String drop_table = "drop table if exists " + TABLE_USER;
		db.execSQL(drop_table);
		onCreate(db);
	}
	
	/**
	 * insert a new user into table User
	 * @param email user unique email id
	 * @param password user encrypted password
	 * @return current auto increment id
	 */
	public long insert(String email, String password) {
		Log.i(TAG, "new user " + email + " inserted.");
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_EMAIL, email);
		cv.put(COLUMN_PASSWORD, password);
		return getWritableDatabase().insert(TABLE_USER, null, cv);
	}
	
	/**
	 * remove a user that currently existing in database
	 * @param email user unique email id
	 */
	public void remove(String email) {
		Log.i(TAG, "current user " + email + " removed.");
		SQLiteDatabase db = this.getReadableDatabase();
		String getCurrentUser = "select * from " + TABLE_USER
				+ " where email='" + email + "'";
		Cursor cursor = db.rawQuery(getCurrentUser, null);
		for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
			int _id = cursor.getInt(0);
			db.delete(TABLE_USER, COLUMN_ID + "=" + _id, null);
		}
	}
	
	/**
	 * remove all records in table
	 */
	public void clear() {
		Log.i(TAG, "clear table " + TABLE_USER);
		SQLiteDatabase db = this.getReadableDatabase();
		db.delete(TABLE_USER, null, null);
	}
	
	/**
	 * return a specific user with email, return null if doesn't exist
	 * @param email email that the user has
	 * @return current user with specific email, null if doesn't exist
	 */
	public User getUser(String email) {
		Log.i(TAG, "return a specific user with email.");
		SQLiteDatabase db = this.getReadableDatabase();
		String getcurrentUser = "select * from " + TABLE_USER
				+ " where email='" + email + "'";
		Cursor cursor = db.rawQuery(getcurrentUser, null);
		cursor.moveToFirst();
		if (cursor.isAfterLast()) return null;
		User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
		return user;
	}
	
	/**
	 * return a list containing all users information
	 * @return list containing all users information
	 */
	public List<User> getAllUsers() {
		Log.i(TAG, "return all users list.");
		List<User> result = new ArrayList<User>();
		SQLiteDatabase db = this.getReadableDatabase();
		String getUsers = "select * from " + TABLE_USER;
		Cursor cursor = db.rawQuery(getUsers, null);
		for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
			User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
			result.add(user);
		}
		return result;
	}
	
	/**
	 * validate whether current user is in db
	 * @param email user email
	 * @param password user password
	 * @return true if user is valid in db, false if not
	 */
	public boolean validateLogin(String email, String password) {
		Log.i(TAG, "validate current user whether is valid in db");
		SQLiteDatabase db = this.getReadableDatabase();
		String val = "select * from " + TABLE_USER
				+ " where email='" + email + "' and password='" + password + "'";
		Cursor cursor = db.rawQuery(val, null);
		cursor.moveToFirst();
		if (cursor.isAfterLast()) return false;
		return true;
	}
	
	/**
	 * validate whether current user has already registered an account
	 * @param email user email
	 * @return true if the user is not in db, false if it is
	 */
	public boolean validateRegister(String email) {
		Log.i(TAG, "validate current user whether exisits in db");
		SQLiteDatabase db = this.getReadableDatabase();
		String val = "select * from " + TABLE_USER
				+ " where email='" + email + "'";
		Cursor cursor = db.rawQuery(val, null);
		cursor.moveToFirst();
		if (cursor.isAfterLast()) return true;
		return false;
	}
}
