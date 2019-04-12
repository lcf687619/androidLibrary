package com.core.lib_core.api;

import android.app.Activity;

public abstract class BaseApiHandler {

    protected Activity mActivity;

    public BaseApiHandler(Activity activity) {
        this.mActivity = activity;
    }

    public abstract void handleTokenInvalid();
}