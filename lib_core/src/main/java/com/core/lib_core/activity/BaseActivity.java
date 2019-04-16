package com.core.lib_core.activity;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.core.lib_core.R;
import com.core.lib_core.api.BaseApiHandler;
import com.core.lib_core.api.BaseRequestApi;
import com.core.lib_core.constants.CoreConstant;
import com.core.lib_core.http.MsgID;
import com.core.lib_core.title.AppTitle;
import com.core.lib_core.title.TitleMgr;
import com.core.lib_core.utils.CameraUtils;
import com.core.lib_core.widgets.AppDialog;
import com.core.lib_core.widgets.viewhelper.VaryViewHelper;
import com.finalteam.galleryfinal.utils.GetImagePath;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * @author 李澄锋<br>
 * 2018/8/13
 */
public abstract class BaseActivity extends FrameBaseActivity {
    private AppTitle appTitle;
    private View waitingView;
    protected CameraUtils cameraUtils = null;
    public VaryViewHelper mVaryViewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setStatusBarColor(R.color.app_status);
        cameraUtils = new CameraUtils(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findViewById() {
        appTitle = new TitleMgr(getContext());
        appTitle.initTitle();
        View contentView = findViewById(R.id.core_content_layout);
        if (contentView != null) {
            initVaryView();
        }
    }

    protected void initVaryView() {
        if (loadingview() <= 0 || emptyview() <= 0 || errorview() <= 0) {
            return;
        }
        mVaryViewHelper = new VaryViewHelper.Builder()
                .setDataView(findViewById(R.id.core_content_layout))//放数据的父布局，逻辑处理在该Activity中处理
                .setLoadingView(LayoutInflater.from(this).inflate(loadingview(), null))//加载页，无实际逻辑处理
                .setEmptyView(LayoutInflater.from(this).inflate(emptyview(), null))//空页面，无实际逻辑处理
                .setErrorView(LayoutInflater.from(this).inflate(errorview(), null))//错误页面
                .setRefreshListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reloadData();
                    }
                })//错误页点击刷新实现
                .build();
    }

    public abstract int loadingview();

    public abstract int emptyview();

    public abstract int errorview();

    public void reloadData() {
        if (mVaryViewHelper != null) {
            mVaryViewHelper.showLoadingView();
        }
    }

    public AppTitle getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(AppTitle appTitle) {
        this.appTitle = appTitle;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MsgID.net_unavailable:
                dissmissWaitingDialog();
                showToast("网络连接异常,请检查网络设置");
                return false;
            default:
                return super.handleMessage(msg);
        }
    }

    /**
     * 显示加载对话框
     */
    @Override
    public Dialog showWaitDialog(final boolean cancelable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null == dialog) {
                    dialog = new Dialog(getContext(), R.style.dialog_transparent);
                    waitingView = getLayoutInflater().inflate(
                            R.layout.circular_progress, null);
                    waitingView.invalidate();
                    dialog.setContentView(waitingView);
                }
                try {
                    dialog.setCancelable(cancelable);
                    dialog.show();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
        return dialog;
    }

    public void showPermissionDialog(String message) {
        showPermissionDialog(message, null);
    }

    public void showPermissionDialog(String message, final onPermissionDialogCancelListener listener) {
        final AppDialog dialog = new AppDialog(this);
        dialog.setTitle("权限申请");
        dialog.setContent(message);
        dialog.setConfirmClickListener("去设置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent appDetailSettingIntent = getAppDetailSettingIntent(getContext());
                startActivity(appDetailSettingIntent);
                if (listener != null) {
                    listener.onOk();
                }
            }
        });
        dialog.setCancelClickListener("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onCancel();
                }
            }
        });
        dialog.show();
    }

    public interface onPermissionDialogCancelListener {
        void onCancel();

        void onOk();
    }

    private Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", this.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", this.getPackageName());
        }
        return localIntent;
    }

    /*------------------------------------------------相机的相关方法（begin）---------------------------------------------------------------------*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CoreConstant.UPLOAD_PICTURE_TAKE_HEAD:// 头像拍照返回
                if (resultCode != RESULT_OK) return;
                cameraUtils.cropImageUri(cameraUtils.getUri(), 1, 1, 500, 500, CoreConstant.UPLOAD_PICTURE_CROP);
                break;
            case CoreConstant.UPLOAD_PICTURE_HEAD:// 头像从手机相册选择
                if (resultCode != RESULT_OK) return;
                cameraUtils.cropImageUri(data.getData(), 1, 1, 500, 500, CoreConstant.UPLOAD_PICTURE_CROP);
                break;
            case CoreConstant.UPLOAD_PICTURE_TAKE:
                if (resultCode != RESULT_OK) {
                    return;
                }
                if (cameraUtils.getUri() == null) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    File file = GetImagePath.getFileForUri(this, cameraUtils.getUri());
                    if (file.exists()) {
                        cameraUtils.setPhotoPath(file.getPath());
                    }
                } else {
                    cameraUtils.setPhotoPath(cameraUtils.getUri().getPath());
                }
                if (TextUtils.isEmpty(cameraUtils.getPhotoPath()) && new File(cameraUtils.getPhotoPath()).exists()) {
                    return;
                }
                cameraUtils.getPath();
                break;
            case CoreConstant.UPLOAD_PICTURE_CROP://裁剪返回
                if (resultCode != RESULT_OK) return;
                if (cameraUtils.getUri() == null) {
                    return;
                }
                String uriStr = cameraUtils.getUri().toString();
                if (uriStr.startsWith("file")) {
                    cameraUtils.setPhotoPath(cameraUtils.getUri().getPath());
                    cameraUtils.getPath();
                } else if (uriStr.startsWith("content")) {
                    cameraUtils.setPhotoPath(cameraUtils.getUri().getPath());
                    cameraUtils.getUriToPath(data);
                }
                break;

            case CoreConstant.UPLOAD_PICTURE_GALLERY://从相册选择不裁剪
                if (resultCode != RESULT_OK) return;
                cameraUtils.getUriToPath(data);
                break;
            case CoreConstant.UPLOAD_CARD_TAKEPHOTO:
                if (resultCode != RESULT_OK) return;
                cameraUtils.cropImageUri(cameraUtils.getUri(), 130, 81, 1300, 810, CoreConstant.UPLOAD_PICTURE_CROP);
                break;
            case CoreConstant.UPLOAD_CARD_GALLERY:
                if (resultCode != RESULT_OK) return;
                cameraUtils.cropImageUri(data.getData(), 130, 81, 1300, 810, CoreConstant.UPLOAD_PICTURE_CROP);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*------------------------------------------------相机的相关方法（end）---------------------------------------------------------------------*/
    public void startActivity(Class<?> classActivity) {
        Intent intent = new Intent();
        intent.setClass(getContext(), classActivity);
        startActivity(intent);
    }

    public void startService(Class<?> toServiceClass) {
        Intent intent = new Intent();
        intent.setClass(getContext(), toServiceClass);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    public void startService(Class<?> toServiceClass, String action, Map<String, String> paramsMap) {
        Intent intent = new Intent();
        intent.setClass(getContext(), toServiceClass);
        if (!TextUtils.isEmpty(action)) {
            intent.setAction(action);
        }
        if (paramsMap != null && paramsMap.size() > 0) {
            Iterator<Map.Entry<String, String>> entries = paramsMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                if (!TextUtils.isEmpty(entry.getKey()) && !TextUtils.isEmpty(entry.getValue())) {
                    intent.putExtra(entry.getKey(), entry.getValue());
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    /**
     * 拷贝到剪贴板
     *
     * @param content 需要拷贝的内容
     * @description 2015年12月31日 12:14:58
     * @author sufun
     */
    public void doCopyToPaste(String content) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(content);
    }

    @Override
    protected void onDestroy() {
        mVaryViewHelper = null;
        super.onDestroy();
    }

    protected void handleTokenInvalid() {
        if (getApiHandler() != null) {
            getApiHandler().handleTokenInvalid();
        }
    }

    public abstract String getToken();

    public abstract BaseRequestApi getRequestApi();

    public abstract BaseApiHandler getApiHandler();
}
