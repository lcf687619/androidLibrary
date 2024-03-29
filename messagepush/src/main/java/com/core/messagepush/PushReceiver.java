package com.core.messagepush;

import android.content.Context;
import android.util.Log;

import com.core.lib_core.activity.HandlerMgr;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class PushReceiver extends XGPushBaseReceiver {

    private static final String TAG = "JPush";

    // 通知展示
    @Override
    public void onNotifactionShowedResult(Context context,
                                          XGPushShowedResult notifiShowedRlt) {
        if (context == null || notifiShowedRlt == null) {
            return;
        }
        Log.d("44444", "推送过来的方法：onNotifactionShowedResult()");
        String customContent = notifiShowedRlt.getContent();
        if (customContent != null && customContent.length() != 0) {
            HandlerMgr.sendMessage(PushMsgID.message_recv, customContent);
        }
//        XGNotification notific = new XGNotification();
//        notific.setMsg_id(notifiShowedRlt.getMsgId());
//        notific.setTitle(notifiShowedRlt.getTitle());
//        notific.setContent(notifiShowedRlt.getContent());
//        // notificationActionType==1为Activity，2为url，3为intent
//        notific.setNotificationActionType(notifiShowedRlt
//                .getNotificationActionType());
//        //Activity,url,intent都可以通过getActivity()获得
//        notific.setActivity(notifiShowedRlt.getActivity());
//        notific.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//                .format(Calendar.getInstance().getTime()));
//        NotificationService.getInstance(context).save(notific);
//        context.sendBroadcast(intent);
//        show(context, "您有1条新消息, " + "通知被展示 ， " + notifiShowedRlt.toString());
//        Log.d("LC", "+++++++++++++++++++++++++++++展示通知的回调");
    }

    //反注册的回调
    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "反注册成功";
        } else {
            text = "反注册失败" + errorCode;
        }
    }

    //设置tag的回调
    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"设置成功";
        } else {
            text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
        }
    }

    //删除tag的回调
    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"删除成功";
        } else {
            text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
        }
    }

    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
    @Override
    public void onNotifactionClickedResult(Context context,
                                           XGPushClickedResult message) {
        Log.d("44444", "推送过来的方法：onNotifactionClickedResult()");
        if (context == null || message == null) {
            return;
        }
        String text = "";
        if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
            // 通知在通知栏被点击啦。。。。。
            // APP自己处理点击的相关动作
            // 这个动作可以在activity的onResume也能监听，请看第3点相关内容
            text = "通知被打开 :" + message;
        } else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
            // 通知被清除啦。。。。
            // APP自己处理通知被清除后的相关动作
            text = "通知被清除 :" + message;
        }
        // 获取自定义key-value
        String customContent = message.getCustomContent();
        String activityName = message.getActivityName();
        Log.d("44444", activityName);
        if (customContent != null && customContent.length() != 0) {
            HandlerMgr.sendMessage(PushMsgID.message_recv, customContent);
        }
    }

    //注册的回调
    @Override
    public void onRegisterResult(Context context, int errorCode,
                                 XGPushRegisterResult message) {
        if (context == null || message == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = message + "注册成功";
            // 在这里拿token
            String token = message.getToken();
        } else {
            text = message + "注册失败，错误码：" + errorCode;
        }
    }

    // 消息透传的回调
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        Log.d("44444", "推送过来的方法：onTextMessage()");
        // 获取自定义key-value
        String customContent = message.getContent();
        if (customContent != null && customContent.length() != 0) {
            HandlerMgr.sendMessage(PushMsgID.message_recv, customContent);
        }
    }
}
