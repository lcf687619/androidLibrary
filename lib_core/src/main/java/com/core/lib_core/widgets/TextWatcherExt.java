package com.core.lib_core.widgets;

import android.text.Editable;
import android.text.TextWatcher;

import com.core.lib_core.utils.StringUtil;

/**
 * 用于格式化文本
 *
 * @author 李澄锋 <br>
 * 2016-9-1下午1:41:42
 */
public class TextWatcherExt implements TextWatcher {

    private int type;

    /**
     * 过滤手机号
     */
    private final int TYPE_PHONE = 1;
    /**
     * 银行卡类型的
     */
    private final int TYPE_CARD = 2;

    public TextWatcherExt() {
        // 无操作
    }

    public TextWatcherExt(int type) {
        this.type = type;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Log.d("input-befor", s + " " + start + " " + count + " " + after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Log.d("input-changed", s + " " + start + " " + before + " " + count + " ");
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // Log.d("input-after", editable);
        if (type == TYPE_PHONE) {
            StringUtil.format(editable, 3, 7);
        } else if (type == TYPE_CARD) {// 银行卡
            StringUtil.format(editable, 4, 8);
        }

    }

}
