package com.core.lib_core.widgets;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.core.lib_core.R;

/**
 * @author hazimi
 * @time 2015年4月28日 18:26:51
 * @describe 用于自定义字体的TextView
 */
@SuppressLint("AppCompatCustomView")
public class SFBaseTextView extends TextView {

    public Context mContext;

    public SFBaseTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SFBaseTextView);
        String name = a.getString(R.styleable.SFBaseTextView_font_name);
        a.recycle();
        mContext = context;
        SFinitFontStyle(context, name);
        initOnLongTouchClickEvent();
    }

    public SFBaseTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mContext = context;
        initOnLongTouchClickEvent();
    }

    public SFBaseTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SFBaseTextView);
        String name = a.getString(R.styleable.SFBaseTextView_font_name);
        a.recycle();
        mContext = context;
        initOnLongTouchClickEvent();
        SFinitFontStyle(context, name);
    }

    void initOnLongTouchClickEvent() {
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(SFBaseTextView.this.getText().toString());
                Alert.toast("复制成功");
                return false;
            }
        });
    }

    /**
     * 初始化字体的显示样式
     */
    private void SFinitFontStyle(Context context, String font_name) {
        if (font_name != null || (!"".equals(font_name))) {
            try {
                AssetManager assManger = context.getAssets();
                Typeface font = Typeface.createFromAsset(assManger, "fonts/" + font_name);
                setTypeface(font);
            } catch (Exception e) {
                // TODO: handle exception
//				NiceLogUtil.D("       SFBaseTextView-->font_name"+e.toString());
            }
        }
    }
}
