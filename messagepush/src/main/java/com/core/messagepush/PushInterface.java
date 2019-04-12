package com.core.messagepush;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.core.lib_core.CoreApplication;
import com.core.lib_core.activity.HandlerMgr;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

public class PushInterface {

    /**
     * 初始化 init接口,并唤醒JPush
     *
     * @param type 0
     */
    public static void init(int type) {
        final Context context = CoreApplication.getContext();
        if (isXiaoMi()) {
            XGPushConfig.setMiPushAppId(context, Constants.HuoJianTouDi_XIAOMI_APPID);
            XGPushConfig.setMiPushAppKey(context, Constants.HuoJianTouDi_XIAOMI_APPKEY);
        } else if (isMeiZu()) {
            XGPushConfig.setMzPushAppId(context, Constants.HuoJianXia_MEIZU_APPID);
            XGPushConfig.setMzPushAppKey(context, Constants.HuoJianXia_MEIZU_APPKEY);
        }
        XGPushConfig.enableOtherPush(context, true);
        XGPushManager.registerPush(context,
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        String token = XGPushConfig.getToken(context);
                        Log.d("44444", token);
                        HandlerMgr.sendMessage(PushMsgID.message_token, token);
                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.d("11111", "推送注册失败");
                    }
                });
    }

    /**
     * 注销JPush<br>
     * 注意stop以后必须resumePush<br>
     */
    public static void logout() {
        Context context = CoreApplication.getContext();
        XGPushManager.registerPush(context, "");
    }

    private static boolean isXiaoMi() {
        return Build.MANUFACTURER.toUpperCase().equals("XIAOMI");
    }

    private static boolean isHuaWei() {
        return Build.MANUFACTURER.toUpperCase().equals("HUAWEI");
    }

    private static boolean isMeiZu() {
        return Build.MANUFACTURER.toUpperCase().equals("MEIZU");
    }
}
