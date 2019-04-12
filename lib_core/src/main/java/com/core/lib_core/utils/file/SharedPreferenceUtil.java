package com.core.lib_core.utils.file;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import com.core.lib_core.constants.CoreConstant;
import com.core.lib_core.utils.NiceLogUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * @author miaoxin.ye
 * @createdate 2013-12-14 上午11:09:20
 * @Description: SharedPreference工具类
 */
public class SharedPreferenceUtil {

    /**
     * @param context
     * @param key
     * @param value
     * @Description: 保存int值
     */
    public static void saveInt(Context context, String key, int value) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(CoreConstant.PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * @param context
     * @param key
     * @return
     * @Description: 获取int值
     */
    public static int getInt(Context context, String key) {
        if (context == null) {
            return 0;
        }
        SharedPreferences shared = context.getSharedPreferences(CoreConstant.PREFERENCE_NAME, Context.MODE_PRIVATE);
        int value = shared.getInt(key, 0);
        return value;
    }

    /**
     * @param context
     * @param key
     * @param value
     * @Description: 保存long值
     */
    public static void saveLong(Context context, String key, long value) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(CoreConstant.PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * @param context
     * @param key
     * @return
     * @Description: 获取long值
     */
    public static long getLong(Context context, String key) {
        if (context == null) {
            return 0L;
        }
        SharedPreferences shared = context.getSharedPreferences(CoreConstant.PREFERENCE_NAME, Context.MODE_PRIVATE);
        long value = shared.getLong(key, 0L);
        return value;
    }

    /**
     * @param context
     * @param key
     * @param value
     * @Description: 保存boolean值
     */
    public static void saveBoolean(Context context, String key, boolean value) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(CoreConstant.PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * @param context
     * @param key
     * @return
     * @Description: 获取boolean值
     */
    public static boolean getBoolean(Context context, String key) {
        if (context == null) {
            return false;
        }
        SharedPreferences shared = context.getSharedPreferences(CoreConstant.PREFERENCE_NAME, Context.MODE_PRIVATE);
        boolean value = shared.getBoolean(key, false);
        return value;
    }

    /**
     * @param context
     * @param key
     * @param value
     * @Description: 保存String值
     */
    public static void saveString(Context context, String key, String value) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(CoreConstant.PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * @param context
     * @param key
     * @return
     * @Description: 获取String值
     */
    public static String getString(Context context, String key) {
        if (context == null) {
            return "";
        }
        SharedPreferences shared = context.getSharedPreferences(CoreConstant.PREFERENCE_NAME, Context.MODE_PRIVATE);
        String value = shared.getString(key, "");
        return value;
    }

    /**
     * @param context
     * @param key
     * @Description: 清空保存的值
     */
    public static void removeString(Context context, String key) {
        if (context == null) {
            return;
        }
        SharedPreferences shared = context.getSharedPreferences(CoreConstant.PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = shared.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 根据key和预期的value类型获取value的值
     *
     * @param key
     * @param clazz
     * @return
     */
    public <T> T getValue(Context context, String key, Class<T> clazz) {
        if (context == null) {
            throw new RuntimeException("请先调用带有context，name参数的构造！");
        }
        SharedPreferences shared = context.getSharedPreferences(CoreConstant.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return getValue(key, clazz, shared);
    }

    /**
     * 针对复杂类型存储<对象>
     *
     * @param key
     * @param object
     */
    public void setObject(Context context, String key, Object object) {
        if (context == null) {
            throw new RuntimeException("请先调用带有context，name参数的构造！");
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstant.PREFERENCE_NAME, Context.MODE_PRIVATE);
        //创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //创建字节对象输出流
        ObjectOutputStream out = null;
        try {
            //然后通过将字对象进行64转码，写入key值为key的sp中
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, objectVal);
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(Context context, String key, Class<T> clazz) {
        if (context == null) {
            throw new RuntimeException("请先调用带有context，name参数的构造！");
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstant.PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(key)) {
            String objectVal = sharedPreferences.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            //一样通过读取字节流，创建字节流输入流，写入对象并作强制转换
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 对于外部不可见的过渡方法
     *
     * @param key
     * @param clazz
     * @param sp
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> T getValue(String key, Class<T> clazz, SharedPreferences sp) {
        T t;
        try {

            t = clazz.newInstance();

            if (t instanceof Integer) {
                return (T) Integer.valueOf(sp.getInt(key, 0));
            } else if (t instanceof String) {
                return (T) sp.getString(key, "");
            } else if (t instanceof Boolean) {
                return (T) Boolean.valueOf(sp.getBoolean(key, false));
            } else if (t instanceof Long) {
                return (T) Long.valueOf(sp.getLong(key, 0L));
            } else if (t instanceof Float) {
                return (T) Float.valueOf(sp.getFloat(key, 0L));
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
            NiceLogUtil.e("system 类型输入错误或者复杂类型无法解析[" + e.getMessage() + "]");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            NiceLogUtil.e("system 类型输入错误或者复杂类型无法解析[" + e.getMessage() + "]");
        }
        NiceLogUtil.e("system 无法找到" + key + "对应的值");
        return null;
    }
}
