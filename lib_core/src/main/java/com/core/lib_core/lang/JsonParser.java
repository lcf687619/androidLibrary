package com.core.lib_core.lang;

import android.util.Log;

import com.core.lib_core.sqlite.TUtils;
import com.core.lib_core.sqlite.annotation.Column;
import com.core.lib_core.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 重构，优化数据类型的返回
 *
 * @author coffee <br>
 * 2016-1-5下午3:24:21
 */
public class JsonParser {

    /**
     * 默认按照 {@link Column 解析}
     */
    private static boolean byColumn = true;

    /**
     * k : class type <br>
     * v (k : 列(column); v: 字段)
     */
    private static Hashtable<Class<?>, Map<String, Field>> cache = new Hashtable<Class<?>, Map<String, Field>>();

    /**
     * 列明(非字段名)- 对应的字段
     *
     * @param beanClass
     * @return
     */
    private static synchronized Map<String, Field> getColumnMap(Class<?> beanClass) {
        if (cache.get(beanClass) == null || cache.get(beanClass).size() == 0) {
            cache.put(beanClass, new HashMap<String, Field>());
            for (Field field : beanClass.getDeclaredFields()) {
                String columnName = "";
                if (byColumn) {
                    Column column = field.getAnnotation(Column.class);
                    if (column != null) {
                        if (!"".equals(column.json())) {
                            columnName = column.json();
                        } else {
                            columnName = column.name();
                        }
                    } else {
                        columnName = field.getName();
                    }
                } else {
                    columnName = field.getName();
                }
                cache.get(beanClass).put(columnName, field);
            }
        }
        return cache.get(beanClass);
    }

