package com.core.lib_core.lang;


import com.core.lib_core.constants.CoreConstant;
import com.core.lib_core.http.ReqResult;

import java.util.ArrayList;
import java.util.HashMap;

public class JSON {

    @SuppressWarnings("unchecked")
    public static <T> ReqResult<T> parser(Object jsonStr, Class<T> resultType) {
        if (jsonStr == null) {
            return new ReqResult<T>();
        }
        ReqResult<T> reqResult = JsonParser.parse(jsonStr + "", ReqResult.class);
        if (reqResult == null) {
            return new ReqResult<T>();
        } else {
            T obj = JsonParser.parse(jsonStr + "", CoreConstant.RESULT_NAME, resultType);
            reqResult.setData(obj);
            return reqResult;
        }
    }

    public static ReqResult<?> parser(Object jsonStr) {
        if (jsonStr == null) {
            return new ReqResult<Object>();
        }
        ReqResult<?> reqResult = JsonParser.parse(jsonStr + "", ReqResult.class);
        if (reqResult == null) {
            reqResult = new ReqResult<Object>();
        }
        return reqResult;
    }

    @SuppressWarnings("unchecked")
    public static <T> ReqResult<T> parseList(Object jsonStr, Class<T> resultType) {
        if (jsonStr == null) {
            return new ReqResult<T>();
        }
        ReqResult<T> reqResult = JsonParser.parse(jsonStr + "", ReqResult.class);
        if (reqResult == null) {
            return new ReqResult<T>();
        } else {
            ArrayList<T> list = JsonParser.parseList(jsonStr + "", CoreConstant.RESULT_NAME, resultType);
            reqResult.setResultList(list);
            return reqResult;
        }
    }

    public static <T> ReqResult<T> parserMap(Object jsonStr, Class<T> resultType) {
        if (jsonStr == null) {
            return new ReqResult<T>();
        }
        ReqResult<T> reqResult = JsonParser.parse(jsonStr + "", ReqResult.class);
        if (reqResult == null) {
            return new ReqResult<T>();
        } else {
            HashMap<String, T> map = JsonParser.parseMap(jsonStr + "", "data", resultType);
            reqResult.setResultMap(map);
            return reqResult;
        }
    }

}
