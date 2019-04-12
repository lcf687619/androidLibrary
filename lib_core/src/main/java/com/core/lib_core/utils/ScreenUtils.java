package com.core.lib_core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import com.core.lib_core.constants.CoreConstant;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.List;
import java.util.Random;

/**
 * @author 李澄锋<br>
 * 2018/8/14
 */
public class ScreenUtils {
    /**
     * @param context
     * @param dipValue
     * @return
     * @Description: dip转像素, 不同分辨率适配
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int dp2px(Context context, double dp) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);

        return (int) (dp * displaymetrics.density + 0.5f);
    }

    public static int px2dp(Context context, int px) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);

        return (int) (px / displaymetrics.density + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * @param activity
     * @return
     * @Description: 获取屏幕宽高
     */
    public static int[] getDisplay(Activity activity) {
        int[] display = new int[2];
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        display[0] = dm.widthPixels;
        display[1] = dm.heightPixels;
        return display;
    }

    /**
     * @param view
     * @return
     * @Description: 获取视图在屏幕的坐标
     */
    public static int[] getViewXYOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location;
    }

    public static void clearList(List<?> list) {
        if (list != null) {
            list.clear();
            list = null;
        }
    }

    /**
     * 2015年4月23日 09:59:53
     * 用于生成Key加密码
     *
     * @param mContext
     * @return
     */
    public static String getHasKey(Context mContext) {
        //Get Has Key
        String hash = "";
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(CoreConstant.PACKAGE_NAME, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                hash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                NiceLogUtil.D("    KeyHash=" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hash;
    }

    /**
     * 取得旋转变化的动画
     *
     * @return
     */
    public static Animation getRoateAnimation() {
        Random random = new Random();
        int count = random.nextInt(5);

        int start = random.nextInt(5);
        final RotateAnimation animation = new RotateAnimation(start, count, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(600);//设置动画持续时间
        /** 常用方法 */
        //animation.setRepeatCount(int repeatCount);//设置重复次数
        //animation.setFillAfter(boolean);//动画执行完后是否停留在执行完的状态
        //animation.setStartOffset(long startOffset);//执行前的等待时间
        animation.setInterpolator(new AccelerateInterpolator());
        return animation;
    }

    /**
     * 取得缩放的图
     *
     * @return
     */
    public static Animation getLikeScaleAnimation() {
        ScaleAnimation scale = new ScaleAnimation(1.5f, 1.0f, 1.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(500);
        scale.setFillAfter(false);
        return scale;
    }

    /**
     * 取得淡入的效果图
     *
     * @return
     */
    public static Animation getFadeInScaleAnimation(Animation.AnimationListener animationListerner) {

        AlphaAnimation ani = new AlphaAnimation(0.0f, 1.0f);
        ani.setDuration(2000);
        ani.setFillAfter(false);
        ani.setAnimationListener(animationListerner);
        return ani;
    }

    /**
     * 取得淡出的效果图
     *
     * @return
     */
    public static Animation getFadeOutScaleAnimation(Animation.AnimationListener animationListerner) {
        AlphaAnimation ani = new AlphaAnimation(1.0f, 0.0f);
        ani.setDuration(500);
        ani.setFillAfter(false);
        ani.setAnimationListener(animationListerner);
        return ani;
    }

    /**
     * @param context
     * @param metaKey 从Manifest.xml配置文件中获取数据  	<meta-data android:name="api_key" android:value="xxx" />
     * @return
     * @author sufun
     * @createtime 2015年11月16日 12:24:20
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String metaValue = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                metaValue = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return metaValue;// xxx
    }

    /**
     * 将View转换成Bitmap
     *
     * @param v
     * @return
     */
    public static Bitmap getViewToBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    /**
     * 华为手机检测是否存在刘海屏
     *
     * @param context
     * @return
     */
    public static boolean hasNotchInScreen(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("test", "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("test", "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            Log.e("test", "hasNotchInScreen Exception");
        } finally {
            return ret;
        }
    }

    /**
     * OPPO判断刘海屏的方式
     *
     * @param context
     * @return
     */
    public static boolean hasNotchInOppo(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    /**
     * VIVO手机判断刘海屏的方式
     */
    public static final int NOTCH_IN_SCREEN_VOIO = 0x00000020;//是否有凹槽
    public static final int ROUNDED_IN_SCREEN_VOIO = 0x00000008;//是否有圆角

    public static boolean hasNotchInScreenAtVoio(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class FtFeature = cl.loadClass("com.util.FtFeature");
            Method get = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) get.invoke(FtFeature, NOTCH_IN_SCREEN_VOIO);

        } catch (ClassNotFoundException e) {
            Log.e("test", "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("test", "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            Log.e("test", "hasNotchInScreen Exception");
        } finally {
            return ret;
        }
    }

    /**
     * 获取刘海屏的参数
     *
     * @param context
     * @return
     */
    public static int[] getNotchSize(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("test", "getNotchSize ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("test", "getNotchSize NoSuchMethodException");
        } catch (Exception e) {
            Log.e("test", "getNotchSize Exception");
        } finally {
            return ret;
        }
    }
}
