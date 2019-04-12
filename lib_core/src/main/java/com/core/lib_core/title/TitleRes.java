package com.core.lib_core.title;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 如果Activity没有继承BaseActivity同时需要用到common_title, 则title的设置方法如下<br>
 * TitleMgr titleMgr = new TitleMgr(this); <br>
 * titleMgr.initTitle(); <br>
 * titleMgr.setTitle(new TitleRes[] { titleMgr.getBackTitle(), new TitleRes("xxx"), null });
 * 
 * @author 李澄锋<br>
 *         2015-3-24下午4:57:20
 */
public class TitleRes {
	/**
	 * 0 文本, 1图片
	 */
	private int type;
	/**
	 * {@link CharSequence} 或者int资源(文本|图片)或者 {@link Drawable}对象
	 */
	private Object res;

	private View.OnClickListener clickListener;

	public int getType() {
		return type;
	}

	//
	public TitleRes(int type, Object res, OnClickListener clickListener) {
		super();
		this.type = type;
		this.res = res;
		this.clickListener = clickListener;
	}

	/**
	 * 适用于标题是文字的。没单击事件
	 *
	 * @param title
	 */
	public TitleRes(CharSequence title) {
		this.type = 0;
		this.res = title;
		this.clickListener = null;
	}

	/**
	 * 适用于标题是文字的。没单击事件
	 *
	 * @param titleImage
	 */
	public TitleRes(int titleImage) {
		this.type = 1;
		this.res = titleImage;
		this.clickListener = null;
	}

	/**
	 * 适用于title右侧文本单击事件
	 *
	 * @param title
	 * @param clickListener
	 */
	public TitleRes(CharSequence title, OnClickListener clickListener) {
		this.type = 0;
		this.res = title;
		this.clickListener = clickListener;
	}

	public TitleRes(int imageIcon, OnClickListener clickListener) {
		this.type = 1;
		this.res = imageIcon;
		this.clickListener = clickListener;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Object getRes() {
		return res;
	}

	public void setRes(Object res) {
		this.res = res;
	}

	public View.OnClickListener getClickListener() {
		return clickListener;
	}

	public void setClickListener(View.OnClickListener clickListener) {
		this.clickListener = clickListener;
	}
}