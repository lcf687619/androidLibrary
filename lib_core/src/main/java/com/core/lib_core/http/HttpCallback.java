package com.core.lib_core.http;

/**
 * 网络加载回调
 * 
 * @author coffee
 * 
 *         2014年1月7日下午4:41:33
 */
public abstract class HttpCallback {

	/**
	 * 返回String或者Bitmap
	 * 
	 * @param result
	 */
	public void callback(Object result, HttpReq req) {
		if (result != null) {
			onSuccess(result);
		}else{
			onFailed(req);
		}
	}

	public void onFailed(HttpReq req) {

	}

	/**
	 * 
	 * @param result
	 *            bitmap或者String
	 */
	public abstract void onSuccess(Object result);
}
