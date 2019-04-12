package com.core.lib_core.widgets;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.core.lib_core.R;

import java.lang.reflect.Field;

public class GsToolBar extends Toolbar {

    public GsToolBar(Context context) {
        super(context);
        initView();
    }

    public GsToolBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    private void initView() {
        View view = getNavButtonView(this);
        Toolbar.LayoutParams lp = (Toolbar.LayoutParams) view.getLayoutParams();
        lp.width = (int) getResources().getDimension(R.dimen.top_bar_height);
        lp.height = (int) getResources().getDimension(R.dimen.top_bar_height);
        view.setLayoutParams(lp);
    }


    private View getNavButtonView(Toolbar toolbar) {
        try {
            Class<?> toolbarClass = Toolbar.class;
            Field navButtonField = toolbarClass.getDeclaredField("mNavButtonView");
            navButtonField.setAccessible(true);
            View navButtonView = (View) navButtonField.get(toolbar);

            return navButtonView;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}