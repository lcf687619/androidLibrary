package com.core.lib_core.api;

import java.util.Map;

/**
 * Created by wangjianjun on 17/5/11.
 */
public abstract class BaseRequestApi {

    public abstract String getBaseUrl();
    public abstract String getUrlName();
    public abstract String getOfficialUrl();
    public abstract String getDevelopUrl();
    public abstract String getQaUrl();
    public abstract String getPptUrl();
    public abstract String getEditUrl();
    public abstract Map<String, String> configRequestParams(Map<String, String> map);

}
