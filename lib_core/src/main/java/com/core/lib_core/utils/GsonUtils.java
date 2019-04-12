package com.core.lib_core.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author 李澄锋<br>
 * 2018/12/27
 */
public class GsonUtils {
    /**
     * 字符串转jsonobject对象
     *
     * @param jsonstr
     * @return
     */
    public static JSONObject strConvertToJson(String jsonstr) {
        if (TextUtils.isEmpty(jsonstr)) {
            return null;
        }
        JSONObject returnData = null;
        try {
            returnData = new JSONObject(jsonstr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnData;
    }

    /**
     * 将字符转换为对象
     *
     * @param jsonstr
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> T strConvertToBean(String jsonstr, Class<T> beanClass) {
        if (TextUtils.isEmpty(jsonstr) || beanClass == null) {
            return null;
        }
        Gson gson = new Gson();
        T bean = gson.fromJson(jsonstr, beanClass);
        return bean;
    }

    /**
     * 将字符串转换为集合
     *
     * @param text
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (text == null) {
            return null;
        }
        Type type = new ParameterizedTypeImpl(clazz);
        Gson gson = new Gson();
        List<T> list = gson.fromJson(text, type);
        return list;
    }

    /**
     * 将字符串转换为json
     *
     * @return
     */
    public static String toJsonString(Object str) {
        if (str == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(str);
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        public ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
