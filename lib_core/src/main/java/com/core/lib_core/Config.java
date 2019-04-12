package com.core.lib_core;

import android.os.Environment;

import com.core.util.Log;

/**
 * 
 * @author coffee<br>
 *         2014年9月20日下午3:47:26
 */
public class Config {

	public static boolean isTest = false;

	private static final String mainDir = "shouhuobao";
	// 为true表示 只用存储卡缓存数据
	private static boolean cacheOnlyInSD = false;

	/**
	 * 从安全上考虑,只保存在如下目录<br>
	 * /data/data/package/app_json <br>
	 */
	public final static String getJsonDir() {
		String dir = CoreApplication.getContext().getDir("json", 0).getAbsolutePath();
		Log.d("config:json", dir);
		return dir;
	}

	public final static String getMainDiar() {
		return Environment.getExternalStorageDirectory() + "/" + mainDir;
	}

	/**
	 * sdcard缓存路径(图片)
	 */
	public final static String getCacheDir() {
		String dir = "";
		if (cacheOnlyInSD || Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			dir = getMainDiar() + "/cache";
		} else {
			// /data/data/pkg/cache
			dir = CoreApplication.getContext().getCacheDir().toString();
		}
		Log.d("config:cache", dir);
		return dir;
	}

	/**
	 * 拍照保存的目录
	 */
	public final static String getCaptureDir() {
		String dir = "";
		if (cacheOnlyInSD || Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			dir = getMainDiar() + "/capture";
		} else {
			// /data/data/pkg/cache
			dir = CoreApplication.getContext().getCacheDir().toString();
		}
		Log.d("config:capture", dir);
		return dir;
	}

	public static String getCrashDir() {
		String dir = "";
		if (cacheOnlyInSD || Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			dir = getMainDiar() + "/crash";
		} else {
			dir = null;
		}
		Log.d("config:crash", dir);
		return dir;
	}
}
