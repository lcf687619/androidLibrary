/**
 * 
 */
package com.core.lib_core.title;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * @author 李澄锋 <br>
 *         2016-3-15下午3:32:36
 */
public abstract class AppTitle {

	// 后退按钮
	private int backDrawable;

	/**
	 * Activity or Fragment
	 */
	protected Object context;

	/**
	 * 注意该参数在Fragment中不能传入Activity类对象
	 * 
	 * @param activityOrFragment
	 */
	public AppTitle(Object activityOrFragment) {
		this.context = activityOrFragment;
	}

	public abstract AppTitle initTitle();

	public abstract AppTitle setTitle(TitleRes... reses);

	public abstract AppTitle setCommonTitle(String title);

	public abstract AppTitle setCommonTitle(String titleText, TitleRes rightTitle);

	/**
	 * @param res
	 * @param position
	 *            0-1-2 , 左中右
	 * @return
	 */
	public abstract AppTitle setTitle(TitleRes res, int position);

	protected String getPackageName() {
		if (context instanceof FragmentActivity) {
			return ((FragmentActivity) context).getPackageName();
		} else if (context instanceof Fragment) {
			return ((Fragment) context).getActivity().getPackageName();
		}
		return null;
	}

	protected Resources getResources() {
		if (context instanceof FragmentActivity) {
			return ((FragmentActivity) context).getResources();
		} else if (context instanceof Fragment) {
			return ((Fragment) context).getActivity().getResources();
		}
		return null;
	}

	protected View findViewById(int resId) {
		if (context instanceof FragmentActivity) {
			return ((FragmentActivity) context).findViewById(resId);
		} else if (context instanceof Fragment) {
			View fragmentView = ((Fragment) context).getView();
			if (fragmentView != null) {
				return fragmentView.findViewById(resId);
			}
		}
		return null;
	}

	public int getBackDrawable() {
		return backDrawable;
	}

	/**
	 * 设置后退按钮的资源id(drawable)
	 */
	public AppTitle setBackDrawable(int backDrawable) {
		this.backDrawable = backDrawable;
		return this;
	}

	/**
	 * 获取所有的TitleRes
	 * 
	 * @return
	 */
	public TitleRes[] getTitlereReses() {
		return new TitleRes[] {};
	}
}
