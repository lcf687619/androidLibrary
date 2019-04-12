package com.core.lib_core.utils;

import android.util.Log;

import com.core.lib_core.constants.CoreConstant;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.Map;

/**
 * @author caibing.zhang
 * @createdate 2012-9-17 下午4:01:04
 * @Description: 日志
 */
public class NiceLogUtil {

    private static final String KEY = "--quxuche--";

    static {
        if (CoreConstant.IS_TEST_FLAG) {
            Logger.init(KEY).setMethodCount(0).hideThreadInfo()
                    .setLogLevel(CoreConstant.IS_TEST_FLAG ? LogLevel.FULL : LogLevel.NONE);
        }
    }

    private static String getMessageForNetWork() {
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[4];
        String className = stackTrace.getClassName();
        String tag = className.substring(className.lastIndexOf('.') + 1);
        StringBuilder sb = new StringBuilder();

        sb.append("(")
                .append(stackTrace.getFileName())
                .append(":")
                .append(stackTrace.getLineNumber())
                .append(")");

        return sb.toString();
    }


    public static void requestInActivity(Map<String, String> map) {
        String method = map.get("method");
        if (CoreConstant.IS_TEST_FLAG) {
            Logger.i(getMessageForNetWork() + "     >>>>>>>>>>>>>        method = " + method);
        }
    }

    /*返回信息相关打印*/
    public static void response(String url, String completeUrl, String message) {
        if (CoreConstant.IS_TEST_FLAG) {
            Logger.init("  " + url);
            Logger.i(completeUrl);
            Logger.i(getMessageForNetWork());
            Logger.json(message);
        }
    }

    /*请求信息相关打印*/
    public static void request(String url, String completeUrl, Map<String, String> params) {
        if (CoreConstant.IS_TEST_FLAG) {
            Logger.init("  " + url);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(getMessageForNetWork());
            stringBuffer.append("\n");
            stringBuffer.append("\n");
            stringBuffer.append(completeUrl);
            stringBuffer.append("\n");
            stringBuffer.append("\n");
            stringBuffer.append("api = " + url);
            stringBuffer.append("\n");
            stringBuffer.append("\n");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey() + " = " + entry.getValue());
                stringBuffer.append("\n");
                stringBuffer.append("\n");
            }
            Logger.i(stringBuffer.toString());
        }
    }

    public static void error(String url, String completeUrl, String message) {
        if (CoreConstant.IS_TEST_FLAG) {
            Logger.init("  " + url);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(completeUrl);
            stringBuffer.append("\n");
            stringBuffer.append("\n");
            stringBuffer.append(message);
            Logger.e(stringBuffer.toString());
        }
    }

    public static void i(String message) {
        if (CoreConstant.IS_TEST_FLAG) {
            Log.i(KEY, message);
        }
    }

    public static void e(String message) {
        if (CoreConstant.IS_TEST_FLAG) {
            Log.e(KEY, message);
        }
    }

    public static void d(String message) {
        if (CoreConstant.IS_TEST_FLAG) {
            Log.d(KEY, message);
        }
    }

    public static void D(String message) {
        if (CoreConstant.IS_TEST_FLAG) {
            Log.d(KEY, message);
        }
    }

    public static void w(String message) {
        if (CoreConstant.IS_TEST_FLAG) {
            Log.w(KEY, message);
        }
    }

    public static void w(String message, Throwable tr) {
        if (CoreConstant.IS_TEST_FLAG) {
            Log.w(KEY, message, tr);
        }
    }
}
