package com.core.lib_core;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;

import com.core.lib_core.constants.CoreConstant;
import com.core.lib_core.utils.NiceLogUtil;
import com.core.lib_core.utils.file.FileUtil;
import com.core.lib_core.utils.file.SharedPreferenceUtil;

import java.io.Serializable;


/**
 * @author miaoxin.ye
 * @createdate 2013-12-14 上午11:02:12
 * @Description: 核心Application
 */
public class CoreApplication extends MultiDexApplication implements Serializable {

    public static final long serialVersionUID = 4656071326644680147L;
    public static Context context;
    public static boolean IS_EXIST_SDCARD;
    public static String CACHE_DIR_SD; // SD卡缓存目录
    public static String CACHE_DIR_SYSTEM; // 系统目录
    public static String IMAGE_DIR; // 图片目录
    public static String FILE_DIR; // 文件目录
    public static String LOG_DIR; // 日志目录
    public static String IMAGE_UPLOAD_TEMP; // 上传图片临时目录
    public static String LOG; // 日志保存的SD卡的目录
    public static String AllLOG;

    private boolean LOGIN_STATE = false;// 登陆状态默认为离线状态 false true:登陆状态

    /**
     * 得到当前用户的登陆状态
     *
     * @return
     */
    public boolean isLOGIN_STATE() {

        LOGIN_STATE = SharedPreferenceUtil.getBoolean(this,
                CoreConstant.LOGIN_STATE);
        return LOGIN_STATE;
        // return LOGIN_STATE;
    }

    /**
     * 设置当前的用户登陆状态
     *
     * @param lOGIN_STATE
     */
    public void setLOGIN_STATE(boolean lOGIN_STATE) {
        SharedPreferenceUtil.saveBoolean(this, CoreConstant.LOGIN_STATE,
                lOGIN_STATE);
        LOGIN_STATE = lOGIN_STATE;
    }

    /**
     * 项目中用到的全局Handler
     */
    protected static Handler mHandler;
    // 关于图片缓存的处理方案以及涉及到的一些常量
    private static CoreApplication coreApp;

    /**
     * 取得单例的核心Applicatio 实例
     **/
    public static CoreApplication getInstance() {
        return coreApp;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = CoreApplication.this;
        coreApp = this;
        onCreateHandler();
    }


    /**
     * @Description: 初始化
     */
    public void initFileDir() {
        if (FileUtil.isExistSD()) {
            // SD存在
            CACHE_DIR_SD = FileUtil.getSDCacheDir(context);
            IS_EXIST_SDCARD = true;
        } else {
            // 不存在则使用系统目录
            CACHE_DIR_SD = context.getCacheDir().getPath();
        }
        CACHE_DIR_SD += "/";
        NiceLogUtil.e("----SD卡目录---->>>:" + CACHE_DIR_SD);
        LOG = CACHE_DIR_SD + "cache.log";
        AllLOG = CACHE_DIR_SD + "allcache.log";
        IMAGE_DIR = CACHE_DIR_SD + "image/";
        FILE_DIR = CACHE_DIR_SD + "file/";
        LOG_DIR = CACHE_DIR_SD + "log/";
        IMAGE_UPLOAD_TEMP = CACHE_DIR_SD + "imageUploadTemp/";
        CACHE_DIR_SYSTEM = context.getCacheDir().getPath() + "/file/";
        FileUtil.checkDir(CACHE_DIR_SD);
        FileUtil.checkDir(IMAGE_DIR);
        FileUtil.checkDir(FILE_DIR);
        FileUtil.checkDir(LOG_DIR);
        // FileUtil.checkDir(IMAGE_UPLOAD_TEMP);
        FileUtil.checkDir(CACHE_DIR_SYSTEM);
        // TerminalUtils.getInstance(getApplicationContext()).initTerminalID();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        //VolleyTool.getInstance(this).release();
        super.onTerminate();
    }

    /**
     * 子类可以根据需要扩展handler的实现
     */
    protected void onCreateHandler() {
        mHandler = new Handler() {
        };
    }

    public static Handler getHandler() {
        return mHandler;
    }
}
