package com.core.lib_core.sqlite.agent;

import android.util.Log;

/**
 * 数据库的log代理类
 * 
 * @author wangtao<br>
 *         2013-1-22下午2:04:03
 */
public final class LogAgent {

	public static void d(String tag, String msg) {
		Log.d(tag, msg);
	}

	public static void w(String tag, String msg, Throwable throwable) {
		Log.w(tag, msg);
	}
}
