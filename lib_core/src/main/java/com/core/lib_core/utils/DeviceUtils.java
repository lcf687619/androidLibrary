package com.core.lib_core.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;

/**
 * @author 李澄锋<br>
 * 2018/9/21
 */
public class DeviceUtils {

    public static boolean isXiaoMi() {
        return Build.MANUFACTURER.toUpperCase().equals("XIAOMI");
    }

    public static boolean isHuaWei() {
        return Build.MANUFACTURER.toUpperCase().equals("HUAWEI");
    }

    public static boolean isMeiZu() {
        return Build.MANUFACTURER.toUpperCase().equals("MEIZU");
    }

    /**
     * 设备的软件版本号：
     * 例如：the IMEI/SV(software version) for GSM phones.
     * Return null if the software version is not available.
     *
     * @param context
     * @return
     */
    public static String getPhoneIMEI(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = "";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            imei = "";
        } else {
            imei = telephonyManager.getDeviceId();
        }
        if (!TextUtils.isEmpty(imei)) {

            return imei;
        }

        if (TextUtils.isEmpty(imei)) {
            // start get mac address
            WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiMan != null) {
                WifiInfo wifiInf = wifiMan.getConnectionInfo();
                if (wifiInf != null && wifiInf.getMacAddress() != null) {//48位，如FA:34:7C:6D:E4:D7
                    imei = wifiInf.getMacAddress().replaceAll(":", "");
                    return imei;
                }
            }
        }
        if (TextUtils.isEmpty(imei)) {
            imei = UUID.randomUUID().toString().replaceAll("-", "");//UUID通用唯一识别码(Universally Unique Identifier)（128位，如3F2504E0-4F89-11D3-9A0C-0305E82C3301）
        }
        return imei;
    }

    /**
     * getVersionCode:得到当前程序版本号. <br/>
     *
     * @param context
     * @return
     * @author hushuan
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * getVersionName:得到当前程序版本名称. <br/>
     *
     * @param context
     * @return
     * @author hushuan
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
