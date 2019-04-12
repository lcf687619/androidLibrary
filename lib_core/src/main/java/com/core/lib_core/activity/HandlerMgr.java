package com.core.lib_core.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.core.lib_core.CoreApplication;
import com.core.lib_core.fragment.FragmentMgr;
import com.core.lib_core.http.MsgID;
import com.core.lib_core.service.ServiceMgr;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 管理项目中用到的Handler
 *
 * @author 李澄锋
 * <p>
 * 2014年1月8日上午11:45:04
 */
public class HandlerMgr {

    /**
     * {@link #sendMessage(int, Object)}替代
     *
     * @param what
     */
    @Deprecated
    public static void sendMessage(int what) {
        sendMessage(what, null);
    }

    /**
     * @param what
     * @param obj
     */
    public static void sendMessage(int what, Object obj) {
        int delayMillis = 0;
        final Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        sendMessage(msg, delayMillis);
    }

    /**
     * 如果需要延迟发送的话则delayMillisArgs传入一个非0整数(单位毫秒)
     *
     * @param what            消息ID {@link MsgID}
     * @param arg1            返回结果状态码 .可以为空
     * @param obj             返回数据
     * @param delayMillisArgs
     */
    public static void sendMessage(int what, int arg1, Object obj, int... delayMillisArgs) {
        int delayMillis = 0;
        if (delayMillisArgs.length > 0) {
            delayMillis = delayMillisArgs[0];
        }
        final Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        msg.obj = obj;
        sendMessage(msg, delayMillis);
    }

    /**
     * 将消息发往Application、Activity、Fragment
     *
     * @param orig
     * @param delayMillis
     */
    public static void sendMessage(Message orig, int delayMillis) {
        // 发往application
        Message appMessage = Message.obtain(orig);
        CoreApplication.getHandler().sendMessage(appMessage);
//         发往Activity
        ArrayList<?> activities = ActivityMgr.getAllActivitys();
        for (final Object activity : activities) {
            boolean result = handle(activity, orig, delayMillis);
            if (result == true) {
                break;// 停止继续分发该Message
            } else {
                continue;
            }
        }
        // 发往Fragment
        HashMap<String, FragmentMgr> alls = FragmentMgr.getAllInstance();
        for (String activityName : alls.keySet()) {
            FragmentMgr fragmentMgr = alls.get(activityName);
            // 发往Fragment
            ArrayList<?> fragments = fragmentMgr.getAllFragments();
            for (final Object fragment : fragments) {
                boolean result = handle(fragment, orig, delayMillis);
                if (result == true) {
                    break;// 停止继续分发该Message
                } else {
                    continue;
                }
            }
        }
        // 发往Service
        ArrayList<?> services = ServiceMgr.getAllServices();
        for (final Object service : services) {
            boolean result = handle(service, orig, delayMillis);
            if (result == true) {
                break;// 停止继续分发该Message
            } else {
                continue;
            }
        }
        alls.clear();
    }

    public static void sendFinishMessage(Class<?> activityClass) {
        ArrayList<?> activities = ActivityMgr.getAllActivitys();
        for (final Object activity : activities) {
            if (activityClass.getName().equals(activity.getClass().getName())) {
                if (activity instanceof Activity) {
                    ((Activity) activity).finish();
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
    }

    public static void removeMessage(int what) {
        CoreApplication.getHandler().removeMessages(what);
    }

    /**
     * 删除所有的handler以及message
     */
    public static void removeCallbacksAndMessages() {
        /**
         * Remove any pending posts of callbacks and sent messages whose <var>obj</var> is <var>token</var>. If <var>token</var> is null, all callbacks and messages will be removed.
         */
        CoreApplication.getHandler().removeCallbacksAndMessages(null);
    }

    private static boolean handle(final Object activityOrFragment, Message orig, int delayMillis) {
        final Message msg = Message.obtain(orig);
        if (delayMillis == 0) {
            boolean result = false;
            if (activityOrFragment instanceof Handler.Callback) {
                result = ((Handler.Callback) activityOrFragment).handleMessage(msg);
            }
            return result;
        } else {
            CoreApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (activityOrFragment instanceof Handler.Callback) {
                        ((Handler.Callback) activityOrFragment).handleMessage(msg);
                    }
                }
            }, delayMillis);
        }
        return false;
    }
}
