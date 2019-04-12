package com.core.lib_core.widgets;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.lib_core.R;

/**
 * app通用的对话框组件
 *
 * @author 李澄锋<br>
 * 2015-4-8上午11:53:40
 * @author coffee <br>
 * 2016-10-5下午7:54:38 按照Activity|Fragment的加载 重构findviewById
 */
public class AppDialog extends BaseDialog {

    private TextView mTitle;
    private TextView mContent;
    private TextView mCancel, mConfirm;
    // 按钮相关操作按钮的父Layout
    private LinearLayout mActionLayout;
    private String cancelLabel, confirmLabel;

    private boolean canceledOnTouchOutside = true;

    public AppDialog(Activity context) {
        super(context);
    }

    protected void findViewById() {
        super.setContentView(R.layout.app_dialog);
        super.findViewById();
        // 标题
        this.mTitle = (TextView) findViewById(R.id.app_dialog_title);
        this.mTitle.setVisibility(View.GONE);
        // 内容
        this.mContent = (TextView) findViewById(R.id.app_dialog_content);
        if (this.mContent != null) {
            this.mContent.setVisibility(View.INVISIBLE);
        }
        // int contentStyle = context.getResources().getIdentifier("app_dialog_content_style", "style", context.getPackageName());
        // this.mContent.setTextAppearance(getContext(), contentStyle);
        // 取消按钮
        this.mCancel = (TextView) findViewById(R.id.app_dialog_cancel);
        // 确认按钮
        this.mConfirm = (TextView) findViewById(R.id.app_dialog_ok);
        //
        mActionLayout = (LinearLayout) findViewById(R.id.app_dialog_action_layout);
    }

    public AppDialog(Activity context, boolean canceledOnTouchOutside) {
        this(context);
        this.canceledOnTouchOutside = canceledOnTouchOutside;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(mContentView);
        setCanceledOnTouchOutside(canceledOnTouchOutside);
    }

    @Override
    public final void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }

    /**
     * 覆盖父类<br>
     * 小米手机调用该方法的时候, 不起作用, 创建的时候需要调用 {@link # AppDialog(Activity, false)} 第二个参数设置为false
     */
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        if (flag == false) {
            setCanceledOnTouchOutside(false);
        }
    }

    public AppDialog setTitle(String title) {
        this.mTitle.setText(title);
        this.mTitle.setVisibility(View.VISIBLE);
        return this;
    }

    public void setTitle(String title, int gravity) {
        this.setTitle(title);
        this.mTitle.setGravity(gravity);
    }

    public AppDialog setContent(String content) {
        this.mContent.setText(content);
        this.mContent.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(mTitle.getText())) {
            mTitle.setVisibility(View.GONE);
            mContent.setTextColor(Color.parseColor("#212121"));
        } else {
            mContent.setTextColor(Color.parseColor("#757575"));
        }
        return this;
    }

    public void setContent(String content, int gravity) {
        setContent(content);
        this.mContent.setGravity(gravity);
    }

    public AppDialog setCancelClickListener(String label, View.OnClickListener cancelClickListener) {
        this.mCancel.setText(label);
        this.cancelLabel = label;
        this.mCancel.setOnClickListener(cancelClickListener);
        updateActionLayout();
        if (cancelClickListener == null) {
            this.mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel();
                }
            });
        }
        return this;
    }

    public AppDialog setConfirmClickListener(String label, View.OnClickListener confirmClickListener) {
        this.mConfirm.setText(label);
        this.confirmLabel = label;
        this.mConfirm.setOnClickListener(confirmClickListener);
        updateActionLayout();
        if (confirmClickListener == null) {
            this.mConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel();
                }
            });
        }
        return this;
    }

    private void updateActionLayout() {
        if (cancelLabel == null) {
            this.mCancel.setVisibility(View.GONE);
            mActionLayout.getChildAt(1).setVisibility(View.GONE);
        } else {
            this.mCancel.setVisibility(View.VISIBLE);
        }
        if (confirmLabel == null) {
            this.mConfirm.setVisibility(View.GONE);
            mActionLayout.getChildAt(1).setVisibility(View.GONE);
        } else {
            this.mConfirm.setVisibility(View.VISIBLE);
        }
        // 如果两个按钮都显示了 则右侧显示
        if (mCancel.getVisibility() == View.VISIBLE && mConfirm.getVisibility() == View.VISIBLE) {
            ((LinearLayout) mCancel.getParent()).setGravity(Gravity.RIGHT);
        } else {
            ((LinearLayout) mCancel.getParent()).setGravity(Gravity.RIGHT);
        }
    }
}
