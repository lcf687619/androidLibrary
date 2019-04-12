package com.core.lib_core.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.core.lib_core.CoreApplication;
import com.core.lib_core.utils.NiceLogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 项目用到的activity manager<br>
 * 主要记录当前activity
 *
 * @author coffee <br>
 * 2013-1-14下午2:41:05
 */
public class ActivityMgr {

    private static Stack<Activity> stack = new Stack<Activity>();

    public static ArrayList<Activity> getAllActivitys() {
        ArrayList<Activity> activities = new ArrayList<Activity>();
        activities.addAll(ActivityMgr.stack);// copy
        NiceLogUtil.d("ActivityMgr—getAll:" + stack);
        return activities;
    }

    public static void push(Activity activity) {
        stack.push(activity);
        NiceLogUtil.d("ActivityMgr-push：" + stack);
    }

    /**
     * 获取堆栈顶层的activity
     *
     * @return
     */
    public static Activity peek() {
        if (stack.isEmpty() == false) {
            return stack.peek();
        }
        return null;
    }

    /**
     * 获取堆栈顶层第二个的activity
     */
    public static Activity peekSecondActivity() {
        if (stack.isEmpty() == false) {
            ArrayList<Activity> activities = new ArrayList<Activity>();
            activities.addAll(ActivityMgr.stack);// copy
            if (activities.size() >= 2) {
                activities.remove(activities.size() - 1);
                return activities.remove(activities.size() - 1);
            } else {
                return stack.peek();
            }
        }
        return null;
    }

    public static void remove(Activity activity) {
        stack.remove(activity);
        NiceLogUtil.d("ActivityMgr-remove：" + stack);
    }

    // public Activity getActivity(String activityName) {
    // for (Activity activity : stack) {
    // if (activity.getClass().getName().equals(activityName)) {
    // return activity;
    // }
    // }
    // return null;
    // }

    /**
     * 结束所有的activity
     */
    public static void finishAll() {
        while (stack.isEmpty() == false) {
            Activity act = stack.pop();
            act.finish();
        }
    }

    /**
     * @return 返回className(包含packageName)
     */
    public static String getTopActivity() {
        Context context = CoreApplication.getContext();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        //
        if (runningTaskInfos != null) {
            ComponentName com = runningTaskInfos.get(0).topActivity;
            return com.getClassName();
        } else {
            return null;
        }
    }

    /**
     * @param context <br>
     *                需要权限 android.permission.GET_TASKS <br>
     * @return 返回className(包含packageName)
     */
    public static String getBaseActivity(Activity context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        //
        if (runningTaskInfos != null) {
            ComponentName com = runningTaskInfos.get(0).baseActivity;
            return com.getClassName();
        } else {
            return null;
        }
    }

    public static int getActivitiesNum(Activity context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        //
        if (runningTaskInfos != null) {
            return runningTaskInfos.get(0).numActivities;
        } else {
            return 0;
        }
    }

    /**
     * 启动主类，相当于launcher的效果<br>
     */
    public static void startMainActivity() {
        Context context = CoreApplication.getContext();
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(context.getPackageName());
        // 如果该程序不可启动（像系统自带的包，有很多是没有入口的）会返回NULL
        if (intent != null) {
            context.startActivity(intent);
        }
    }

    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null)
            return false;
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        if (runningService == null) {
            return false;
        }
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测activity是否还在存活
     *
     * @param context
     * @param cls
     * @return
     */
    public static boolean isActivityRunning(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        if (context.getPackageManager().resolveActivity(intent, 0) == null) {
            // 说明系统中不存在这个activity
            return false;
        } else {
            return true;
        }
    }

    /**
     *  判断某个界面是否在前台 
     *  @param context 
     *  @param className 
     *  某个界面名称 
     */
    public static boolean isForegroundActivity(Context context, String className) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName cpn = tasks.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
