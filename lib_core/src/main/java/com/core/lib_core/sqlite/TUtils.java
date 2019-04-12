package com.core.lib_core.sqlite;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Locale;

/**
 * 反射用到的工具
 *
 * @author lichengfeng
 */
public class TUtils {
    /**
     * 判断给定的field名是否存在于指定的class
     *
     * @param fieldName
     * @return
     */
    public static boolean isField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if (field != null) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 获取泛型的对象类型
     *
     * @param type java.util.ArrayList<com.xxx.bean.AddressListBean$AddressBean>
     * @return com.xxx.bean.AddressListBean.class
     */
    public static Class<?> type2Class(Type type) {
        //
        String typeStr = type.toString();
        String tmpStr = null;
        if (typeStr.indexOf("<") > 0) {
            tmpStr = typeStr.substring(typeStr.indexOf("<") + 1, typeStr.lastIndexOf(">"));
        } else {
            // 非内部类 ???
            tmpStr = typeStr.substring(6);
        }
        Class<?> clazz = null;
        try {
            clazz = Class.forName(tmpStr);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    /**
     * 获取某个字段的值
     */
    public static <T> Object getValue(T obj, Field field) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object setValue(Object obj, String fieldName, Object value) {
        try {
            if (obj == null || value == null) {
                return obj;
            }
            Field field = obj.getClass().getDeclaredField(fieldName);
            if (field != null) {
                Object newVal = value;
                if (field.getType().isPrimitive()) {
                    String type = field.getType().toString();
                    if (type.contains("long")) {
                        if (TextUtils.isEmpty(value + "")) {
                            newVal = 0;
                        } else {
                            newVal = Long.valueOf(value + "");
                        }
                    } else if (type.contains("int")) {
                        if (TextUtils.isEmpty(value + "")) {
                            newVal = 0;
                        } else {
                            newVal = Integer.valueOf(value + "");
                        }
                    } else if (type.contains("float")) {
                        if (TextUtils.isEmpty(value + "")) {
                            newVal = 0;
                        } else {
                            newVal = Float.valueOf(value + "");
                        }
                    } else if (type.contains("double")) {
                        if (TextUtils.isEmpty(value + "")) {
                            newVal = 0;
                        } else {
                            newVal = Double.valueOf(value + "");
                        }
                    } else if (type.contains("boolean")) {
                        newVal = isBooleanValue(value + "");
                    } else if (type.contains("char")) {
                        if (TextUtils.isEmpty(value + "")) {
                            newVal = "";
                        } else {
                            newVal = Character.valueOf(value.toString().charAt(0));
                        }
                    } else if (type.contains("byte")) {
                        if (TextUtils.isEmpty(value + "")) {
                            newVal = "";
                        } else {
                            newVal = Byte.valueOf(value + "");
                        }
                    } else if (type.contains("short")) {
                        if (TextUtils.isEmpty(value + "")) {
                            newVal = 0;
                        } else {
                            newVal = Short.valueOf(value + "");
                        }
                    }
                } else {
                    if (field.getType() == BigDecimal.class) {
                        if (TextUtils.isEmpty(value + "")) {
                            newVal = 0;
                        } else {
                            newVal = BigDecimal.valueOf(Double.valueOf(value + ""));
                        }
                    } else if (field.getType() == String.class) {
                        if (TextUtils.isEmpty(value + "")) {
                            newVal = "";
                        } else {
                            newVal = String.valueOf(value);
                        }
                    } else if (field.getType() == Long.class) {
                        if (TextUtils.isEmpty(value + "")) {
                            newVal = null;
                        } else {
                            newVal = Long.valueOf(value + "");
                        }
                    } else if (field.getType() == Integer.class) {
                        if (TextUtils.isEmpty(value + "")) {
                            newVal = null;
                        } else {
                            newVal = Integer.valueOf(value + "");
                        }
                    } else if (field.getType() == Float.class) {
                        if (TextUtils.isEmpty(value + "")) {
                            newVal = null;
                        } else {
                            newVal = Float.valueOf(value + "");
                        }
                    } else if (field.getType() == Double.class) {
                        if (TextUtils.isEmpty(value + "")) {
                            newVal = null;
                        } else {
                            newVal = Double.valueOf(value + "");
                        }
                    } else if (field.getType() == Boolean.class) {
                        newVal = isBooleanValue(value + "");
                    } else if (field.getType() == Character.class) {
                        if (TextUtils.isEmpty(value + "")) {
                            newVal = null;
                        } else {
                            newVal = Character.valueOf(value.toString().charAt(0));
                        }
                    } else if (field.getType() == Byte.class) {
                        if (TextUtils.isEmpty(value + "")) {
                            newVal = null;
                        } else {
                            newVal = Byte.valueOf(value + "");
                        }
                    } else if (field.getType() == Short.class) {
                        if (TextUtils.isEmpty(value + "")) {
                            newVal = null;
                        } else {
                            newVal = Short.valueOf(value + "");
                        }
                    }
                }
                String methodName = "set" + fieldName.substring(0, 1).toUpperCase(Locale.getDefault()) + fieldName.substring(1);
                Method method = obj.getClass().getMethod(methodName, new Class[]{field.getType()});
                method.invoke(obj, newVal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 获取boolean类型得值是否为true
     *
     * @return
     */
    private static boolean isBooleanValue(String value) {
        if (!TextUtils.isEmpty(value)) {
            if ("1".equals(value) || "yes".equals(value)) {
                return true;
            } else {
                return Boolean.valueOf(value);
            }
        }
        return false;
    }
}
