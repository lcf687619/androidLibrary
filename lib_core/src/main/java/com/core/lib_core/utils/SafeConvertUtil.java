package com.core.lib_core.utils;

public class SafeConvertUtil {

    public static int StrToInt(String string, int defaultValue) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static float StrToFloat(String string, float defaultValue) {
        try {
            if (!string.contains("-")) {
                return Float.parseFloat(string);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static long StrToLong(String string, long defaultValue) {
        try {
            return Long.parseLong(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static Double StrToDouble(String string, double defaultValue) {
        try {
            return Double.parseDouble(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}
