package com.core.lib_core.constants;

/**
 * @author miaoxin.ye
 * @createdate 2013-12-16 下午6:54:35
 * @Description: 常量
 */
public class CoreConstant {
    public static boolean IS_TEST_FLAG = true;        //日志打印,试运行环境、测试环境为true,生产环境为false
    public static final String PREFERENCE_NAME = "goxueche_sp"; //SharedPreference文件名
    //缓存文件目录0为system目录,1为SD卡目录
    public static final int CACHE_DIR_SYSTEM = 0;
    public static final int CACHE_DIR_SD = 1;

    public static String RESULT_NAME="data";

    //用户的登陆状态
    public static final String LOGIN_STATE = "LOGIN_STATE";

    public static final String PACKAGE_NAME = "com.core";

    public static final int UPLOAD_PICTURE_TAKE_HEAD = 51;//头像拍照
    public static final int UPLOAD_PICTURE_HEAD = 50;//头像从手机选择照片
    public static final int UPLOAD_PICTURE_CROP = 53;//裁剪
    public static final int UPLOAD_PICTURE_GALLERY = 55;//从相册选择不裁剪
    public static final int UPLOAD_CARD_TAKEPHOTO = 10001;//身份证拍照
    public static final int UPLOAD_CARD_GALLERY = 10002;//身份证从手机选择照片
    public static final int UPLOAD_PICTURE_TAKE = 10003;//直接拍照

}
