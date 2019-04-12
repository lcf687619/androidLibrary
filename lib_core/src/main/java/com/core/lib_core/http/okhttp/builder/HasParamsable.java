package com.core.lib_core.http.okhttp.builder;

import java.util.Map;

/**
 * @author 李澄锋
 */
public interface HasParamsable
{
    OkHttpRequestBuilder params(Map<String, String> params);
    OkHttpRequestBuilder addParams(String key, String val);
}
