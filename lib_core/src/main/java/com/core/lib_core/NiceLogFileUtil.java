package com.core.lib_core;

import com.core.util.StringUtil;
import com.core.util.file.FileUtil;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * Created by ${nice} on ${2016年04月29日14:09:09}.
 */

public class NiceLogFileUtil {

    private static final String LogFileName = "log_file.txt";

    private static String getFilePath() {
        return FileUtil.getSDCacheDir(CoreApplication.getInstance()) + "/" + LogFileName;
    }

    public static void writeLog(String writeStr) {
        try {
            OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(getFilePath(),true), "utf-8");
            oStreamWriter.write(writeStr);
            oStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeRequestMessage(String url, String completeUrl, Map<String, String> params) {
        StringBuffer stringBuffer = new StringBuffer();
        String time = StringUtil.getDateToStringLongNot1000(System.currentTimeMillis());
        stringBuffer.append("\n");
        stringBuffer.append("\n");
        stringBuffer.append("\n");
        stringBuffer.append("\n");
        stringBuffer.append("请求信息：");
        stringBuffer.append(time);
        stringBuffer.append("\n");
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
        writeLog(stringBuffer.toString());
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


    /*返回信息相关打印*/
    public static void response(String completeUrl, String message) {
        StringBuffer stringBuffer = new StringBuffer();
        String time = StringUtil.getDateToStringLongNot1000(System.currentTimeMillis());
        stringBuffer.append("\n");
        stringBuffer.append("\n");
        stringBuffer.append("\n");
        stringBuffer.append("\n");
        stringBuffer.append("返回信息：");
        stringBuffer.append(time);
        stringBuffer.append("\n");
        stringBuffer.append(completeUrl);
        stringBuffer.append("\n");
        stringBuffer.append("\n");
        stringBuffer.append(message);
        writeLog(stringBuffer.toString());
    }

    public static void error(String completeUrl, String message) {
        StringBuffer stringBuffer = new StringBuffer();
        String time = StringUtil.getDateToStringLongNot1000(System.currentTimeMillis());
        stringBuffer.append("\n");
        stringBuffer.append("\n");
        stringBuffer.append("\n");
        stringBuffer.append("\n");
        stringBuffer.append("错误：");
        stringBuffer.append(time);
        stringBuffer.append("\n");
        stringBuffer.append(completeUrl);
        stringBuffer.append("\n");
        stringBuffer.append("\n");
        stringBuffer.append(message);
        writeLog(stringBuffer.toString());
    }
}
