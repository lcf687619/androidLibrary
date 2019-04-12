package com.core.lib_core.widgets;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.core.lib_core.CoreApplication;
import com.core.lib_core.R;
import com.core.lib_core.utils.Android;
import com.core.lib_core.widgets.toast.SupportToast;

/**
 * 项目用用到的toast统一调用
 * 在华为、三星等一些手机上，禁止了通知栏权限后，toast不能正常显示，需要做单独得处理
 * 所以需要先判断是否有通知栏权限
 *
 * @author 李澄锋<br>
 * 2016-12-27下午4:24:47
 */
public class Alert {

    private static Toast sToast;

    /**
     * 获取当前Toast对象
     */
    public static Toast getToast() {
        return sToast;
    }

    /**
     * 初始化ToastUtils，建议在Application中初始化
     *
     * @param application 应用的上下文
     */
    public static void init(Application application) {
        // 判断有没有通知栏权限
        if (Android.isNotificationEnabled(application)) {
            sToast = new Toast(application);
        } else {
            sToast = new SupportToast(application);
        }
    }

    /**
     * 给当前Toast设置新的布局，具体实现可看
     */
    public static void setView(Context context, int layoutId) {
        if (context != context.getApplicationContext()) {
            context = context.getApplicationContext();
        }
        setView(View.inflate(context, layoutId, null));
    }

    public static void setView(View view) {

        checkToastState();

        if (view == null) {
            throw new IllegalArgumentException("Views cannot be empty");
        }

        // 如果吐司已经创建，就重新初始化吐司
        if (sToast != null) {
            //取消原有吐司的显示
            sToast.cancel();
            sToast.setView(view);
        }
    }

    /**
     * 检查吐司状态，如果未初始化请先调用
     */
    private static void checkToastState() {
        //吐司工具类还没有被初始化，必须要先调用init方法进行初始化
        if (sToast == null) {
            throw new IllegalStateException("ToastUtils has not been initialized");
        }
    }

    /**
     * 取消吐司的显示
     */
    public void cancel() {
        checkToastState();
        sToast.cancel();
    }

    /**
     * @param msg
     */
    public static void toast(String msg, final int... shortOrLong) {
        // 默认显示toast
        if (shortOrLong == null || shortOrLong.length == 0) {
            toastInternal(msg, Toast.LENGTH_SHORT);
        } else {
            toastInternal(msg, shortOrLong[0]);
        }
    }

    public static void toast(int res) {
        String str = CoreApplication.getContext().getResources().getString(res);
        toast(str);
    }

    @SuppressLint("InflateParams")
    private static void toastInternal(final String msg, final int shortOrLong) {
        if (Android.isNotificationEnabled(CoreApplication.getContext())) {
            CoreApplication.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    // toast布局
                    View layout = LayoutInflater.from(CoreApplication.getContext()).inflate(R.layout.app_toast_common, null);
                    TextView text = (TextView) layout.findViewById(R.id.toast_content);
                    text.setText(msg);
                    sToast.setGravity(Gravity.CENTER, 0, 0);
                    sToast.setDuration(shortOrLong);
                    sToast.setView(layout);
                    sToast.show();
                }
            });
        } else {
            View layout = LayoutInflater.from(CoreApplication.getContext()).inflate(R.layout.app_toast_common, null);
            sToast.setGravity(Gravity.CENTER, 0, 0);
            sToast.setDuration(shortOrLong);
            setView(layout);
            sToast.setText(msg);
            sToast.show();
        }
    }

    /**
     * 带图片的toast
     *
     * @param msg
     * @param shortOrLong
     */
    public static void toastWithImage(final View layoutView, final String msg, final int... shortOrLong) {
        if (Android.isNotificationEnabled(CoreApplication.getContext())) {
            CoreApplication.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    // toast布局
                    Toast toast = new Toast(CoreApplication.getContext());
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    // 默认显示toast
                    if (shortOrLong == null || shortOrLong.length == 0) {
                        toast.setDuration(Toast.LENGTH_SHORT);
                    } else {
                        toast.setDuration(shortOrLong[0]);
                    }
                    toast.setView(layoutView);
                    toast.show();

                }
            });
        } else {
            sToast.setGravity(Gravity.CENTER, 0, 0);
            // 默认显示toast
            if (shortOrLong == null || shortOrLong.length == 0) {
                sToast.setDuration(Toast.LENGTH_SHORT);
            } else {
                sToast.setDuration(shortOrLong[0]);
            }
            sToast.setView(layoutView);
            sToast.setText(msg);
            sToast.show();
        }
    }
}
