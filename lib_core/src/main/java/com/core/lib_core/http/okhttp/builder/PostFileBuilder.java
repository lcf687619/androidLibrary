package com.core.lib_core.http.okhttp.builder;


import com.core.lib_core.http.okhttp.request.PostFileRequest;
import com.core.lib_core.http.okhttp.request.RequestCall;

import java.io.File;

import okhttp3.MediaType;

/**
 * @author 李澄锋
 */
public class PostFileBuilder extends OkHttpRequestBuilder<PostFileBuilder>
{
    private File file;
    private MediaType mediaType;


    public OkHttpRequestBuilder file(File file)
    {
        this.file = file;
        return this;
    }

    public OkHttpRequestBuilder mediaType(MediaType mediaType)
    {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build()
    {
        return new PostFileRequest(url, tag, params, headers, file, mediaType,id).build();
    }


}
