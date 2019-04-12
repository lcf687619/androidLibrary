package com.core.lib_core.fragment;

import com.core.lib_core.activity.BaseActivity;
import com.core.lib_core.widgets.Alert;

/**
 * 非public.用的时候直接拷贝到子项目中。用法同 {@link BaseActivity} 一样
 *
 * @author 李澄锋<br>
 * 2014年9月19日下午3:42:50
 */
public abstract class BaseFragment extends FrameBaseFragment {

    public abstract String getFragmentName();

    @Override
    protected void findViewById() {
        super.findViewById();
    }

    public boolean isEmpty(Object str) {
        if (str == null || str.toString().trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void showToast(String content) {
        Alert.toast(content);
    }
}
