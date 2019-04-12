package com.core.lib_core.http;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * http接口解析
 *
 * @param <T>2015-4-17下午6:26:22
 * @author 李澄锋<br>
 */
public class ReqResult<T> {

    private static final long serialVersionUID = 2811605260757072329L;

    private int code;
    private String success;
    private String msg = "服务器异常";
    private T data;
    private ArrayList<T> resultList;// 结果列表
    private HashMap<String, T> resultMap;//返回map类型

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ArrayList<T> getResultList() {
        if (resultList == null) {
            resultList = new ArrayList<T>();
        }
        return resultList;
    }

    public void setResultList(ArrayList<T> resultList) {
        this.resultList = resultList;
    }

    public HashMap<String, T> getResultMap() {
        return resultMap;
    }

    public void setResultMap(HashMap<String, T> resultMap) {
        this.resultMap = resultMap;
    }
}
