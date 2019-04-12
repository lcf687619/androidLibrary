package com.core.lib_core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author miaoxin.ye
 * @createdate 2013-12-15 下午2:09:43
 * @Description: MD5加密
 */
public class MD5Util {

	/**
	 * @Description: MD5加密
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
		StringBuilder builder = new StringBuilder();
		try {
			MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(str.getBytes());
			byte messageDigest[] = mDigest.digest();
			for (byte b : messageDigest) {
				builder.append(Integer.toHexString((b >> 4) & 0xf));
				builder.append(Integer.toHexString(b & 0xf));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
	
}
