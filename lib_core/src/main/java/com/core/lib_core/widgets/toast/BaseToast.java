package com.core.lib_core.widgets.toast;

import android.app.Application;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.core.lib_core.R;

/**
 * desc   : Toast基类
 */
abstract class BaseToast extends Toast implements Runnable {

    // 显示延迟时间，避免重复点击
    static final int SHOW_DELAY_MILLIS = 300;

    // 吐司消息View
    private TextView mMessageView;

    // 吐司显示的文本
    private CharSequence mText;

    BaseToast(Application application) {
        super(application);
    }

    @Override
    public void setView(View view) {
        super.setView(view);
        if (view instanceof TextView) {
            mMessageView = (TextView) view;
            return;
        } else if (view.findViewById(R.id.toast_content) instanceof TextView) {
            mMessageView = ((TextView) view.findViewById(R.id.toast_content));
            return;
        } else if (view instanceof ViewGroup) {
            if ((mMessageView = findTextView((ViewGroup) view)) != null)
                return;
        }
        // 如果设置的布局没有包含一个 TextView 则抛出异常，必须要包含一个 TextView 作为 Message View
        throw new IllegalArgumentException("The layout must contain a TextView");
    }

    /**
     * 获取当前的消息 View
     */
    TextView getMessageView() {
        return mMessageView;
    }

    /**
     * 获取当前欲显示的文本
     */
    CharSequence getText() {
        return mText;
    }

    @Override
    public void setText(CharSequence s) {
        // 记录本次吐司欲显示的文本
        mText = s;
    }

    /**
     * 递归获取ViewGroup中的TextView对象
     */
    private static TextView findTextView(ViewGroup group) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View view = group.getChildAt(i);
            if ((view instanceof TextView)) {
                return (TextView) view;
            } else if (view instanceof ViewGroup) {
                TextView textView = findTextView((ViewGroup) view);
                if (textView != null) return textView;
            }
        }
        return null;
    }
}