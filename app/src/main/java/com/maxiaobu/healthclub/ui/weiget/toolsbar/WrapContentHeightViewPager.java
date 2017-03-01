package com.maxiaobu.healthclub.ui.weiget.toolsbar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.maxiaobu.healthclub.App;

import java.util.HashMap;
import java.util.LinkedHashMap;

import maxiaobu.mxbutilscodelibrary.ScreenUtils;

/**
 * Created by 莫玉婷 on 2017/1/3.
 * introduction：我长得真他娘的磕碜，单身未娶，求包养
 * email：maxiaobu1216@gmail.com
 * 功能：appbarlayout 包裹内容viewpager
 * 伪码：
 * 待完成：
 */
public class WrapContentHeightViewPager extends ViewPager {

    private int current;
    private int height = 0;
    private int h = 0;

    public void setH(int h) {
        this.h = h;
    }

    /**
     * 保存position与对于的View
     */
    private HashMap<Integer, View> mChildrenViews = new LinkedHashMap<>();

    private boolean scrollble = true;

    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mChildrenViews.size() > current) {
            View child = mChildrenViews.get(current);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            height = child.getMeasuredHeight();
            if (height < ScreenUtils.getScreenHeight(App.getInstance()) - h) {
                height = ScreenUtils.getScreenHeight(App.getInstance()) - h;
            }
//            height = height + 100;
        }
        Log.d("WrapContentHeightViewPa", "height:" + (height - (ScreenUtils.getScreenHeight(App.getInstance()) - h)));

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height , MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void resetHeight(int current) {
        this.current = current;
        if (mChildrenViews.size() > current) {

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
            } else {
                layoutParams.height = height;
            }
            setLayoutParams(layoutParams);
        }
    }

    public int getheight() {
        return height - (ScreenUtils.getScreenHeight(App.getInstance()) - h);
    }

    /**
     * 保存position与对于的View
     */
    public void setObjectForPosition(View view, int position) {
        mChildrenViews.put(position, view);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble) {
            return true;
        }
        return super.onTouchEvent(ev);
    }


    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }

}