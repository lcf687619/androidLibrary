package com.core.lib_core.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by hazimi
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private int mDividerHeight = 1;//分割线高度，默认为1px
    private Drawable mDivider;

    private int mOrientation;
    private MODE mMODE = MODE.DRAWABLE; //模式, 默认是画drawable
    private boolean mLastDividerFlag = true; //是否添加最后一个item的分割线
    private boolean mFirstDividerFlag = true; //是否添加第一个item的分割线
    private int mLineColor = 0xffdddddd;

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;


    enum MODE{
        COLOR,
        DRAWABLE;
    }

    /**
     * 默认分割线 高度2px, 颜色 0xffdddddd, 最后一个item也添加
     *
     * @param context
     * @param orientation 列表方向
     */
    public DividerItemDecoration(Context context, int orientation) {
        //默认分割线listview自带分割线
//        mMODE = MODE.DRAWABLE;
//        final TypedArray a = context.obtainStyledAttributes(ATTRS);
//        mDivider = a.getDrawable(0);
//        a.recycle();
//        setOrientation(orientation);
        this(context, orientation, true, true);

    }

    /**
     * 是否添加第一条 最后一条分割线
     * @param lastDividerFlag  最后一条
     * @param firstDividerFlag 第一条
     */
    public DividerItemDecoration(Context context, int orientation, boolean firstDividerFlag, boolean lastDividerFlag) {
        this(context, orientation, 1, 0xffdddddd, firstDividerFlag, lastDividerFlag);

    }

    /**
     * 是否添加最后一条分割线
     * @param lastDividerFlag
     */
    public DividerItemDecoration(Context context, int orientation, boolean lastDividerFlag) {
        this(context, orientation, 1, 0xffdddddd, true, lastDividerFlag);

    }

    /**
     * 自定义分割线图片
     *
     * @param context
     * @param orientation 列表方向
     * @param drawableId  分割线图片
     */
    public DividerItemDecoration(Context context, int orientation, int drawableId) {
        mMODE = MODE.DRAWABLE;
        mDivider = ContextCompat.getDrawable(context, drawableId);
        mDividerHeight = mDivider.getIntrinsicHeight();
        setOrientation(orientation);
    }

    /**
     * 自定义分割线颜色和高度
     *
     * @param context
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public DividerItemDecoration(Context context, int orientation, int dividerHeight, @ColorInt int dividerColor) {
        this(context, orientation, dividerHeight,dividerColor, true, true);
    }

    /**
     * 自定义分割线颜色和高度
     *
     * @param context
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     * @param lastDividerFlag  是否给最后一个item添加分割线
     */
    public DividerItemDecoration(Context context, int orientation, int dividerHeight, @ColorInt int dividerColor, boolean firstDividerFlag, boolean lastDividerFlag) {
        mMODE = MODE.COLOR;
        mLastDividerFlag = lastDividerFlag;
        mFirstDividerFlag = firstDividerFlag;
        mDividerHeight = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }

    }


    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        if(!mLastDividerFlag){
            childCount = Math.max(0, --childCount);
        }
        int firstIndex = 0;
        if(!mFirstDividerFlag){
            firstIndex = Math.min(childCount, 1);
        }
        for (int i = firstIndex; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView v = new RecyclerView(parent.getContext());
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            if(mMODE == MODE.DRAWABLE){
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }else if(mMODE == MODE.COLOR){
                final int bottom = top + mDividerHeight ;
                c.drawRect(left,top,right,bottom,mPaint);
            }

        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        int childCount = parent.getChildCount();
        if(!mLastDividerFlag){
            childCount = Math.max(0, --childCount);
        }
        int firstIndex = 0;
        if(!mFirstDividerFlag){
            firstIndex = Math.min(childCount, 1);
        }
        for (int i = firstIndex; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            if(mMODE == MODE.DRAWABLE){
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }else if(mMODE == MODE.COLOR){
                final int right = left + mDividerHeight ;
                c.drawRect(left,top,right,bottom,mPaint);
            }

        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(mMODE == MODE.DRAWABLE){
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }else if(mMODE == MODE.COLOR){
            if(mOrientation == LinearLayoutManager.VERTICAL){
                outRect.set(0,0,0,mDividerHeight);
            }else {
                outRect.set(0,0,mDividerHeight,0);
            }
        }
    }
}
