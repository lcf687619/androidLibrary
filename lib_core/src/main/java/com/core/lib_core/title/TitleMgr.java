package com.core.lib_core.title;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class TitleMgr extends AppTitle {

    private ViewGroup mTitleLeft, mTitleRight;

    private View mTitleMiddle;
    /**
     * 返回到上一页||通用Title
     */
    private TitleRes mBackTitleRes;

    private TitleRes titleRes1, titleRes2, titleRes3;

    public TitleMgr(Object activityOrFragment) {
        super(activityOrFragment);
    }

    @Override
    public AppTitle initTitle() {
        int resLeft = getResources().getIdentifier("title_left_switcher", "id", getPackageName());
        int resMiddle = getResources().getIdentifier("title_middle_switcher", "id", getPackageName());
        int resRight = getResources().getIdentifier("title_right_switcher", "id", getPackageName());
        //
        if (resLeft != 0) {
            mTitleLeft = (ViewGroup) findViewById(resLeft);
        }
        if (resMiddle != 0) {
            mTitleMiddle = findViewById(resMiddle);
        }
        if (resRight != 0) {
            mTitleRight = (ViewGroup) findViewById(resRight);
        }
        //
        int resBack = getBackDrawable();
        if (resBack != 0) {
            // resBack = getResources().getIdentifier("title_back", "drawable", getPackageName());
            setBackDrawable(resBack);// 重置一下
            mBackTitleRes = new TitleRes(1, resBack, mBackClickListener);
        } else {
            if (mTitleLeft != null) {
                Drawable drawableBack = ((ImageView) mTitleLeft.getChildAt(1)).getDrawable();
                mBackTitleRes = new TitleRes(1, drawableBack, mBackClickListener);
            }
        }
        return this;
    }

    @Override
    public AppTitle setTitle(TitleRes... reses) {
        for (int i = 0; i < reses.length; i++) {
            TitleRes res = reses[i];
            setTitle(res, i);
        }
        return this;
    }

    @Override
    public AppTitle setCommonTitle(String titleText, TitleRes rightTitle) {
        setTitle(new TitleRes[]{mBackTitleRes, new TitleRes(0, titleText, null), rightTitle});
        return this;
    }

    @Override
    public AppTitle setCommonTitle(String titleText) {
        setCommonTitle(titleText, null);
        return this;
    }

    @Override
    public AppTitle setTitle(TitleRes res, int position) {
        if (position == 0) {
            handleTitle(getTitleLeft(), res, 0);
            this.titleRes1 = res;
        } else if (position == 1) {
            handleTitle(getTitleMiddle(), res, 1);
            this.titleRes2 = res;
        } else if (position == 2) {
            handleTitle(getTitleRight(), res, 2);
            this.titleRes3 = res;
        }
        return this;
    }

    /**
     * 左中右
     *
     * @param res
     * @param type     0-文字 1-图片
     * @param position 0-1-2 左中右
     */
    public void setTitleResource(Object res, int type, int position) {
        View view = mTitleLeft;
        if (position == 1) {
            view = mTitleMiddle;
        } else if (position == 2) {
            view = mTitleRight;
        }
        // 文本
        if (type == 0) {
            ViewSwitcher viewSwitcher = (ViewSwitcher) view;
            viewSwitcher.setDisplayedChild(0);
            TextView textView = (TextView) viewSwitcher.getChildAt(0);
            if (res instanceof Integer) {
                textView.setText(Integer.valueOf(res.toString()));
            } else {
                // 支持String|以及SpannableString等
                textView.setText((CharSequence) res);
            }
        } else {// 图片
            ViewSwitcher viewSwitcher = (ViewSwitcher) view;
            viewSwitcher.setDisplayedChild(1);
            ImageView imageView = (ImageView) viewSwitcher.getChildAt(1);
            if (res instanceof Integer) {
                imageView.setImageResource(Integer.valueOf(res.toString()));
            } else if (res instanceof Drawable) {
                imageView.setImageDrawable((Drawable) res);
            }

        }
    }

    public void handleTitle(View view, TitleRes titleRes, int position) {
        if (view == null) {
            return;
        }
        if (titleRes == null) {
            view.setVisibility(View.INVISIBLE);
            // 让该view占指定资源的空间
            // view.setBackgroundResource(R.drawable.title_back);
            return;
        }
        view.setVisibility(View.VISIBLE);
        setTitleResource(titleRes.getRes(), titleRes.getType(), position);
        //
        if (titleRes.getClickListener() != null) {
            view.setOnClickListener(titleRes.getClickListener());
        }
    }

    private View.OnClickListener mBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (context instanceof Activity) {
                ((Activity) context).finish();
            } else if (context instanceof Fragment) {
                ((Fragment) context).getActivity().finish();
            }
        }
    };

    public ViewGroup getTitleLeft() {
        return mTitleLeft;
    }

    public ViewGroup getTitleRight() {
        return mTitleRight;
    }

    public View getTitleMiddle() {
        return mTitleMiddle;
    }

    public TitleRes getBackTitle() {
        return mBackTitleRes;
    }

    public View.OnClickListener getBackClickListener() {
        return mBackClickListener;
    }

    @Override
    public TitleRes[] getTitlereReses() {
        return new TitleRes[]{titleRes1, titleRes2, titleRes3};
    }
}