    /**
     * 获取某个属性的值
     *
     * @param jsonStr
     * @param attrs   支持attr[attr[attr]]的格式, 例如 "result[id]"
     */
    public static Object get(String jsonStr, String attrs) {
        try {
            JSONObject json = new JSONObject(jsonStr);
            String[] seg = attrs.replace("]", "").split("\\[");
            if (seg.length == 1) {
                String attr = seg[0];
                if (json.has(attr)) {
                    Object value = json.get(attr);
                    return value;
                } else {
                    return null;
                }
            } else {
                for (int i = 0; i < seg.length; i++) {
                    String attr = seg[i];
                    if (json.has(attr)) {
                        Object value = json.get(attr);
                        if (i < seg.length) {
                            return get(value.toString(), seg[i + 1]);
                        } else {
                            return value;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 不按照 {@link Column}注解解析
     *
     * @param jsonStr
     * @param beanClass
     * @return
     */
    public static <T> T parseWithNoColumn(String jsonStr, Class<T> beanClass) {
        byColumn = false;
        return parse(jsonStr, beanClass);
    }

    /**
     * 不按照 {@link Column}注解解析
     *
     * @param jsonStr
     * @param attr
     * @param beanClass
     * @return
     */
    public static <T> ArrayList<T> parseListWithNoColumn(String jsonStr, String attr, Class<T> beanClass) {
        byColumn = false;
        return parseList(jsonStr, attr, beanClass);
    }

    public static <T> ArrayList<T> parseList(String jsonStr, Class<T> beanClass) {
        try {
            if (jsonStr == null || jsonStr.trim().length() == 0) {
                return null;
            }
            JSONArray json = new JSONArray(jsonStr);
            ArrayList<T> arrayObj = parse(json, beanClass);
            return arrayObj;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将jsonStr的attr属性值解析成ArrayList<BeanClass>
     *
     * @param jsonStr
     * @param attr      如果需要解析整个jsonStr 则该属性设置为 null 或者 ""
     * @param beanClass
     * @return
     */
    public static <T> ArrayList<T> parseList(String jsonStr, String attr, Class<T> beanClass) {
        try {
            if (jsonStr == null || jsonStr.trim().length() == 0) {
                return null;
            }
            if (attr == null || attr.trim().length() == 0) {
                JSONArray jsonArray = new JSONArray(jsonStr);
                ArrayList<T> arrayObj = parse(jsonArray, beanClass);
                return arrayObj;
            } else {
                JSONObject json = new JSONObject(jsonStr);
                if (json.has(attr)) {
                    ArrayList<T> arrayObj = parse(json.getJSONArray(attr), beanClass);
                    return arrayObj;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param jsonStr
     * @param attr
     * @param valueType
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> HashMap<String, T> parseMap(String jsonStr, String attr, Class<T> valueType) {
        try {
            if (jsonStr == null || jsonStr.trim().length() == 0) {
                return null;
            }
            JSONObject json = new JSONObject(jsonStr);
            String tmpStr = json.getString(attr);
            JSONObject mapJson = new JSONObject(tmpStr);
            Object map = parse(mapJson, String.class, valueType);
            return (HashMap<String, T>) map;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param jsonStr
     * @param valueType
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> HashMap<String, T> parseMap(String jsonStr, Class<T> valueType) {
        try {
            if (jsonStr == null || jsonStr.trim().length() == 0) {
                return null;
            }
            JSONObject mapJson = new JSONObject(jsonStr);
            Object map = parse(mapJson, String.class, valueType);
            return (HashMap<String, T>) map;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将指定json字符串的指定节点的内容转换成beanClass对象
     *
     * @param jsonStr
     * @param attr
     * @param beanClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T parse(String jsonStr, String attr, Class<T> beanClass) {
        try {
            if (jsonStr == null || jsonStr.trim().length() == 0) {
                return null;
            }
            JSONObject json = new JSONObject(jsonStr);
            if (json.has(attr)) {
                if (beanClass == String.class) {
                    String result = json.getString(attr);
                    return (T) result;
                } else if (beanClass == Integer.class) {
                    Integer result = json.getInt(attr);
                    return (T) result;
                } else if (beanClass == HashMap.class) {
                    String tmpStr = json.getString(attr);
                    JSONObject mapJson = new JSONObject(tmpStr);
                    Type[] types = beanClass.getGenericInterfaces();
                    Class<?> keyClass = TUtils.type2Class(types[0]);
                    Class<?> valueClass = TUtils.type2Class(types[1]);
                    Object map = parse(mapJson, keyClass, valueClass);
                    return (T) map;
                } else {
                    JSONObject attrObj = json.getJSONObject(attr);
                    T t;
                    try {
                        t = GsonUtils.strConvertToBean(attrObj.toString(), beanClass);
                    } catch (Exception e) {
                        e.printStackTrace();
                        t = parse(attrObj, beanClass);
                    }
                    return t;
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将指定的Json字符串解析成beanClass对象
     *
     * @param jsonStr
     * @param beanClass
     * @return
     */
    public static <T> T parse(String jsonStr, Class<T> beanClass) {
        try {
            if (jsonStr == null || jsonStr.trim().length() == 0) {
                return null;
            }
            Log.d("json:parse", beanClass + "---enter---");
            JSONObject json = new JSONObject(jsonStr);
            return parse(json, beanClass);
        } catch (Exception e) {
            Log.w("json:parser", e.getMessage(), e);
        } finally {
            Log.d("json:parse", beanClass + "---end---");
            // 清空缓存
            // cache.remove(beanClass);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T> T parse(JSONObject json, Class<T> beanClass) {
        try {
            T obj = beanClass.newInstance();
            // 保存 JSONObject中的键值对
            Map<String, Object> items = new HashMap<String, Object>();
            for (Iterator<String> it = json.keys(); it.hasNext(); ) {
                String key = it.next();
                // 不处理null值
                if (json.isNull(key)) {
                    continue;
                }
                items.put(key, json.get(key));
            }
            for (String columnName : getColumnMap(beanClass).keySet()) {
                // 如果JSONObject中不包含 columnName 则不做映射
                if (json.has(columnName) == false) {
                    continue;
                }
                Field field = cache.get(beanClass).get(columnName);
                Object value = items.get(columnName);
                // 创建ArrayList
                if (field.getType() == ArrayList.class || field.getType() == List.class) {
                    try {
                        Type arrType = field.getGenericType();
                        Class<?> arrClass = TUtils.type2Class(arrType);
                        if (arrClass == null) {
                            obj = GsonUtils.strConvertToBean(json.toString(), beanClass);
                        } else {
                            Object arrayObj = parse(json.getJSONArray(columnName), arrClass);
                            field.setAccessible(true);
                            field.set(obj, arrayObj);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (field.getType() == HashMap.class) {
                    // json 转换为map
                    Type mapMainType = field.getGenericType();
                    ParameterizedType parameterizedType = (ParameterizedType) mapMainType;
                    Type[] types = parameterizedType.getActualTypeArguments();
                    JSONObject mapJson = json.getJSONObject(columnName);
                    Class<?> keyClass = TUtils.type2Class(types[0]);
                    Class<?> valueClass = TUtils.type2Class(types[1]);
                    //
                    Object map = parse(mapJson, keyClass, valueClass);
                    TUtils.setValue(obj, field.getName(), map);
                } else {
                    if (value != null) {
                        // 递归
                        if (value instanceof JSONObject) {
                            value = parse((JSONObject) value, field.getType());
                            TUtils.setValue(obj, field.getName(), value);
                        }
                        // JSONArray 转化为 String[]
                        else if (value instanceof JSONArray) {
                            JSONArray arr = (JSONArray) value;
                            List<String> list = new ArrayList<String>();
                            for (int i = 0; i < arr.length(); i++) {
                                list.add(arr.getString(i));
                            }
                            TUtils.setValue(obj, field.getName(), list.toArray(new String[]{}));
                        } else {
                            TUtils.setValue(obj, field.getName(), value);
                        }
                    }
                }
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> ArrayList<T> parse(JSONArray jsonArr, Class<T> beanClass) {
        ArrayList<T> lst = new ArrayList<T>();
        for (int i = 0; i < jsonArr.length(); i++) {
            try {
                if (beanClass.getName().equals(String.class.getName())) {
                    lst.add((T) jsonArr.get(i));
                } else {
                    JSONObject jsonObj = jsonArr.getJSONObject(i);
                    lst.add(parse(jsonObj, beanClass));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return lst;
    }

    /**
     * @param mapJson
     * @param keyClass   Map key的数据类型
     * @param valueClass Map value的数据类型
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <K> Map<String, K> parse(JSONObject mapJson, Class<?> keyClass, Class<K> valueClass) {
        Iterator<String> keys = mapJson.keys();
        Map<String, K> map = new HashMap<String, K>();
        while (keys.hasNext()) {
            String key = keys.next();
            K p = null;
            try {
                p = (K) mapJson.get(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            map.put(key, p);
        }
        return map;
    }

    /**
     * map转化成json字符串
     *
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, V> String toJson(Map<T, V> map) {
        if (map.size() == 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Iterator<T> it = map.keySet().iterator(); it.hasNext(); ) {
            T key = it.next();
            V val = map.get(key);
            if (val == null) {
                continue;
            }
            if (key instanceof Number) {
                sb.append(key);
            } else {
                sb.append("\"").append(key).append("\"");
            }
            sb.append(":");
            if (val instanceof Map) {
                sb.append(toJson((Map<T, V>) val));
            } else if (val instanceof Number) {
                sb.append(val);
            } else {
                sb.append("\"").append(val).append("\"");
            }
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
