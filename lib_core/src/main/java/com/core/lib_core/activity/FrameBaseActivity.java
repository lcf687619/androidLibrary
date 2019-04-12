package com.core.lib_core.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.core.lib_core.fragmentation.SupportActivity;
import com.core.lib_core.systembar.SystemBarConfig;
import com.core.lib_core.utils.Android;
import com.core.lib_core.widgets.Alert;

/**
 * @author 李澄锋<br>
 * 2018/8/13
 */
public abstract class FrameBaseActivity extends SupportActivity implements Handler.Callback {

    // 推荐用方法访问该变量 getContext()
    private Activity context;
    protected Dialog dialog = null;
    protected boolean isResume = false;
    /**
     * 状态栏背景颜色
     */
    private int statusBarColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        ActivityMgr.push(this);
        // 沉浸式状态栏和requestWindowFeature(Window.FEATURE_NO_TITLE)冲突
        if (statusBarColor != 0 && SystemBarConfig.isSupport()) {
            // 注意需要设置FEATURE_NO_TITLE 否则适配可能会有bug
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            SystemBarConfig.showSystemStatusBar(this, statusBarColor);
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        findViewById();
    }

    /**
     * 需要在子类onCreate之前调用
     *
     * @param statusBarColor
     */
    public void setStatusBarColor(int statusBarColor) {
        this.statusBarColor = statusBarColor;
    }

    /**
     * 注意子该方法之前需要先调用 {@link #setContentView(int)}
     */
    protected void findViewById() {
    }

    public Activity getContext() {
        return context;
    }

    public void showToast(final String message) {
        Alert.toast(message);
    }

    public void showToast(final int messageId) {
        Alert.toast(messageId);
    }

    @Override
    public void startActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        super.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        super.startActivityForResult(intent, requestCode);
    }

    /**
     * 处理各个Activity的请求message
     *
     * @param msg
     * @return 如果返回true则该消息会停止继续分发<br>
     * {@link HandlerMgr#sendMessage(int, int, Object, int...)}
     */
    @Override
    public boolean handleMessage(Message msg) {

        return false;
    }

    /**
     * @param cancelable 是否支持取消操作
     * @Description: 显示加载对话框
     */
    public abstract Dialog showWaitDialog(boolean cancelable);

    /**
     * @author caibing.zhang
     * @createdate 2012-6-6 下午1:57:01
     * @Description: 显示异步交互后的数据, 隐藏加载框
     */
    public void dissmissWaitingDialog() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Alert.init(getApplication());
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResume = false;
    }

    @Override
    protected void onStop() {
        dissmissWaitingDialog();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ActivityMgr.remove(this);
        Android.hideSoftInput(getContext());
        super.onDestroy();
    }
}
