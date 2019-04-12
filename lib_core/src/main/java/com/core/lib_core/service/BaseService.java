package com.core.lib_core.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.core.lib_core.R;
import com.core.lib_core.widgets.Alert;

/**
 * Created by joker on 2018/5/9.
 */
@SuppressLint("NewApi")
public abstract class BaseService extends Service implements Handler.Callback {

    private String CHANNEL_ID_LOCATION = "com.goxueche";

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceMgr.push(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_LOCATION+getServiceName(), getServiceName(),
                    NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(this).setChannelId(CHANNEL_ID_LOCATION+getServiceName())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("正在运行中")
                    .build();
            startForeground(1, notification);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    /**
     * 项目中用到的通用toast<br>
     * 子类可以根据需要重载覆盖
     */
    public void showToast(final String message) {
        Alert.toast(message);
    }

    public void showToast(final int message) {
        Alert.toast(message);
    }

    @Override
    public void onDestroy() {
        ServiceMgr.remove(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public abstract String getServiceName();
}
