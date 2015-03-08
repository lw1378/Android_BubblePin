package com.laiwang.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {

	/**
	 * check whether a given email is a valid email string
	 * @param email input email string
	 * @return true if input is a valid email, false if not
	 */
	public static boolean isValidEmail(String email) {
		String patternString = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	/**
	 * by passing a password string, encrypt the password string with MD5 algorithm
	 * @param passwd input password string
	 * @return encrypted password string
	 * @throws NoSuchAlgorithmException
	 */
	public static String getEncryptPassword(String passwd) throws NoSuchAlgorithmException {
		if (passwd == null || passwd.length() == 0) return "";
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(passwd.getBytes());
		byte[] hash = digest.digest();
		StringBuilder sb = new StringBuilder();
		for (int i = 0;i < hash.length;i ++) {
			if ((0xff & hash[i]) < 0x10) {
				sb.append("0" + Integer.toHexString((0xff & hash[i])));
			} else {
				sb.append(Integer.toHexString((0xff & hash[i])));
			}
		}
		return sb.toString();
	}
}
