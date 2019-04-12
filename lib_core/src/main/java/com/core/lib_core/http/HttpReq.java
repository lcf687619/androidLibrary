package com.core.lib_core.http;

import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * http请求
 */
public class HttpReq {
    /**
     * 每个请求都有唯一标示, 通过该id进行Message发送，刷新界面
     */
    private int msgId;
    /**
     * 该参数对应{@link Message} 的 arg1 arg2
     */
    private int arg1, arg2;

    /**
     * 请求状态, 加载成功1,加载中0,加载失败-1
     */
    private int state;
    /**
     * 请求链接(如果是文本请求, 该参数可能为空)
     */
    private String url;
    /**
     * 请求的标记，为了之后可以关闭请求
     */
    private Object tag;
    /**
     * 框架中用到的预留的key {@link HttpParam}
     */
    private Map<String, String> params;
    /**
     * 框架中用到的上传文件参数
     */
    private Map<String, String> pathList;
    private String uploadFileParamsName;
    /**
     * 图片、apk等
     */
    private int type;


    /**
     * 与该http相关
     */
    private WeakReference<HttpClient> httpClient;
    /**
     * 如果该http请求需要特殊处理的话，可以扩展http加载类
     */
    private HttpRunnable httpRunnable;
    /**
     * http请求的回调
     */
    private HttpCallback httpCallback;

    /**
     * @param url
     * @param httpCallback
     */
    public HttpReq(String url, HttpCallback httpCallback) {
        this.url = url;
        this.httpCallback = httpCallback;
    }

    /**
     * 文本请求
     *
     * @param params
     * @param messageId
     */
    public HttpReq(Map<String, String> params, int messageId) {
        this.params = params;
        this.type = HttpLoader.TYPE_TEXT;
        this.msgId = messageId;
    }

    /**
     * 文件和文本请求
     *
     * @param params
     * @param messageId
     */
    public HttpReq(Map<String, String> params, Map<String, String> pathList, int messageId, String uploadFileParamsName) {
        this.params = params;
        this.uploadFileParamsName = uploadFileParamsName;
        this.pathList = pathList;
        this.type = HttpLoader.TYPE_upload;
        this.msgId = messageId;
    }

    public HttpReq(Map<String, String> params, int messageId, HttpCallback httpCallback) {
        this(params, messageId);
        this.httpCallback = httpCallback;
    }

    /**
     * @param paramsName {@link HttpParam}
     * @return
     */
    public boolean getParam(String paramsName) {
        if (params != null) {
            return "true".equals(params.get(paramsName));
        }
        return false;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getArg1() {
        return arg1;
    }

    public void setArg1(int arg1) {
        this.arg1 = arg1;
    }

    public int getArg2() {
        return arg2;
    }

    public void setArg2(int arg2) {
        this.arg2 = arg2;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getPathList() {
        return pathList;
    }

    public void setPathList(Map<String, String> pathList) {
        this.pathList = pathList;
    }

    public String getUploadFileParamsName() {
        return uploadFileParamsName;
    }

    public void setUploadFileParamsName(String uploadFileParamsName) {
        this.uploadFileParamsName = uploadFileParamsName;
    }

    public WeakReference<HttpClient> getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = new WeakReference<HttpClient>(httpClient);
    }

    public HttpRunnable getHttpRunnable() {
        return httpRunnable;
    }

    public void setHttpRunnable(HttpRunnable httpRunnable) {
        this.httpRunnable = httpRunnable;
    }

    public HttpCallback getHttpCallback() {
        return httpCallback;
    }

    public void setHttpCallback(HttpCallback httpCallback) {
        this.httpCallback = httpCallback;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    /**
     * {@link HttpLoader#TYPE_TEXT}请求除了比较Url, 还需要比较msgId<br>
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HttpReq other = (HttpReq) obj;
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }
        if (this.type == HttpLoader.TYPE_TEXT) {
            if (msgId != other.msgId) {
                return false;
            }
        }
        return true;
    }

}
