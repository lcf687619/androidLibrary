package com.core.lib_core.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.core.lib_core.R;

/**
 * 项目中用到的对话框基础父类
 *
 * @author 李澄锋<br>
 *         2015-12-19下午3:55:39
 */
public class BaseDialog extends Dialog {

    // 内容区的宽度
    private int width;

    private Activity context;

    public BaseDialog(Activity context) {
        // 带主题样式
        super(context, R.style.app_dialog_theme);
        this.context = context;
        findViewById();
    }

    public BaseDialog(Activity context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        findViewById();
    }

    public Activity getActivityContext() {
        return this.context;
    }

    protected void findViewById() {
        //
    }

    /**
     * 如果是webview样式的对话框, 宽度可能需要设置大一点
     *
     * @param scale
     */
    @SuppressWarnings("deprecation")
    public void setWidth(float scale) {
        width = (int) (getWindow().getWindowManager().getDefaultDisplay().getWidth() * scale);
    }

    public void show() {
        Window win = getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        // win.getDecorView().setMinimumWidth((int) (win.getWindowManager().getDefaultDisplay().getWidth() * 0.6));// 设置dialog的宽度
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // lp.horizontalMargin = 300;
        lp.gravity = Gravity.CENTER;
        win.setAttributes(lp);
        win.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        if (width != 0) {
            win.setLayout(width, lp.height);
        } else {
            win.setLayout(lp.width, lp.height);
        }
        super.show();
    }

    public void superShow() {
        super.show();
    }
}
