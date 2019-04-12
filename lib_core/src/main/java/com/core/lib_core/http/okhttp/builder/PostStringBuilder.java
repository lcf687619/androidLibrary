package com.core.lib_core.http.okhttp.builder;

import com.core.lib_core.http.okhttp.request.PostStringRequest;
import com.core.lib_core.http.okhttp.request.RequestCall;

import okhttp3.MediaType;

/**
 * @author 李澄锋
 */
public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder>
{
    private String content;
    private MediaType mediaType;


    public PostStringBuilder content(String content)
    {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType)
    {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build()
    {
        return new PostStringRequest(url, tag, params, headers, content, mediaType,id).build();
    }


}
