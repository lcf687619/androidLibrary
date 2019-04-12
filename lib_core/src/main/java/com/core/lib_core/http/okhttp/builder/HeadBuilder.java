package com.core.lib_core.http.okhttp.builder;


import com.core.lib_core.http.okhttp.OkHttpUtils;
import com.core.lib_core.http.okhttp.request.OtherRequest;
import com.core.lib_core.http.okhttp.request.RequestCall;

/**
 * @author 李澄锋
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
