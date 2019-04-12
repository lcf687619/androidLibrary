package com.core.lib_core.http.okhttp.callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * @author 李澄锋
 */
public abstract class StringCallback extends Callback<String>
{
    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException
    {
        return response.body().string();
    }
}
