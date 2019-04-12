package com.core.lib_core.service;

import android.app.Service;

import com.core.lib_core.utils.NiceLogUtil;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by joker on 2018/5/9.
 */
public class ServiceMgr {

    private static Stack<Service> stack = new Stack<Service>();

    public static void push(Service service) {
        stack.push(service);
        NiceLogUtil.d("ServiceMgr-push：" + stack);
    }

    /**
     * 获取堆栈顶层的activity
     *
     * @return
     */
    public static Service peek() {
        if (stack.isEmpty() == false) {
            return stack.peek();
        }
        return null;
    }

    public static void remove(Service service) {
        stack.remove(service);
        service.stopSelf();
        NiceLogUtil.d("ActivityMgr-remove：" + stack);
    }

    /**
     * 结束所有的activity
     */
    public static void finishAll() {
        while (stack.isEmpty() == false) {
            Service act = stack.pop();
            act.stopSelf();
        }
    }

    public static ArrayList<Service> getAllServices() {
        ArrayList<Service> activities = new ArrayList<Service>();
        activities.addAll(ServiceMgr.stack);// copy
        NiceLogUtil.d("ServiceMgr—getAll：" + stack);
        return activities;
    }
}
